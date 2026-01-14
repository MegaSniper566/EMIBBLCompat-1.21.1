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
                
                // Get first mesh from results for this recipe
                if (!rollResults.isEmpty()) {
                    Object firstMeshResult = rollResults.get(0);
                    Method meshMethod = firstMeshResult.getClass().getMethod("mesh");
                    Ingredient mesh = (Ingredient) meshMethod.invoke(firstMeshResult);
                    
                    // Collect all chance results from this recipe
                    List<Object> chanceResults = new ArrayList<>();
                    for (Object meshChanceResult : rollResults) {
                        Method chanceResultMethod = meshChanceResult.getClass().getMethod("chanceResult");
                        Object chanceResult = chanceResultMethod.invoke(meshChanceResult);
                        chanceResults.add(chanceResult);
                    }
                    
                    // Create unique key per recipe (don't merge different recipes)
                    String key = holder.id().toString();
                    
                    merged.put(key, new StrainerEmiRecipe(
                        holder.id(),
                        aboveBlock,
                        input,
                        mesh,
                        chanceResults
                    ));
                }
            } catch (Exception e) {
                // Log and continue if reflection fails
                e.printStackTrace();
            }
        }
        
        return new ArrayList<>(merged.values());
    }
}
