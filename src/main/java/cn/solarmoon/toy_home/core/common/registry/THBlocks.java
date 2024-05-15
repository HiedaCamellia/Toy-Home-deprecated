package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.BlockEntry;
import cn.solarmoon.toy_home.api.common.block.PillowBlock;
import cn.solarmoon.toy_home.core.ToyHome;
import net.minecraft.world.level.block.Block;

public class THBlocks {
    public static void register() {}

    public static final BlockEntry<Block> TEDDY_BEAR_TOY = ToyHome.REGISTRY.block()
            .id("teddy_bear_toy")
            .bound(PillowBlock::new)
            .build();

    public static final BlockEntry<Block> PANDA_TOY = ToyHome.REGISTRY.block()
            .id("panda_toy")
            .bound(PillowBlock::new)
            .build();

    public static final BlockEntry<Block> POLAR_BEAR_TOY = ToyHome.REGISTRY.block()
            .id("polar_bear_toy")
            .bound(PillowBlock::new)
            .build();

    public static final BlockEntry<Block> BLAVINGAD_TOY = ToyHome.REGISTRY.block()
            .id("blavingad_toy")
            .bound(PillowBlock::new)
            .build();

}
