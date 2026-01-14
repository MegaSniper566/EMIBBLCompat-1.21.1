package com.megasniper.emi.bblcompat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StrainerEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final BlockState aboveBlock;
    private final Ingredient input;
    private final Ingredient mesh;
    private final List<Object> chanceResults; // ChanceResult objects
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public StrainerEmiRecipe(ResourceLocation id, BlockState aboveBlock, Ingredient input,
                             Ingredient mesh, List<Object> chanceResults) {
        this.id = id;
        this.aboveBlock = aboveBlock;
        this.input = input;
        this.mesh = mesh;
        this.chanceResults = chanceResults;
        
        // Build inputs list
        this.inputs = new ArrayList<>();
        this.inputs.add(EmiIngredient.of(input));
        
        // Build outputs list from ChanceResult objects
        this.outputs = new ArrayList<>();
        for (Object chanceResult : chanceResults) {
            try {
                Method stackMethod = chanceResult.getClass().getMethod("stack");
                ItemStack stack = (ItemStack) stackMethod.invoke(chanceResult);
                if (!stack.isEmpty()) {
                    outputs.add(EmiStack.of(stack));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BlockState getAboveBlock() {
        return aboveBlock;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getMesh() {
        return mesh;
    }

    public List<Object> getChanceResults() {
        return chanceResults;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMIBBLPlugin.STRAINER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 155;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // Layout based on JEI reference:
        // Top left: Above block/fluid (2, 2)
        // Middle left: Input slot (2, 20)
        // Bottom left: Mesh slot (2, 38)
        // Right side: Scrollable outputs grid (starting at 50, 1)
        
        // Above block/fluid slot
        Fluid fluidState = aboveBlock.getFluidState().getType();
        if (fluidState != Fluids.EMPTY) {
            // Render fluid
            widgets.addSlot(EmiStack.of(fluidState, 1000), 2, 2).drawBack(false);
        } else {
            // Render block
            widgets.addSlot(EmiStack.of(aboveBlock.getBlock()), 2, 2);
        }
        
        // Input slot
        widgets.addSlot(EmiIngredient.of(input), 2, 20);
        
        // Mesh slot (catalyst)
        widgets.addSlot(EmiIngredient.of(mesh), 2, 38).catalyst(true);
        
        // Output slots in a scrollable grid (max 5 columns, 3 rows visible)
        int outputX = 50;
        int outputY = 1;
        int col = 0;
        int row = 0;
        
        for (int i = 0; i < outputs.size(); i++) {
            EmiStack output = outputs.get(i);
            Object chanceResult = chanceResults.get(i);
            
            int x = outputX + (col * 18);
            int y = outputY + (row * 18);
            
            widgets.addSlot(output, x, y)
                .recipeContext(this)
                .appendTooltip(getChanceTooltip(chanceResult));
            
            col++;
            if (col >= 5) {
                col = 0;
                row++;
            }
        }
    }
    
    private Component getChanceTooltip(Object chanceResult) {
        try {
            Method chanceMethod = chanceResult.getClass().getMethod("chance");
            float chance = (Float) chanceMethod.invoke(chanceResult);
            int asPercent = Math.round(chance * 100);
            
            if (asPercent < 100) {
                return Component.translatable("emi.chance", asPercent + "%")
                    .withStyle(ChatFormatting.GOLD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Component.empty();
    }
}
