package cn.solarmoon.toy_home.core.data.loot_table;

import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseBushCropBlock;
import cn.solarmoon.solarmoon_core.api.common.block.crop.INoLimitAgeBlock;
import cn.solarmoon.toy_home.core.common.registry.THBlocks;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.Set;

public class THBlockLoots extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet<>();

    public THBlockLoots() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(THBlocks.BLAVINGAD_TOY.get());
        dropSelf(THBlocks.PANDA_TOY.get());
        dropSelf(THBlocks.TEDDY_BEAR_TOY.get());
        dropSelf(THBlocks.POLAR_BEAR_TOY.get());
    }

    private void cropDrop(Block block, Item product, Item seed) {
        add(block, createCropDrops(block, product, seed, cropConditionBuilder(block)));
    }

    private void garlicDrop(Block block, Item product, Item product2, Item seed) {
        add(block, createGarlicDrops(block, product, product2, seed, cropConditionBuilder(block)));
    }

    private void bushDrop(Block block, Item product, Item seed) {
        add(block, createBushDrops(block, product, seed, bushConditionBuilder(block)));
    }

    /**
     * 大蒜专用掉落，未成熟只掉落蒜瓣，成熟掉落大蒜和蒜苗，不吃幸运
     */
    protected LootTable.Builder createGarlicDrops(Block block, Item product, Item product2, Item seed, LootItemCondition.Builder condition) {
        return this.applyExplosionDecay(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product)
                                                .when(condition)
                                                .otherwise(LootItem.lootTableItem(seed)))
                        )
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product2)
                                                .when(condition)
                                )
                        )
        );
    }

    /**
     * 类浆果丛掉落，未成熟只掉作物种子，成熟后变为掉一个果实（默认都掉1个，此类一般右键摘取，右键情况下才有可能摘取更多）
     */
    protected LootTable.Builder createBushDrops(Block block, Item product, Item seed, LootItemCondition.Builder condition) {
        return this.applyExplosionDecay(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product)
                                                .when(condition)
                                                .otherwise(LootItem.lootTableItem(seed)))
                        )
        );
    }

    private LootItemCondition.Builder bushConditionBuilder(Block block) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(INoLimitAgeBlock.AGE, ((BaseBushCropBlock)block).getMaxAge())
                );
    }

    private LootItemCondition.Builder cropConditionBuilder(Block block) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(CropBlock.AGE, ((CropBlock)block).getMaxAge())
                );
    }

    private void dropBedPart(Block block) {
        this.add(block, (builder) -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("pool1")
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(IBedPartBlock.PART, BedPart.HEAD)))));
    }

    private void dropWildCrop(Block wildCrop, ItemLike seed, ItemLike product) {
        this.add(wildCrop, (builder) -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("pool1")
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(product)
                                .when(LootItemRandomChanceCondition.randomChance(0.2F))
                                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)))))
                )
                .withPool(LootPool.lootPool()
                        .name("pool2")
                        .setRolls(ConstantValue.exactly(1))
                        .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(wildCrop)
                                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))),
                                        LootItem.lootTableItem(seed)
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))
                                                .apply(ApplyExplosionDecay.explosionDecay())
                                )
                        )
                )
        );
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }

}
