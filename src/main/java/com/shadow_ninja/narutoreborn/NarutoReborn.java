package com.shadow_ninja.narutoreborn;

import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import com.shadow_ninja.narutoreborn.data.AttachmentInit;
import com.shadow_ninja.narutoreborn.entity.EntityInit;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(NarutoReborn.MODID)
public class NarutoReborn {
    public static final String MODID = "narutoreborn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NarutoReborn(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NinjutsuRegistry.bootstrap();
        AttachmentInit.ATTACHMENT_TYPES.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
    }
    public static ResourceLocation createRL(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
