package com.shadow_ninja.narutoreborn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.shadow_ninja.narutoreborn.entity.katon.KatonFireballEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;

/**
 * Renders the custom fireball as a scaled fire charge item billboard.
 */
public class KatonFireballRenderer extends EntityRenderer<KatonFireballEntity> {

    public KatonFireballRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(KatonFireballEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float scale = (float) (0.6f + entity.getRadius() * 0.1f);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        var stack = Items.FIRE_CHARGE.getDefaultInstance();
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(KatonFireballEntity entity) {
        // Use vanilla fireball texture (required but not actually used by item render).
        return ResourceLocation.withDefaultNamespace("textures/entity/projectiles/fireball.png");
    }
}
