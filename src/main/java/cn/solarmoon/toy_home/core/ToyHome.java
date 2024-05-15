package cn.solarmoon.toy_home.core;

import cn.solarmoon.solarmoon_core.api.ObjectRegistry;
import cn.solarmoon.solarmoon_core.api.SolarMoonBase;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Debug;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Translator;
import cn.solarmoon.toy_home.core.client.registry.THEntityRenderers;
import cn.solarmoon.toy_home.core.client.registry.ability.THItemProperties;
import cn.solarmoon.toy_home.core.common.config.CommonConfig;
import cn.solarmoon.toy_home.core.common.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ToyHome.MOD_ID)
public class ToyHome extends SolarMoonBase {

    public static final String MOD_ID = "toy_home";
    public static final ObjectRegistry REGISTRY = ObjectRegistry.create(MOD_ID);
    public static final Translator TRANSLATOR = Translator.create(MOD_ID);
    public static final Debug DEBUG = Debug.create("[玩具之家]", CommonConfig.deBug);

    @Override
    public void objectsClientOnly() {
        THEntityRenderers.register();
    }

    @Override
    public void objects() {
        THItems.register();
        THBlocks.register();
        THEntities.register();
        THDamageTypes.register();
        THNetPacks.register();
        THCreativeModeTab.register();
    }

    @Override
    public void eventObjectsClientOnly() {

    }

    @Override
    public void eventObjects() {
        new THCommonEvents().register();
        new THDatas().register();
    }

    @Override
    public void xData() {
        CommonConfig.register();
    }

    @Override
    public void abilitiesClientOnly() {
        THItemProperties.register();
    }

    @Override
    public void abilities() {

    }

    @Override
    public void compats() {

    }

}
