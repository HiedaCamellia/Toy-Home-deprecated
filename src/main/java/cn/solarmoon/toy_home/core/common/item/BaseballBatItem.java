package cn.solarmoon.toy_home.core.common.item;

import cn.solarmoon.solarmoon_core.api.util.CommonParticleSpawner;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import cn.solarmoon.solarmoon_core.api.util.phys.OrientedBox;
import cn.solarmoon.toy_home.core.common.config.CommonConfig;
import cn.solarmoon.toy_home.core.common.entity.SpongeBobEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BaseballBatItem extends Item {

    public BaseballBatItem() {
        super(new Properties().stacksTo(1).durability(512));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return super.use(level, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int tickRemain) {
        float power = Math.min((stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 60f, 1);
        OrientedBox box = new OrientedBox(entity, 1, 3, 1, Mth.lerp(power, 0, 3)).updateVertex();
        List<Entity> entities = new ArrayList<>();
        if (level instanceof ServerLevel sl) {
            sl.getEntities().getAll().forEach(check -> {
                if (box.intersects(check.getBoundingBox()) && !entity.is(check)) {
                    entities.add(check);
                }
            });
            if (!entities.isEmpty()) {
                level.playSound(null, entity.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS);
                if (power == 1) { // 全垒打（蓄满力）
                    level.playSound(null, entity.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 5, 1);
                }
                CommonParticleSpawner.sweep(entity, level);
                Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(entity, 1.0);
                level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y - 0.35, spawnPos.z, 0.0, 0.0, 0.0);
                for (var en : entities) {
                    Vec3 v = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot()).scale(Mth.lerp(power, 1, 3));
                    if (en instanceof Projectile projectile) {
                        projectile.setOwner(entity);
                        projectile.setXRot(entity.getXRot());
                        projectile.setYRot(entity.getYRot());
                    }
                    if (en instanceof AbstractHurtingProjectile hurtingProjectile) {
                        hurtingProjectile.xPower = 0;
                        hurtingProjectile.yPower = 0;
                        hurtingProjectile.zPower = 0;
                    } // 重置可打击弹射物的初始速度防止回弹
                    en.setDeltaMovement(v);
                }
            }
        }
        entity.swing(entity.getUsedItemHand());
        if (CommonConfig.deBug.get()) {
            level.addParticle(ParticleTypes.END_ROD, box.vertex1.x, box.vertex1.y, box.vertex1.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex2.x, box.vertex2.y, box.vertex2.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex3.x, box.vertex3.y, box.vertex3.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex4.x, box.vertex4.y, box.vertex4.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex5.x, box.vertex5.y, box.vertex5.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex6.x, box.vertex6.y, box.vertex6.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex7.x, box.vertex7.y, box.vertex7.z, 0, 0.01, 0);
            level.addParticle(ParticleTypes.END_ROD, box.vertex8.x, box.vertex8.y, box.vertex8.z, 0, 0.01, 0);
        }
        super.releaseUsing(stack, level, entity, tickRemain);
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player player) {
        return !player.isCreative();
    }

}
