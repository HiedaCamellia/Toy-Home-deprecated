package cn.solarmoon.toy_home.core.data;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.data.loot_table.THBlockLoots;
import cn.solarmoon.toy_home.core.data.tag.THBlockTags;
import cn.solarmoon.toy_home.core.data.tag.THFluidTags;
import cn.solarmoon.toy_home.core.data.tag.THItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用于加载数据
 */
@Mod.EventBusSubscriber(modid = ToyHome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    /**
     * 在运行runData时加载数据
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        THBlockTags blockTags = new THBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new THItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new THFluidTags(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(THBlockLoots::new, LootContextParamSets.BLOCK)
        )));
    }

}
