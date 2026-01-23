package com.shadow_ninja.narutoreborn.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.data.NinjaDataHandler;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuUseService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = NarutoReborn.MODID)
public final class NetworkInit {
    private NetworkInit() {
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        registrar.playToServer(
                UseQuickbarNinjutsuC2SPayload.TYPE,
                UseQuickbarNinjutsuC2SPayload.STREAM_CODEC,
                NetworkInit::handleUseQuickbar
        );
    }

    private static void handleUseQuickbar(UseQuickbarNinjutsuC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            int slot = payload.slot();
            if (slot < 0) {
                return;
            }
            var quickbar = NinjaDataHandler.instance().getQuickbar(player);
            if (slot >= quickbar.size()) {
                return;
            }
            ResourceLocation id = quickbar.get(slot);
            var result = NinjutsuUseService.tryUse(player, id, Double.NaN);
            if (!result.success() && result.message() != null) {
                player.displayClientMessage(result.message(), true);
            }
        });
    }
}
