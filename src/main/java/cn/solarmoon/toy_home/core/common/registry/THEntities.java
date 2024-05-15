package cn.solarmoon.toy_home.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.EntityEntry;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.entity.SpongeBobEntity;
import cn.solarmoon.toy_home.core.common.entity.StickyHandEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class THEntities {
    public static void register() {}

    public static final EntityEntry<StickyHandEntity> STICKY_HAND = ToyHome.REGISTRY.entity()
            .id("sticky_hand")
            .builder(EntityType.Builder
                    .<StickyHandEntity>of(StickyHandEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .setCustomClientFactory((StickyHandEntity::new))
                    .fireImmune())
            .build();

    public static final EntityEntry<SpongeBobEntity> SPONGE_BOB = ToyHome.REGISTRY.entity()
            .id("sponge_bob")
            .builder(EntityType.Builder
                    .<SpongeBobEntity>of(SpongeBobEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setCustomClientFactory((SpongeBobEntity::new))
                    .fireImmune())
            .build();

}
