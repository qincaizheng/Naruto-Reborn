package com.shadow_ninja.narutoreborn;

import net.minecraft.server.level.ServerPlayer;
import com.shadow_ninja.narutoreborn.command.NinjutsuCommands;
import com.shadow_ninja.narutoreborn.data.NinjaData;
import com.shadow_ninja.narutoreborn.data.NinjaDataHandler;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjaDefaults;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = NarutoReborn.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        NinjutsuRegistry.bootstrap();
        NinjutsuCommands.register(event);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }
        NinjaData data = NinjaDataHandler.instance().get(sp);
        NinjaDefaults.applyIfEmpty(data);
        NinjaDataHandler.instance().save(sp, data);
    }
}
