package com.shadow_ninja.narutoreborn.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.datagen.tag.ModBlockTagsProvider;
import com.shadow_ninja.narutoreborn.datagen.tag.ModDamageTypeTagsProvider;
import com.shadow_ninja.narutoreborn.datagen.tag.ModItemTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = NarutoReborn.MODID)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lp = event.getLookupProvider();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        // 物品模型
        generator.addProvider(
                event.includeClient(),
                new ModItemModelProvider(packOutput,NarutoReborn.MODID,efh)
        );
        // 方块state
        generator.addProvider(
                event.includeClient(),
                new ModBlockStateProvider(packOutput,NarutoReborn.MODID,efh)
        );
        // loot table
//        generator.addProvider(
//                event.includeServer(),
//                (DataProvider.Factory<ModLootTableProvider>)pOutput -> new ModLootTableProvider(pOutput, Collections.emptySet(),
//                        List.of(
//                                new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
//                        ),lp)
//        );
        generator.addProvider(
                event.includeServer(),
                new ModDatapackProvider(packOutput,lp)
        );
        generator.addProvider(
                event.includeServer(),
                new ModDamageTypeTagsProvider(packOutput, lp, efh)
        );
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, lp, efh);
        generator.addProvider(
                event.includeServer(),
                blockTagsProvider
        );
        generator.addProvider(
                event.includeServer(),
                (DataProvider.Factory<TagsProvider<Item>>) output -> new ModItemTagsProvider(output, lp, blockTagsProvider.contentsGetter(), efh)
        );

        // 语言文件
        generator.addProvider(
                event.includeClient(),
                new ModLanguageProvider(packOutput,NarutoReborn.MODID,"en_us")
        );
        generator.addProvider(
                event.includeClient(),
                new ModLanguageProvider(packOutput,NarutoReborn.MODID,"zh_cn")
        );

    }
}
