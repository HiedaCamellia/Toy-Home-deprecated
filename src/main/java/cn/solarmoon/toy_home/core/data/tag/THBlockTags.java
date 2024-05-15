package cn.solarmoon.toy_home.core.data.tag;

import cn.solarmoon.toy_home.core.ToyHome;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class THBlockTags extends BlockTagsProvider {

    public THBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToyHome.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {

    }

    public static final TagKey<Block> ROLLABLE = blockTag("rollable");
    public static final TagKey<Block> HEAT_SOURCE = blockTag("heat_source");
    public static final TagKey<Block> MINEABLE_WITH_CLEAVER = blockTag("mineable/cleaver");
    public static final TagKey<Block> SOUP_CONTAINER = blockTag("soup_container");
    public static final TagKey<Block> CUTTING_BOARD = blockTag("cutting_board");

    private static TagKey<Block> blockTag(String path) {
        return BlockTags.create(new ResourceLocation(ToyHome.MOD_ID, path));
    }

    private static TagKey<Block> forgeBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("forge", path));
    }

}

