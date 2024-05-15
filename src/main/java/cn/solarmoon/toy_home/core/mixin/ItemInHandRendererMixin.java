package cn.solarmoon.toy_home.core.mixin;

import cn.solarmoon.toy_home.core.common.item.ToyGunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow protected abstract void applyItemArmTransform(PoseStack p_109383_, HumanoidArm p_109384_, float p_109385_);

    @Shadow protected abstract void applyItemArmAttackTransform(PoseStack p_109336_, HumanoidArm p_109337_, float p_109338_);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    public void renderArmWithItem(AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, CallbackInfo ci) {
        if (p_109377_.getItem() instanceof ToyGunItem) {
            boolean flag = p_109375_ == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? p_109372_.getMainArm() : p_109372_.getMainArm().getOpposite();
            boolean flag1 = ToyGunItem.isCharged(p_109377_);
            boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
            int i = flag2 ? 1 : -1;
            if (p_109372_.isUsingItem() && p_109372_.getUseItemRemainingTicks() > 0 && p_109372_.getUsedItemHand() == p_109375_) {
                this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                p_109379_.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                p_109379_.mulPose(Axis.XP.rotationDegrees(-11.935F));
                p_109379_.mulPose(Axis.YP.rotationDegrees((float) i * 65.3F));
                p_109379_.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
                float f9 = (float) p_109377_.getUseDuration() - ((float) minecraft.player.getUseItemRemainingTicks() - p_109373_ + 1.0F);
                float f13 = f9 / (float) 25;
                if (f13 > 1.0F) {
                    f13 = 1.0F;
                }

                if (f13 > 0.1F) {
                    float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                    float f3 = f13 - 0.1F;
                    float f4 = f16 * f3;
                    p_109379_.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                }

                p_109379_.translate(f13 * 0.0F, f13 * 0.0F, f13 * 0.04F);
                p_109379_.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
                p_109379_.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
            } else {
                float f = -0.4F * Mth.sin(Mth.sqrt(p_109376_) * (float) Math.PI);
                float f1 = 0.2F * Mth.sin(Mth.sqrt(p_109376_) * ((float) Math.PI * 2F));
                float f2 = -0.2F * Mth.sin(p_109376_ * (float) Math.PI);
                p_109379_.translate((float) i * f, f1, f2);
                this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                this.applyItemArmAttackTransform(p_109379_, humanoidarm, p_109376_);
                if (flag1 && p_109376_ < 0.001F && flag) {
                    p_109379_.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    p_109379_.mulPose(Axis.YP.rotationDegrees((float) i * 10.0F));
                }
            }
        }
    }

}
