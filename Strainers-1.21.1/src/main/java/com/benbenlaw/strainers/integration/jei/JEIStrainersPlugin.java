package com.benbenlaw.strainers.integration.jei;

import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIStrainersPlugin implements IModPlugin {
    public static IDrawableStatic slotDrawable;

    public static RecipeType<StrainerRecipeDisplay> STRAINER =
            new RecipeType<>(StrainerRecipeCategory.UID, StrainerRecipeDisplay.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WOODEN_STRAINER.get()), StrainerRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StrainerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        var allRecipes = recipeManager.getAllRecipesFor(ModRecipes.STRAINER_TYPE.get())
                .stream().map(RecipeHolder::value).toList();

        List<StrainerRecipeDisplay> displays = mergeStrainerRecipes(allRecipes);
        displays.sort(Comparator.comparingDouble(d ->
                d.getChanceResults().stream()
                        .mapToDouble(ChanceResult::chance)
                        .min()
                        .orElse(0.0)
        ));

        registration.addRecipes(StrainerRecipeCategory.RECIPE_TYPE, displays);
    }

    private static List<StrainerRecipeDisplay> mergeStrainerRecipes(List<StrainerRecipe> recipes) {
        Map<String, StrainerRecipeDisplay> merged = new HashMap<>();

        for (StrainerRecipe recipe : recipes) {
            for (MeshChanceResult meshChance : recipe.getRollResults()) {
                if (meshChance == MeshChanceResult.EMPTY) continue;

                // Create a stable key: input + blockAbove + mesh
                String key = recipe.input().toString() + "|" +
                        recipe.getBlockAbove().toString() + "|" +
                        meshChance.mesh().toString();

                merged.compute(key, (k, existing) -> {
                    if (existing == null) {
                        return new StrainerRecipeDisplay(
                                recipe.getBlockAbove(),
                                recipe.input(),
                                meshChance.mesh(),
                                List.of(meshChance.chanceResult())
                        );
                    } else {
                        List<ChanceResult> combined = new ArrayList<>(existing.getChanceResults());
                        combined.add(meshChance.chanceResult());
                        return new StrainerRecipeDisplay(
                                existing.getAboveBlock(),
                                existing.getInput(),
                                existing.getMesh(),
                                combined
                        );
                    }
                });
            }
        }

        return new ArrayList<>(merged.values());
    }


}
