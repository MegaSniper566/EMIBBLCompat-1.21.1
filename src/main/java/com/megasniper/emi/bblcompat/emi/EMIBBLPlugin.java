package com.megasniper.emi.bblcompat.emi;

import com.megasniper.emi.bblcompat.EMIBBLCompat;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class EMIBBLPlugin implements EmiPlugin {
    public static final ResourceLocation STRAINER_ID = ResourceLocation.fromNamespaceAndPath("strainers", "strainer");
    public static final EmiRecipeCategory STRAINER_CATEGORY = new StrainerRecipeCategory();

    @Override
    public void register(EmiRegistry registry) {
        EMIBBLCompat.LOGGER.info("Registering EMI BBL Plugin");
        
        // Check if strainers mod is loaded
        if (!isModLoaded("strainers")) {
            EMIBBLCompat.LOGGER.info("Strainers mod not loaded, skipping registration");
            return;
        }
        
        // Register the strainer recipe category
        registry.addCategory(STRAINER_CATEGORY);
        
        // Register workstations for the category (the strainer blocks)
        Block woodenStrainer = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("strainers", "wooden_strainer"));
        if (woodenStrainer != null) {
            registry.addWorkstation(STRAINER_CATEGORY, EmiStack.of(woodenStrainer));
        }
        
        // Load and register strainer recipes
        loadStrainerRecipes(registry);
        
        EMIBBLCompat.LOGGER.info("EMI BBL Plugin registered successfully");
    }
    
    private boolean isModLoaded(String modId) {
        return net.neoforged.fml.ModList.get().isLoaded(modId);
    }
    
    private void loadStrainerRecipes(EmiRegistry registry) {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return;
            
            // Get the recipe type for strainers
            ResourceLocation strainerTypeId = ResourceLocation.fromNamespaceAndPath("strainers", "strainer");
            RecipeType<?> strainerType = BuiltInRegistries.RECIPE_TYPE.get(strainerTypeId);
            
            if (strainerType == null) {
                EMIBBLCompat.LOGGER.warn("Could not find strainer recipe type");
                return;
            }
            
            // Get all strainer recipes and merge them
            @SuppressWarnings("unchecked")
            List<RecipeHolder<?>> recipes = new ArrayList<>(
                minecraft.level.getRecipeManager().getAllRecipesFor((RecipeType) strainerType)
            );
            List<StrainerEmiRecipe> displays = StrainerRecipeMerger.mergeRecipes(recipes);
            
            // Register merged recipes
            for (StrainerEmiRecipe recipe : displays) {
                registry.addRecipe(recipe);
            }
            
            EMIBBLCompat.LOGGER.info("Loaded {} strainer recipe displays", displays.size());
        } catch (Exception e) {
            EMIBBLCompat.LOGGER.error("Error loading strainer recipes", e);
        }
    }
}
