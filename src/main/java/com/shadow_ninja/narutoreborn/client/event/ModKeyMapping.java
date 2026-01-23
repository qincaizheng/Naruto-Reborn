package com.shadow_ninja.narutoreborn.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.data.NinjaDataHandler;
import com.shadow_ninja.narutoreborn.network.UseQuickbarNinjutsuC2SPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = NarutoReborn.MODID, value = Dist.CLIENT)
public class ModKeyMapping {

    private static KeyMapping useNinjutsuKey;
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        useNinjutsuKey = new KeyMapping(
                "key." + NarutoReborn.MODID + ".use_ninjutsu",
                GLFW.GLFW_KEY_F,
                "key.categories.gameplay");
        event.register(useNinjutsuKey);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (useNinjutsuKey != null && useNinjutsuKey.consumeClick()) {
            triggerNinjutsuClient();
        }
    }

    private static void triggerNinjutsuClient() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        var quickbar = NinjaDataHandler.instance().getQuickbar(mc.player);
        if (quickbar.isEmpty()) {
            mc.player.displayClientMessage(Component.translatable("message.narutoreborn.quickbar.empty"), true);
            return;
        }
        // Multiplayer-safe: ask server to use quickbar slot 0
        PacketDistributor.sendToServer(new UseQuickbarNinjutsuC2SPayload(0));
    }
}
