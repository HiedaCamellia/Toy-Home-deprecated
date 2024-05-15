package cn.solarmoon.toy_home.core.data.tag;

import cn.solarmoon.toy_home.core.ToyHome;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class THFluidTags extends FluidTagsProvider {

    public THFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, ToyHome.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {

    }

    public static final TagKey<Fluid> HOT_FLUID = fluidTag("hot_fluid");
    public static final TagKey<Fluid> WARM_FLUID = fluidTag("warm_fluid");

    private static TagKey<Fluid> fluidTag(String path) {
        return FluidTags.create(new ResourceLocation(ToyHome.MOD_ID, path));
    }

}
