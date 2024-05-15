package cn.solarmoon.toy_home.core.common.item;

import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.toy_home.core.common.entity.SpongeBobEntity;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ToyGunItem extends Item implements IContainerItem {

    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;

    public ToyGunItem() {
        super(new Properties().stacksTo(1).durability(512));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 28;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (isCharged(stack)) {
            ContainerUtil.extractItem(stack, 1);
            SpongeBobEntity bobEntity = new SpongeBobEntity(player, level);
            bobEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3, 1);
            if (!level.isClientSide) {
                level.addFreshEntity(bobEntity);
            }
            level.playSound(null, player.blockPosition(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS);
            stack.hurtAndBreak(1, player, (playerIn) -> player.broadcastBreakEvent(playerIn.getUsedItemHand()));
            return InteractionResultHolder.consume(stack);
        }

        if (findBob(player).isPresent()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            player.startUsingItem(hand);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int tick) {
        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                findBob(player).ifPresent(bob -> {
                    float f = (float) (stack.getUseDuration() - tick) / (float) 25;
                    if (f < 0.2F) {
                        this.startSoundPlayed = false;
                        this.midLoadSoundPlayed = false;
                    }
                    if (f >= 0.2F && !this.startSoundPlayed) {
                        this.startSoundPlayed = true;
                        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 0.5F, 1.0F);
                    }
                    if (f >= 0.5F && !this.midLoadSoundPlayed) {
                        this.midLoadSoundPlayed = true;
                        ContainerUtil.insertItem(stack, bob, 1);
                        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 0.5F, 1.0F);
                    }
                });
            }
        }
        super.onUseTick(level, entity, stack, tick);
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainDuration) {
        entity.stopUsingItem();
        super.releaseUsing(stack, level, entity, remainDuration);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        ItemStack bob = ContainerUtil.getInventory(stack).getStackInSlot(0);
        String amountS = bob.isEmpty() ? "" : " x" + bob.getCount();
        components.add(bob.getHoverName().copy().append(amountS));
        super.appendHoverText(stack, level, components, flag);
    }

    public static Optional<ItemStack> findBob(Player player) {
        return player.getInventory().items.stream().filter(stack1 -> stack1.is(THItems.SPONGE_BOB.get())).findFirst();
    }

    public static boolean isCharged(ItemStack gun) {
        return ContainerUtil.getInventory(gun).getStackInSlot(0).is(THItems.SPONGE_BOB.get());
    }

}
