package com.benbenlaw.strainers.block.entity.client;

import com.benbenlaw.strainers.block.entity.WoodenStrainerBlockEntity;
import com.benbenlaw.strainers.config.StrainersConfigFile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class StrainerMeshEntityRenderer implements BlockEntityRenderer<WoodenStrainerBlockEntity> {

    public StrainerMeshEntityRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(WoodenStrainerBlockEntity entity, float ticks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack mesh = entity.getItemStackHandler().getStackInSlot(WoodenStrainerBlockEntity.MESH_SLOT);
        ItemStack insideItem = entity.getItemStackHandler().getStackInSlot(WoodenStrainerBlockEntity.INPUT_SLOT); // Get insideItem ItemStack
        int progress = entity.progress;
        int maxProgress = entity.maxProgress;

        if (StrainersConfigFile.SPEC.isLoaded()) {

            if (!mesh.isEmpty() && StrainersConfigFile.showMeshesInWorld.get()) {
                poseStack.pushPose();

                // Render the mesh item on all four sides of the block
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP || direction == Direction.DOWN) {
                        continue; // Skip rendering on top and bottom sides
                    }

                    poseStack.pushPose();
                    poseStack.translate(0f, 1.23f, 0f); // Adjust y position for the top side
                    poseStack.scale(1.9f, 1.9f, 1.9f);

                    // Apply transformations for rendering on the current side
                    switch (direction) {
                        case NORTH:
                            poseStack.translate(0.26, 0.0, 0.02f); // Adjust x position for the north side
                            break;
                        case SOUTH:
                            poseStack.translate(0.26, 0.0, 0.5f); // Adjust x position for the south side
                            break;
                        case WEST:
                            poseStack.translate(0.5, 0.0, 0.26);
                            poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));// Adjust z position for the west side
                            break;
                        case EAST:
                            poseStack.translate(0.02f, 0.0, 0.26); // Adjust z position for the east side
                            poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));// Adjust z position for the west side
                            break;
                    }

                    // Render the mesh item
                    BakedModel model = itemRenderer.getModel(mesh, null, null, 0);
                    itemRenderer.render(mesh, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, getLightLevel(entity.getLevel(), entity.getBlockPos().above()), overlay, model);

                    poseStack.popPose(); // Pop pose after rendering mesh item
                }


                // Render the insideItem in the middle of the block
                poseStack.popPose(); // Pop pose after rendering the mesh item
            }


            // System.out.println("totalCompletedValue: " + adjustedFloat);


            if (StrainersConfigFile.showItemBeingStrainerInWorld.get()) {

                if (insideItem.getItem() instanceof BlockItem) {

                    double percentageValue = (double) 100 / maxProgress;
                    double totalCompletedValue = percentageValue * progress;
                    float adjustedFloat = (float) (totalCompletedValue / 100);

                    poseStack.pushPose();
                    poseStack.translate(0.5, 1 - adjustedFloat - 0.1, 0.5); // Adjust position to the middle of the block
                    poseStack.scale(3.3f, 3.3f, 3.3f);

                    BakedModel insideModel = itemRenderer.getModel(insideItem, null, null, 0);
                    itemRenderer.render(insideItem, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, getLightLevel(entity.getLevel(), entity.getBlockPos().above()), overlay, insideModel);

                    poseStack.popPose(); // Pop pose after rendering insideItem
                }

                else {
                    poseStack.pushPose();

                    // Render the mesh item on all four sides of the block
                    for (Direction direction : Direction.values()) {
                        if (direction == Direction.UP || direction == Direction.DOWN) {
                            continue; // Skip rendering on top and bottom sides
                        }

                        poseStack.pushPose();
                        poseStack.translate(0f, 0.25f, 0f); // Adjust y position for the top side
                        poseStack.scale(1.9f, 1.9f, 1.9f);

                        // Apply transformations for rendering on the current side
                        switch (direction) {
                            case NORTH:
                                poseStack.translate(0.26, 0.0, 0.02f); // Adjust x position for the north side
                                break;
                            case SOUTH:
                                poseStack.translate(0.26, 0.0, 0.5f); // Adjust x position for the south side
                                break;
                            case WEST:
                                poseStack.translate(0.5, 0.0, 0.26);
                                poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));// Adjust z position for the west side
                                break;
                            case EAST:
                                poseStack.translate(0.02f, 0.0, 0.26); // Adjust z position for the east side
                                poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));// Adjust z position for the west side
                                break;
                        }

                        // Render the mesh item
                        BakedModel insideModel = itemRenderer.getModel(insideItem, null, null, 0);
                        itemRenderer.render(insideItem, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, getLightLevel(entity.getLevel(), entity.getBlockPos().above()), overlay, insideModel);

                        poseStack.popPose(); // Pop pose after rendering mesh item
                    }


                    // Render the insideItem in the middle of the block
                    poseStack.popPose(); // Pop pose after rendering the mesh item

                }
            }
        }
    }




    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}
