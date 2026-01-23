package com.shadow_ninja.narutoreborn.entity;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import com.shadow_ninja.narutoreborn.entity.katon.KatonFireballEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NarutoReborn.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<KatonFireballEntity>> KATON_FIREBALL = ENTITY_TYPES.register(
            "katon_fireball",
            () -> EntityType.Builder.<KatonFireballEntity>of(KatonFireballEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build(NarutoReborn.MODID + ":katon_fireball")
    );
}
