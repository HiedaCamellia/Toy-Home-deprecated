package cn.solarmoon.toy_home.core.data.tag;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class THItemTags extends ItemTagsProvider {

    public THItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, ToyHome.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        tag(HUGGABLE).add(
                THItems.TEDDY_BEAR_TOY.get(),
                THItems.PANDA_TOY.get(),
                THItems.POLAR_BEAR_TOY.get(),
                THItems.BLAVINGAD_TOY.get()
        );
    }

    //特殊效果
    //可抱起来
    public static final TagKey<Item> HUGGABLE = itemTag("huggable");

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(ToyHome.MOD_ID, path));
    }

    private static TagKey<Item> forgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

}
