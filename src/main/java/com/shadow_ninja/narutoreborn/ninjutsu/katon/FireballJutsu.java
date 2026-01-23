package com.shadow_ninja.narutoreborn.ninjutsu.katon;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.entity.katon.KatonFireballEntity;
import com.shadow_ninja.narutoreborn.ninjutsu.BaseNinjutsu;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballJutsu extends BaseNinjutsu {
    public static final ResourceLocation ID = NarutoReborn.createRL("katon_fireball");

    public FireballJutsu() {
        super(new Builder()
                .power(1.0d)
                .maxPower(5.0d)
                .minPower(0.5d)
                .requiredLevel(1)
                .category("katon")
                .icon(NarutoReborn.createRL("textures/gui/ninjutsu/katon_fireball.png"))
                .displayName(Component.translatable("ninjutsu.narutoreborn.katon_fireball"))
                .tooltip(Component.translatable("ninjutsu.narutoreborn.katon_fireball.tooltip"))
                .range(24.0d)
                .cost(10.0d)
                .cooldown(4.0d)
                .duration(0)
                .unlocked(true)
        );
    }

    @Override
    public void use(Player player, Level level) {
        // 仅服务器执行产生实体/伤害，避免客户端自嗨。
        if (level.isClientSide) {
            return;
        }

        // 将 power clamp 到 min/max，避免外部未调用 setPower 的情况。
        setPower(getPower());

        ServerLevel serverLevel = (ServerLevel) level;

        double power = getPower();
        double range = getRange();
        final KatonFireballEntity fireball = getKatonFireballEntity(player, power, range, serverLevel);
        serverLevel.addFreshEntity(fireball);

        NarutoReborn.LOGGER.info("{} 使用了 {} (power={})", player.getScoreboardName(), getDisplayName().getString(), power);
        super.use(player, level);
    }

    private static @NotNull KatonFireballEntity getKatonFireballEntity(Player player, double power, double range, ServerLevel serverLevel) {
        float radius = Mth.clamp(2.5f + (float) power * 0.75f, 2.5f, 8.0f);
        float damage = Mth.clamp(4.0f + (float) power * 2.0f, 4.0f, 20.0f);
        int fireSeconds = Mth.clamp(3 + (int) Math.round(power), 3, 10);

        KatonFireballEntity fireball = new KatonFireballEntity(serverLevel, player,range, radius, damage, fireSeconds);
        // 从玩家视线方向发射
        Vec3 look = player.getLookAngle();
        fireball.setDeltaMovement(look.scale(1.2));
        fireball.setYRot(player.getYRot());
        fireball.setXRot(player.getXRot());
        return fireball;
    }

    public static boolean canAffect(Entity entity, LivingEntity attacker) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        if (living.isInvulnerable()) {
            return false;
        }
        // 不伤害施法者自己
        return living != attacker;
    }
}
