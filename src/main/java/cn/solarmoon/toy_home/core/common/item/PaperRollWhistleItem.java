package cn.solarmoon.toy_home.core.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PaperRollWhistleItem extends Item {

    public PaperRollWhistleItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 10;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getCooldowns().isOnCooldown(this)) {
            if (!player.isCreative()) player.getCooldowns().addCooldown(this, 60); // 不在创造模式下增加CD
            player.startUsingItem(hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int usingTime) {
        entity.stopUsingItem();
        super.releaseUsing(stack, level, entity, usingTime);
    }

    public static boolean isWhistling(ItemStack stack, LivingEntity entity) {
        return entity.getUseItem().equals(stack, true);
    }

    public static float getRenderByWhistling(ItemStack stack, LivingEntity entity) {
        return entity != null && isWhistling(stack, entity) ? 1 : 0;
    }

}
