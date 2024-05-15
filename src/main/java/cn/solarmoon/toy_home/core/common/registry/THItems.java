package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleBlockItem;
import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleFoodItem;
import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleItem;
import cn.solarmoon.solarmoon_core.api.common.registry.ItemEntry;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.item.*;

public class THItems {
    public static void register() {}

    public static final ItemEntry<SimpleItem> TEDDY_BEAR_TOY = ToyHome.REGISTRY.item()
            .id("teddy_bear_toy")
            .bound(() -> new SimpleBlockItem(THBlocks.TEDDY_BEAR_TOY.get(), 1))
            .build();

    public static final ItemEntry<SimpleItem> PANDA_TOY = ToyHome.REGISTRY.item()
            .id("panda_toy")
            .bound(() -> new SimpleBlockItem(THBlocks.PANDA_TOY.get(), 1))
            .build();

    public static final ItemEntry<SimpleItem> POLAR_BEAR_TOY = ToyHome.REGISTRY.item()
            .id("polar_bear_toy")
            .bound(() -> new SimpleBlockItem(THBlocks.POLAR_BEAR_TOY.get(), 1))
            .build();

    public static final ItemEntry<SimpleItem> BLAVINGAD_TOY = ToyHome.REGISTRY.item()
            .id("blavingad_toy")
            .bound(() -> new SimpleBlockItem(THBlocks.BLAVINGAD_TOY.get(), 1))
            .build();

    public static final ItemEntry<PaperRollWhistleItem> PAPER_ROLL_WHISTLE = ToyHome.REGISTRY.item()
            .id("paper_roll_whistle")
            .bound(PaperRollWhistleItem::new)
            .build();

    public static final ItemEntry<StickyHandItem> STICKY_HAND = ToyHome.REGISTRY.item()
            .id("sticky_hand")
            .bound(StickyHandItem::new)
            .build();

    public static final ItemEntry<ToyGunItem> TOY_GUN = ToyHome.REGISTRY.item()
            .id("toy_gun")
            .bound(ToyGunItem::new)
            .build();

    public static final ItemEntry<SimpleItem> SPONGE_BOB = ToyHome.REGISTRY.item()
            .id("sponge_bob")
            .bound(SimpleItem::new)
            .build();

    public static final ItemEntry<BaseballBatItem> BASEBALL_BAT = ToyHome.REGISTRY.item()
            .id("baseball_bat")
            .bound(BaseballBatItem::new)
            .build();

    public static final ItemEntry<FortuneCookieItem> FORTUNE_COOKIE = ToyHome.REGISTRY.item()
            .id("fortune_cookie")
            .bound(FortuneCookieItem::new)
            .build();

    public static final ItemEntry<SlipItem> SLIP = ToyHome.REGISTRY.item()
            .id("slip")
            .bound(SlipItem::new)
            .build();

}
