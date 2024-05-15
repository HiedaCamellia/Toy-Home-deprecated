package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.CreativeTabEntry;
import cn.solarmoon.toy_home.core.ToyHome;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class THCreativeModeTab {
    public static void register() {}

    public static final CreativeTabEntry COMMON = ToyHome.REGISTRY.creativeTab()
            .id(ToyHome.MOD_ID)
            .builder(CreativeModeTab.builder()
                    .title(ToyHome.TRANSLATOR.set("creative_mode_tab", "main"))
                    .icon(() -> new ItemStack(THItems.TEDDY_BEAR_TOY.get()))
                    .displayItems((params, output) -> {
                        var list = ToyHome.REGISTRY.itemRegister.getEntries().stream()
                                .map(RegistryObject::get)
                                .toList();
                        list.forEach(output::accept);
                    })
            )
            .build();

}
