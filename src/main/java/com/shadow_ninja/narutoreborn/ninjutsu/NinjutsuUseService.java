package com.shadow_ninja.narutoreborn.ninjutsu;

import com.shadow_ninja.narutoreborn.data.AttachmentInit;
import com.shadow_ninja.narutoreborn.data.NinjaData;
import com.shadow_ninja.narutoreborn.data.NinjaDataHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Centralized use flow with server-side validation and state updates.
 */
public final class NinjutsuUseService {
    private NinjutsuUseService() {
    }

    public static NinjutsuUseResult tryUse(ServerPlayer player, ResourceLocation ninjutsuId, double powerOverrideOrNaN) {
        Ininjutsu ninjutsu = NinjutsuRegistry.get(ninjutsuId).orElse(null);
        if (ninjutsu == null) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.unknown", ninjutsuId.toString()));
        }

        NinjaData data = NinjaDataHandler.instance().get(player);
        Optional<NinjutsuContainer> optContainer = data.findContainer(ninjutsuId);
        if (optContainer.isEmpty()) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.not_owned"));
        }
        NinjutsuContainer container = optContainer.get();

        if (!container.enabled()) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.disabled"));
        }
        if (!container.unlocked()) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.locked"));
        }

        if (data.getLevel() < ninjutsu.getRequiredLevel()) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.level_requirement", ninjutsu.getRequiredLevel()));
        }

        double basePower = Double.isNaN(container.power()) ? ninjutsu.getPower() : container.power();
        double power = Double.isNaN(powerOverrideOrNaN) ? basePower : powerOverrideOrNaN;
        double minPower = Double.isNaN(container.minPower()) ? ninjutsu.getMinPower() : container.minPower();
        double maxPower = Double.isNaN(container.maxPower()) ? ninjutsu.getMaxPower() : container.maxPower();
        double clamped = Math.max(minPower, Math.min(maxPower, power));
        ninjutsu.setPower(clamped);

        double cost = container.chakraCost() > 0.0d ? container.chakraCost() : ninjutsu.getCost();
        if (data.getChakraCurrent() < cost) {
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.chakra_not_enough", cost));
        }

        long gameTime = player.level().getGameTime();
        long cooldownTicks = Math.max(0L, Math.round(ninjutsu.getCooldown() * 20.0));
        if (container.isCoolingDown(gameTime)) {
            double remainSec = container.remainingCooldownSeconds(gameTime);
            return NinjutsuUseResult.fail(Component.translatable("message.narutoreborn.ninjutsu.cooldown", String.format("%.1f", remainSec)));
        }

        data.setChakraCurrent(data.getChakraCurrent() - cost);
        container.startCooldown(gameTime, cooldownTicks);
        container.setDurationRemainingTicks(Math.max(0, ninjutsu.getDuration() * 20));

        ninjutsu.use(player, player.level());
        player.setData(AttachmentInit.NINJA_DATA.get(), data);
        return NinjutsuUseResult.success(Component.translatable("message.narutoreborn.ninjutsu.used", ninjutsu.getDisplayName(), clamped));
    }
}
