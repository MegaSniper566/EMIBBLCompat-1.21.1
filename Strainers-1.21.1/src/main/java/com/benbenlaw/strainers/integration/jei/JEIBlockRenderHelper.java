package com.benbenlaw.strainers.integration.jei;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class JEIBlockRenderHelper {

    public static void renderBlock(GuiGraphics guiGraphics, BlockState state, int x, int y, float scale) {
        PoseStack poseStack = guiGraphics.pose();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        Lighting.setupForFlatItems();
        poseStack.pushPose();

        poseStack.translate(x, y, 150); // Z = 150 to ensure it renders above GUI

        // Convert from 1-block (1 unit = 1m) to pixel scale, then scale down
        poseStack.scale(16.0f * scale, -16.0f * scale, 16.0f * scale);

        // Apply desired 3D rotation
        poseStack.mulPose(Axis.XP.rotationDegrees(30));
        poseStack.mulPose(Axis.YP.rotationDegrees(45));

        // Render the block
        blockRenderer.renderSingleBlock(
                state,
                poseStack,
                guiGraphics.bufferSource(),
                0xF000F0,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
        guiGraphics.bufferSource().endBatch();
        Lighting.setupFor3DItems(); // Restore default lighting

    }

}
