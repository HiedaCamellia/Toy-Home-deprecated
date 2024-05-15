package cn.solarmoon.toy_home.core.client.registry.ability;

import cn.solarmoon.solarmoon_core.api.client.ability.ItemRenderProperties;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.item.PaperRollWhistleItem;
import cn.solarmoon.toy_home.core.common.item.SlipItem;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class THItemProperties {

    private static final ItemRenderProperties properties = new ItemRenderProperties(ToyHome.MOD_ID);

    private static void addRegistry() {
        properties.put(THItems.PAPER_ROLL_WHISTLE.get(), "whistling", (stack, level, entity, num) ->
                PaperRollWhistleItem.getRenderByWhistling(stack, entity));
        properties.put(THItems.SLIP.get(), "written", (stack, level, entity, num) ->
                SlipItem.getNIfWritten(stack));
    }

    private static void onFMLDefferSetup(FMLClientSetupEvent event) {
        event.enqueueWork(THItemProperties::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(THItemProperties::onFMLDefferSetup);
    }

}
