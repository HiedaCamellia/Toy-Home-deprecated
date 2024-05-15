package cn.solarmoon.toy_home.core.common.item;

import cn.solarmoon.toy_home.core.common.entity.StickyHandEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StickyHandItem extends Item {

    public StickyHandItem() {
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
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainDuration) {
        StickyHandEntity oldHdCheck = StickyHandEntity.ENTITY_BOUND.get(entity);
        if (oldHdCheck != null) oldHdCheck.discard(); // 保证每个实体只留存一个钩子
        StickyHandEntity hd = new StickyHandEntity(entity, level);
        int power = this.getUseDuration(stack) - remainDuration;
        hd.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, getPowerForTime(power) * 3, 1);
        if (!level.isClientSide) {
            level.addFreshEntity(hd);
        }
        stack.hurtAndBreak(1, entity, (playerIn) -> entity.broadcastBreakEvent(playerIn.getUsedItemHand()));
        super.releaseUsing(stack, level, entity, remainDuration);
    }

    public static float getPowerForTime(int p) {
        float f = (float)p / 20.0F;
        f = (f * f + f + f * 2.0F) / 4.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

}
