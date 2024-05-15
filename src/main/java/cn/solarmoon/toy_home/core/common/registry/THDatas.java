package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.BaseDataPackRegistry;
import cn.solarmoon.toy_home.core.data.builder.FortuneQuoteBuilder;

public class THDatas extends BaseDataPackRegistry {
    @Override
    public void addRegistry() {
        add(new FortuneQuoteBuilder());
    }
}
