package com.megasniper.emi.bblcompat.emi;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Method;
import java.util.*;

public class StrainerRecipeMerger {
    
    public static List<StrainerEmiRecipe> mergeRecipes(List<RecipeHolder<?>> recipes) {
        Map<String, StrainerEmiRecipe> merged = new HashMap<>();
        
        for (RecipeHolder<?> holder : recipes) {
            Object recipe = holder.value();
            
            try {
                // Use reflection to access recipe data
                Method getBlockAboveMethod = recipe.getClass().getMethod("getBlockAbove");
                Method inputMethod = recipe.getClass().getMethod("input");
                Method getRollResultsMethod = recipe.getClass().getMethod("getRollResults");
                
                BlockState aboveBlock = (BlockState) getBlockAboveMethod.invoke(recipe);
                Ingredient input = (Ingredient) inputMethod.invoke(recipe);
                @SuppressWarnings("unchecked")
                List<Object> rollResults = (List<Object>) getRollResultsMethod.invoke(recipe);
                
                for (Object meshChanceResult : rollResults) {
                    // Extract mesh and chanceResult from MeshChanceResult
                    Method meshMethod = meshChanceResult.getClass().getMethod("mesh");
                    Method chanceResultMethod = meshChanceResult.getClass().getMethod("chanceResult");
                    
                    Ingredient mesh = (Ingredient) meshMethod.invoke(meshChanceResult);
                    Object chanceResult = chanceResultMethod.invoke(meshChanceResult);
                    
                    // Create a stable key: input + blockAbove + mesh
                    String key = input.toString() + "|" + 
                                aboveBlock.toString() + "|" + 
                                mesh.toString();
                    
                    merged.compute(key, (k, existing) -> {
                        if (existing == null) {
                            return new StrainerEmiRecipe(
                                holder.id(),
                                aboveBlock,
                                input,
                                mesh,
                                List.of(chanceResult)
                            );
                        } else {
                            List<Object> combined = new ArrayList<>(existing.getChanceResults());
                            combined.add(chanceResult);
                            return new StrainerEmiRecipe(
                                existing.getId(),
                                existing.getAboveBlock(),
                                existing.getInput(),
                                existing.getMesh(),
                                combined
                            );
                        }
                    });
                }
            } catch (Exception e) {
                // Log and continue if reflection fails
                e.printStackTrace();
            }
        }
        
        return new ArrayList<>(merged.values());
    }
}
