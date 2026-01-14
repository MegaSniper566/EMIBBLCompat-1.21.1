package com.megasniper.emi.bblcompat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class StrainerRecipeCategory extends EmiRecipeCategory {
    private static final EmiStack ICON = getStrainerIcon();

    public StrainerRecipeCategory() {
        super(EMIBBLPlugin.STRAINER_ID, ICON);
    }

    @Override
    public Component getName() {
        return Component.translatable("category.emibblcompat.strainer");
    }
    
    private static EmiStack getStrainerIcon() {
        Block woodenStrainer = BuiltInRegistries.BLOCK.get(
            ResourceLocation.fromNamespaceAndPath("strainers", "wooden_strainer")
        );
        if (woodenStrainer != null) {
            return EmiStack.of(woodenStrainer);
        }
        return EmiStack.of(Items.HOPPER); // Fallback
    }
}
