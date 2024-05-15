package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.DamageTypeEntry;
import cn.solarmoon.toy_home.core.ToyHome;

public class THDamageTypes {
    public static void register() {}

    public static final DamageTypeEntry SPONGE_BOB_ATTACK = ToyHome.REGISTRY.damageType()
            .id("sponge_bob_attack")
            .build();

}
