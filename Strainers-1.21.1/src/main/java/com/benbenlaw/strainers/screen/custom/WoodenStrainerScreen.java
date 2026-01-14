package com.benbenlaw.strainers.screen.custom;

import com.benbenlaw.core.screen.util.TooltipArea;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.strainers.Strainers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WoodenStrainerScreen extends AbstractContainerScreen<WoodenStrainerMenu> {

    Level level;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "textures/gui/wooden_strainer_gui.png");

    public WoodenStrainerScreen(WoodenStrainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.level = pMenu.level;
        this.imageHeight = imageWidth + 39;
        this.inventoryLabelY = inventoryLabelY + 39;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);



        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        if (menu.isCrafting()) {
            int l = this.menu.getScaledProgress();
            guiGraphics.blit(TEXTURE, x + 31, y + 25, 176, 0, menu.getScaledProgress() + 1, 16);
        }


    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderSlotTooltips(guiGraphics, mouseX, mouseY, x, y);

    }

    private void renderSlotTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        List<TooltipArea> tooltipAreas = new ArrayList<>();

        tooltipAreas.add(new TooltipArea(8, 17, 16, 16, "block.strainers.gui.input_slot"));
        tooltipAreas.add(new TooltipArea(8, 35, 16, 16, "block.strainers.gui.mesh_slot"));
        tooltipAreas.add(new TooltipArea(8, 53, 16, 16, "block.strainers.gui.upgrade_slot"));
        tooltipAreas.add(new TooltipArea(8, 71, 16, 16, "block.strainers.gui.upgrade_slot"));
        tooltipAreas.add(new TooltipArea(8, 89, 16, 16, "block.strainers.gui.upgrade_slot"));


        for (TooltipArea area : tooltipAreas) {
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, area.offsetX, area.offsetY, area.width, area.height)) {
                if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                    guiGraphics.renderTooltip(this.font, Component.translatable(area.translationKey), mouseX, mouseY);
                }
            }
        }
    }
}
