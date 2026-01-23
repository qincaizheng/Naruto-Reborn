package com.shadow_ninja.narutoreborn.client;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.client.renderer.KatonFireballRenderer;
import com.shadow_ninja.narutoreborn.entity.EntityInit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NarutoReborn.MODID)
public final class ClientSetupEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.KATON_FIREBALL.get(), KatonFireballRenderer::new);
    }
}
