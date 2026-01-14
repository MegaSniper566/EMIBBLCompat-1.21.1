package com.megasniper.emi.bblcompat.emi;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Method;
import java.util.*;

public class StrainerRecipeMerger {
    
    public static List<StrainerEmiRecipe> mergeRecipes(List<RecipeHolder<?>> recipes) {
        Map<String, MergedRecipeData> merged = new HashMap<>();
        
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
                
                // Expand ingredient tags into individual items
                for (var inputStack : input.getItems()) {
                    // For each individual item in the ingredient (expands tags)
                    Ingredient singleInput = Ingredient.of(inputStack);
                    
                    // Group results by mesh type for this specific input item
                    Map<String, List<Object>> outputsByMesh = new HashMap<>();
                    Map<String, Ingredient> meshByKey = new HashMap<>();
                    
                    for (Object meshChanceResult : rollResults) {
                        Method meshMethod = meshChanceResult.getClass().getMethod("mesh");
                        Method chanceResultMethod = meshChanceResult.getClass().getMethod("chanceResult");
                        
                        Ingredient mesh = (Ingredient) meshMethod.invoke(meshChanceResult);
                        Object chanceResult = chanceResultMethod.invoke(meshChanceResult);
                        
                        // Create key for this mesh
                        String meshKey = mesh.getItems().length > 0 ?
                            Arrays.stream(mesh.getItems())
                                .map(stack -> stack.getItem().toString())
                                .sorted()
                                .reduce((a, b) -> a + "," + b)
                                .orElse("empty") : "empty";
                        
                        outputsByMesh.computeIfAbsent(meshKey, k -> new ArrayList<>()).add(chanceResult);
                        meshByKey.putIfAbsent(meshKey, mesh);
                    }
                    
                    // Create one recipe per item+mesh+block combination
                    for (Map.Entry<String, List<Object>> meshEntry : outputsByMesh.entrySet()) {
                        String key = inputStack.getItem().toString() + "|" + 
                                    aboveBlock.toString() + "|" + 
                                    meshEntry.getKey();
                        
                        Ingredient meshIngredient = meshByKey.get(meshEntry.getKey());
                        
                        merged.compute(key, (k, existing) -> {
                            if (existing == null) {
                                return new MergedRecipeData(
                                    holder.id(), aboveBlock, singleInput, meshIngredient,
                                    new ArrayList<>(meshEntry.getValue())
                                );
                            } else {
                                // Merge outputs if key already exists
                                existing.outputs.addAll(meshEntry.getValue());
                                return existing;
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Convert merged data to recipe displays
        List<StrainerEmiRecipe> result = new ArrayList<>();
        for (MergedRecipeData data : merged.values()) {
            result.add(new StrainerEmiRecipe(
                data.id,
                data.aboveBlock,
                data.input,
                data.mesh,
                data.outputs
            ));
        }
        
        return result;
    }
    
    private static class MergedRecipeData {
        final ResourceLocation id;
        final BlockState aboveBlock;
        final Ingredient input;
        final Ingredient mesh;
        final List<Object> outputs;
        
        MergedRecipeData(ResourceLocation id, BlockState aboveBlock, Ingredient input, Ingredient mesh, List<Object> outputs) {
            this.id = id;
            this.aboveBlock = aboveBlock;
            this.input = input;
            this.mesh = mesh;
            this.outputs = outputs;
        }
    }
}
