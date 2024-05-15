package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.NetPackEntry;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.network.ClientPackHandler;
import cn.solarmoon.toy_home.core.network.ServerPackHandler;

public class THNetPacks {
    public static void register() {}

    public static final NetPackEntry SERVER = ToyHome.REGISTRY.netPack()
            .id("server")
            .side(NetPackEntry.Side.SERVER)
            .addHandler(new ServerPackHandler())
            .build();

    public static final NetPackEntry CLIENT = ToyHome.REGISTRY.netPack()
            .id("client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new ClientPackHandler())
            .build();

}
