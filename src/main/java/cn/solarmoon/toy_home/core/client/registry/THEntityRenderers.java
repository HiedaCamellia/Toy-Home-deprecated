package cn.solarmoon.toy_home.core.client.registry;

import cn.solarmoon.toy_home.core.client.entity_renderer.SpongeBobEntityRenderer;
import cn.solarmoon.toy_home.core.client.entity_renderer.StickyHandEntityRenderer;
import cn.solarmoon.toy_home.core.common.registry.THEntities;

public class THEntityRenderers {

    public static void register() {
        THEntities.STICKY_HAND.renderer(StickyHandEntityRenderer::new);
        THEntities.SPONGE_BOB.renderer(SpongeBobEntityRenderer::new);
    }

}
