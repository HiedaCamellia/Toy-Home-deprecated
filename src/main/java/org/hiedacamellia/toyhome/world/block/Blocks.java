package org.hiedacamellia.toyhome.world.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.hiedacamellia.toyhome.register.AutoRegistryObject;

public class Blocks {
    public static final AutoRegistryObject<Block> A_BLOCK = AutoRegistryObject.of(() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final AutoRegistryObject<Block> B_BLOCK = AutoRegistryObject.of(() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
}
