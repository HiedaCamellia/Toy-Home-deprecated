package org.hiedacamellia.toyhome.datagenerator;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.hiedacamellia.toyhome.Toyhome;

import java.util.Collection;

public class SingleTextureBlockModelGenerator extends BlockStateProvider {

    private final Collection<Block> blocks;

    public SingleTextureBlockModelGenerator(Collection<Block> blocks, PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Toyhome.MODID, existingFileHelper);
        this.blocks = blocks;
    }

    @Override
    protected void registerStatesAndModels() {
        blocks.forEach(block -> simpleBlockWithItem(block, models().cubeAll(getNameFromBlock(block), blockTexture(block))));
    }

    private static String getNameFromBlock(Block block) {
        return getNameFromDescriptionId(block.getDescriptionId());
    }

    private static String getNameFromDescriptionId(String descriptionId) {
        String[] strings = descriptionId.split("\\.");
        return strings[strings.length - 1];
    }
}
