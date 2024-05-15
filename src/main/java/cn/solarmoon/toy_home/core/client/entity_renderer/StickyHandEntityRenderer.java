package cn.solarmoon.toy_home.core.client.entity_renderer;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.entity.StickyHandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

/**
 * Credit: <a href="https://github.com/AlexModGuy/AlexsMobs/tree/1.20">...</a>
 */
public class StickyHandEntityRenderer extends EntityRenderer<StickyHandEntity> {

    private static final float TENTACLES_COLOR_R = 12F / 255F;
    private static final float TENTACLES_COLOR_G = 92F / 255F;
    private static final float TENTACLES_COLOR_B = 68F / 255F;
    private static final float TENTACLES_COLOR_R2 = 36F / 255F;
    private static final float TENTACLES_COLOR_G2 = 148F / 255F;
    private static final float TENTACLES_COLOR_B2 = 116F / 255F;

    private static final ResourceLocation TEXTURE = new ResourceLocation(ToyHome.MOD_ID + ":textures/item/sticky_hand.png");

    private final ItemRenderer itemRenderer;

    public StickyHandEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(StickyHandEntity handEntity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(handEntity.getXRot()));
        poseStack.mulPose(Axis.YN.rotationDegrees(handEntity.getYRot()));
        light = LevelRenderer.getLightColor(handEntity.level(), handEntity.blockPosition()); //让光度和环境一致
        itemRenderer.renderStatic(handEntity.getItem(), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, handEntity.level(), 0);
        poseStack.popPose();

        if (handEntity.getOwner() instanceof LivingEntity holder) {
            final double d0 = Mth.lerp(partialTicks, handEntity.xOld, handEntity.getX());
            final double d1 = Mth.lerp(partialTicks, handEntity.yOld, handEntity.getY());
            final double d2 = Mth.lerp(partialTicks, handEntity.zOld, handEntity.getZ());
            poseStack.pushPose();
            poseStack.translate(-d0, -d1, -d2);
            renderTentacle(handEntity, partialTicks, poseStack, buffer, holder, holder.getMainArm() != HumanoidArm.LEFT, -0.1F);
            poseStack.popPose();
        }

        super.render(handEntity, pEntityYaw, partialTicks, poseStack, buffer, light);
    }

    public static void renderTentacle(Entity mob, float partialTick, PoseStack p_115464_, MultiBufferSource p_115465_, LivingEntity player, boolean left, float zOffset) {
        p_115464_.pushPose();
        float bodyRot = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRot : mob.getYRot();
        float bodyRot0 = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRotO : mob.yRotO;
        Vec3 vec3 = player.getRopeHoldPosition(partialTick);
        double d0 = (double) (Mth.lerp(partialTick, bodyRot, bodyRot0) * Mth.DEG_TO_RAD) + (Math.PI / 2D);
        Vec3 vec31 = new Vec3(0, 0F, 0);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(partialTick, mob.xo, mob.getX()) + d1;
        double d4 = Mth.lerp(partialTick, mob.yo, mob.getY()) + vec31.y;
        double d5 = Mth.lerp(partialTick, mob.zo, mob.getZ()) + d2;
        p_115464_.translate(d3, d4, d5);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        VertexConsumer vertexconsumer = p_115465_.getBuffer(RenderType.leash());
        Matrix4f matrix4f = p_115464_.last().pose();
        float f4 = (float) (Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F);
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = fromVec3(mob.getEyePosition(partialTick));
        BlockPos blockpos1 = fromVec3(player.getEyePosition(partialTick));
        int i = getTentacleLightLevel(mob, blockpos);
        int j = mob.level().getBrightness(LightLayer.BLOCK, blockpos1);
        int k = mob.level().getBrightness(LightLayer.SKY, blockpos);
        int l = mob.level().getBrightness(LightLayer.SKY, blockpos1);
        float width = 0.2F;
        for (int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, j1, true);
        }
        p_115464_.popPose();
    }

    public static BlockPos fromCoords(double x, double y, double z){
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public static BlockPos fromVec3(Vec3 vec3){
        return fromCoords(vec3.x, vec3.y, vec3.z);
    }

    protected static int getTentacleLightLevel(Entity p_114496_, BlockPos p_114497_) {
        return p_114496_.isOnFire() ? 15 : p_114496_.level().getBrightness(LightLayer.BLOCK, p_114497_);
    }

    private static void addVertexPair(VertexConsumer p_174308_, Matrix4f p_174309_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float) p_174321_ / 24.0F;
        int i = (int) Mth.lerp(f, (float) p_174313_, (float) p_174314_);
        int j = (int) Mth.lerp(f, (float) p_174315_, (float) p_174316_);
        int k = LightTexture.pack(i, j);
        float f2 = TENTACLES_COLOR_R;
        float f3 = TENTACLES_COLOR_G;
        float f4 = TENTACLES_COLOR_B;
        if (p_174321_ % 2 == (p_174322_ ? 1 : 0)) {
            f2 = TENTACLES_COLOR_R2;
            f3 = TENTACLES_COLOR_G2;
            f4 = TENTACLES_COLOR_B2;
        }
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        p_174308_.vertex(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(StickyHandEntity entity) {
        return TEXTURE;
    }

}
