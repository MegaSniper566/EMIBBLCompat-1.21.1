package com.benbenlaw.strainers.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeItem extends Item {

    private final int speedReduction;
    private final double outputIncrease;

    public UpgradeItem(Properties properties, int speedReduction) {
        super(properties);
        this.outputIncrease = 0;
        this.speedReduction = speedReduction;
    }

    public UpgradeItem(Properties properties, double outputIncrease) {
        super(properties);
        this.outputIncrease = outputIncrease;
        this.speedReduction = 0;
    }

    public int getSpeedReduction() {
        return speedReduction;
    }

    public double getOutputIncrease() {
        return outputIncrease;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            if (speedReduction == 0) {
                String outputIncrease = String.format("%.0f%%", this.outputIncrease * 100);
                components.add(Component.translatable("tooltip.strainers.output_increase", outputIncrease).withStyle(ChatFormatting.BLUE));
            } else {
                components.add(Component.translatable("tooltip.strainers.duration_decrease", speedReduction).withStyle(ChatFormatting.YELLOW));
            }

        } else {
            components.add(Component.translatable("tooltips.bblcore.shift").withStyle(ChatFormatting.YELLOW));
        }
    }
}