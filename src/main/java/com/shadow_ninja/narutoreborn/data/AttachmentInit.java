package com.shadow_ninja.narutoreborn.data;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentInit {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NarutoReborn.MODID);

    public static final Supplier<AttachmentType<NinjaData>> NINJA_DATA =
            ATTACHMENT_TYPES.register("ninja_data", () -> AttachmentType.serializable(NinjaData::new)
                    .sync(NinjaData.SYNC_HANDLER)
                    .copyOnDeath()
                    .build());
}
