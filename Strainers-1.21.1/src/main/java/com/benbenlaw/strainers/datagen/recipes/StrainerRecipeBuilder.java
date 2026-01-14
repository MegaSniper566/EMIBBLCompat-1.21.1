package com.benbenlaw.strainers.datagen.recipes;

import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.recipe.MeshChanceResult;
import com.benbenlaw.strainers.recipe.StrainerRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class StrainerRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected BlockState aboveBlock;
    protected Ingredient input;
    protected NonNullList<MeshChanceResult> results;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public StrainerRecipeBuilder(BlockState aboveBlock, Ingredient input, NonNullList<MeshChanceResult> results) {
        this.aboveBlock = aboveBlock;
        this.input = input;
        this.results = results;
    }

    public static StrainerRecipeBuilder strainerRecipe(BlockState aboveBlock, Ingredient input, NonNullList<MeshChanceResult> results) {
        return new StrainerRecipeBuilder(aboveBlock, input, results);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return results.getFirst().chanceResult().stack().getItem();
    }

    public void save(@NotNull RecipeOutput recipeOutput) {
        this.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/"));
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        StrainerRecipe strainerRecipe = new StrainerRecipe(aboveBlock, input, results);
        recipeOutput.accept(id, strainerRecipe, builder.build(id.withPrefix("recipe/strainer/")));
    }
}