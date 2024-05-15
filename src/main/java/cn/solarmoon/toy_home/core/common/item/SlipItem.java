package cn.solarmoon.toy_home.core.common.item;

import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleItem;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.client.gui.SlipEditScreen;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlipItem extends Item {

    public static final String CONTENT = "Content";

    public SlipItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack slip = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && level.isClientSide && !isWritten(slip)) {
            SlipEditScreen screen = new SlipEditScreen(slip);
            Minecraft.getInstance().setScreen(screen);
        }
        return super.use(level, player, hand);
    }

    public static boolean isWritten(ItemStack slip) {
        return !slip.getOrCreateTag().getString(CONTENT).isEmpty() && slip.is(THItems.SLIP.get());
    }

    public static String getContent(ItemStack slip) {
        if (isWritten(slip)) {
            return slip.getOrCreateTag().getString(CONTENT);
        }
        else return "";
    }

    public static float getNIfWritten(ItemStack slip) {
        return isWritten(slip) ? 1 : 0;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (isWritten(stack)) {
            return ToyHome.TRANSLATOR.set("item", "slip_written");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (isWritten(stack)) {
            components.add(Component.literal(getContent(stack)));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
