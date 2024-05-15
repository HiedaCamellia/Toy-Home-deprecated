package cn.solarmoon.toy_home.core.common.item;

import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleFoodItem;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import cn.solarmoon.toy_home.core.data.serializer.FortuneQuote;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FortuneCookieItem extends SimpleFoodItem implements IContainerItem {

    public FortuneCookieItem() {
        super(2, 0.4f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack offStack = player.getOffhandItem();
            if (offStack.is(THItems.SLIP.get()) && SlipItem.isWritten(offStack) && !hasCustomSlip(stack)) {
                ContainerUtil.insertItem(stack, offStack, 1);
                return InteractionResultHolder.consume(stack);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (hasCustomSlip(stack)) {
                player.displayClientMessage(Component.literal(getContentIn(stack)), true);
            } else {
                int size = FortuneQuote.get().length;
                if (size > 0 && level.isClientSide) {
                    player.displayClientMessage(Component.translatable(FortuneQuote.get()[entity.getRandom().nextInt(size)]), true);
                }
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    public static boolean hasCustomSlip(ItemStack cookie) {
        ItemStack slip = ContainerUtil.getInventory(cookie).getStackInSlot(0);
        return slip.is(THItems.SLIP.get()) && SlipItem.isWritten(slip);
    }

    public static String getContentIn(ItemStack cookie) {
        ItemStack slip = ContainerUtil.getInventory(cookie).getStackInSlot(0);
        return SlipItem.getContent(slip);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (hasCustomSlip(stack)) {
            components.add(ToyHome.TRANSLATOR.set("message", "custom_cookie"));
        } else {
            components.add(ToyHome.TRANSLATOR.set("message", "random_cookie"));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
