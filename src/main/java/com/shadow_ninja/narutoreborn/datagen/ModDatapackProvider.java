package com.shadow_ninja.narutoreborn.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import com.shadow_ninja.narutoreborn.NarutoReborn;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries,BUILDER, Set.of(NarutoReborn.MODID));
    }

    public static final RegistrySetBuilder BUILDER =new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE,bootstrap -> {

            });
}
