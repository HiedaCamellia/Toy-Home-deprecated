package cn.solarmoon.toy_home.core.common.config;

import cn.solarmoon.solarmoon_core.api.config.SolarConfigBuilder;
import cn.solarmoon.solarmoon_core.api.util.RegisterHelper;
import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.toy_home.core.ToyHome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class CommonConfig {

    public static final SolarConfigBuilder builder = ToyHome.REGISTRY.configBuilder(COMMON);

    public static final ForgeConfigSpec.ConfigValue<Boolean> deBug;

    static {
        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);
    }

    public static void register() {
        RegisterHelper.register(builder);
    }

}
