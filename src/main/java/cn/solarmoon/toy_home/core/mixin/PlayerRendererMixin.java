package cn.solarmoon.toy_home.core.mixin;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.item.ToyGunItem;
import cn.solarmoon.toy_home.core.data.tag.THItemTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(THItemTags.HUGGABLE)) {
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_CHARGE);
        }
        if (!player.swinging && heldItem.getItem() instanceof ToyGunItem && ToyGunItem.isCharged(heldItem)) {
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
        }
    }

}
