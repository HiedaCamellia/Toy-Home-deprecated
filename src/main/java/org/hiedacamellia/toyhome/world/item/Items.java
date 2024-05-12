package org.hiedacamellia.toyhome.world.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.hiedacamellia.toyhome.register.AutoRegistryObject;
import org.hiedacamellia.toyhome.world.block.Blocks;

public class Items {
    public static final AutoRegistryObject<Item> A_BLOCK = AutoRegistryObject.of(() -> new BlockItem(Blocks.A_BLOCK.get(), new Item.Properties()));
    public static final AutoRegistryObject<Item> B_BLOCK = AutoRegistryObject.of(() -> new BlockItem(Blocks.B_BLOCK.get(), new Item.Properties()));
}
