package cn.solarmoon.toy_home.core.client.entity_renderer;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.entity.SpongeBobEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SpongeBobEntityRenderer extends EntityRenderer<SpongeBobEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ToyHome.MOD_ID + ":textures/entity/projectiles/arrow.png");

    private final ItemRenderer itemRenderer;

    public SpongeBobEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(SpongeBobEntity p_113839_, float p_113840_, float p_113841_, PoseStack p_113842_, MultiBufferSource p_113843_, int p_113844_) {
        p_113842_.pushPose();
        p_113842_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_113841_, p_113839_.yRotO, p_113839_.getYRot()) - 90.0F));
        p_113842_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_113841_, p_113839_.xRotO, p_113839_.getXRot())));
        p_113842_.mulPose(Axis.XP.rotationDegrees(45.0F));
        p_113842_.scale(0.05625F, 0.05625F, 0.05625F);
        p_113842_.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = p_113843_.getBuffer(RenderType.entityCutout(this.getTextureLocation(p_113839_)));
        PoseStack.Pose posestack$pose = p_113842_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, p_113844_);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, p_113844_);

        for(int j = 0; j < 4; ++j) {
            p_113842_.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, p_113844_);
        }

        p_113842_.popPose();
        super.render(p_113839_, p_113840_, p_113841_, p_113842_, p_113843_, p_113844_);
    }

    public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, int p_254058_, int p_254338_, int p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_) {
        p_253902_.vertex(p_254392_, (float)p_254058_, (float)p_254338_, (float)p_254196_).color(255, 255, 255, 255).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float)p_253982_, (float)p_254038_, (float)p_254037_).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(SpongeBobEntity entity) {
        return TEXTURE;
    }

}
