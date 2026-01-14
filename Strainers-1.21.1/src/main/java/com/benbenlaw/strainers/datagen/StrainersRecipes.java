package com.benbenlaw.strainers.datagen;

import com.benbenlaw.core.item.CoreItems;
import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.datagen.recipes.StrainerRecipeBuilder;
import com.benbenlaw.strainers.fluid.StrainersFluids;
import com.benbenlaw.strainers.item.ModItems;
import com.benbenlaw.strainers.recipe.MeshChanceResult;
import com.benbenlaw.strainers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems;

public class StrainersRecipes extends RecipeProvider {

    public StrainersRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        //Compactor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COMPACTOR)
                .pattern("III")
                .pattern("ICI")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Items.PISTON)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        //Mesh Upgrade 1
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHANCE_UPGRADE_1)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', ModTags.Items.TIER_2_MESHES)
                .define('U', Items.IRON_INGOT)
                .group("strainers")
                .unlockedBy("has_item", has(ModTags.Items.TIER_2_MESHES))
                .save(consumer);

        //Mesh Upgrade 2
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHANCE_UPGRADE_2)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', ModTags.Items.TIER_5_MESHES)
                .define('U', ModItems.CHANCE_UPGRADE_1)
                .group("strainers")
                .unlockedBy("has_item", has(ModTags.Items.TIER_4_MESHES))
                .save(consumer);

        //Mesh Upgrade 3
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHANCE_UPGRADE_3)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', ModTags.Items.TIER_8_MESHES)
                .define('U', ModItems.CHANCE_UPGRADE_2)
                .group("strainers")
                .unlockedBy("has_item", has(ModTags.Items.TIER_6_MESHES))
                .save(consumer);


        //Output Upgrade 1
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPEED_UPGRADE_1)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('U', Items.IRON_INGOT)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(consumer);

        //Output Upgrade 2
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPEED_UPGRADE_2)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('U', ModItems.SPEED_UPGRADE_1)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
                .save(consumer);

        //Output Upgrade 3
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPEED_UPGRADE_3)
                .pattern(" M ")
                .pattern("MUM")
                .pattern(" M ")
                .define('M', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .define('U', ModItems.SPEED_UPGRADE_2)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.STORAGE_BLOCKS_NETHERITE))
                .save(consumer);
        // ********** Vanilla Recipes ********** //

        //Eroding Salt Mulch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ERODING_SALT_MULCH)
                .pattern("SSS")
                .pattern("SMS")
                .pattern("SSS")
                .define('S', Items.CHARCOAL)
                .define('M', ModBlocks.MULCH)
                .group("strainers")
                .unlockedBy("has_item", has(ModBlocks.MULCH))
                .save(consumer);

        //Purifying Salt Mulch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PURIFYING_SALT_MULCH)
                .pattern("SSS")
                .pattern("SMS")
                .pattern("SSS")
                .define('S', ItemTags.SAND)
                .define('M', ModBlocks.MULCH)
                .group("strainers")
                .unlockedBy("has_item", has(ModBlocks.MULCH))
                .save(consumer);

        //Blazing Salt Mulch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLAZING_SALT_MULCH)
                .pattern("SSS")
                .pattern("SMS")
                .pattern("SSS")
                .define('S', Items.BLAZE_POWDER)
                .define('M', ModBlocks.MULCH)
                .group("strainers")
                .unlockedBy("has_item", has(ModBlocks.MULCH))
                .save(consumer);

        //Tank
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STRAINER_TANK)
                .pattern("GGG")
                .pattern("GSG")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('S', ModBlocks.WOODEN_STRAINER)
                .group("strainers")
                .unlockedBy("has_item", has(Blocks.GLASS))
                .save(consumer);

        //Mulch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MULCH)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.LEAF_PILE)
                .group("strainers")
                .unlockedBy("has_item", has(ModItems.LEAF_PILE))
                .save(consumer);

        //Dirt
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.DIRT, 2)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.MULCH)
                .group("strainers")
                .unlockedBy("has_item", has(ModBlocks.MULCH))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "dirt_from_mulch"));

        //Bamboo Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BAMBOO_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.BAMBOO)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.BAMBOO))
                .save(consumer);

        //Netherite Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHERITE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.NETHERITE_INGOT)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.NETHERITE_INGOT))
                .save(consumer);

        //Obsidian Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OBSIDIAN_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Blocks.OBSIDIAN)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Blocks.OBSIDIAN))
                .save(consumer);

        //Prismarine Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PRISMARINE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.GEMS_PRISMARINE)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.GEMS_PRISMARINE))
                .save(consumer);

        //End Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.END_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.END_STONES)
                .define('S', Items.END_ROD)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.END_STONES))
                .save(consumer);

        //Heavy Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEAVY_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.HEAVY_CORE)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Items.HEAVY_CORE))
                .save(consumer);

        //Wooden Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_MESH)
                .pattern("SSS")
                .pattern("S S")
                .pattern("SSS")
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        //Bone mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.BONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.BONE))
                .save(consumer);

        //Blaze Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLAZE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.BLAZE_POWDER)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.BLAZE_ROD))
                .save(consumer);

        //Breeze Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BREEZE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.BREEZE_ROD)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.BREEZE_ROD))
                .save(consumer);

        //Bronze Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BRONZE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', CommonTags.getTag("bronze", CommonTags.ResourceType.INGOTS))
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(CommonTags.getTag("bronze", CommonTags.ResourceType.INGOTS)))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("bronze", CommonTags.ResourceType.INGOTS)))));

        //Copper Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COPPER_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.INGOTS_COPPER)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_COPPER))
                .save(consumer);

        //Diamond Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.DIAMOND))
                .save(consumer);

        //Echo Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ECHO_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.ECHO_SHARD)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Items.ECHO_SHARD))
                .save(consumer);

        //Emerald Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMERALD_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.EMERALD)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.EMERALD))
                .save(consumer);

        //Flint Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLINT_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Items.FLINT)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", hasItems(Items.FLINT))
                .save(consumer);

        //Gold Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

        //Iron Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        //lapis Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LAPIS_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.GEMS_LAPIS)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.GEMS_LAPIS))
                .save(consumer);

        //Redstone Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REDSTONE_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.DUSTS_REDSTONE))
                .save(consumer);

        //Tin Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIN_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', CommonTags.getTag("tin", CommonTags.ResourceType.INGOTS))
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(CommonTags.getTag("tin", CommonTags.ResourceType.INGOTS)))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("tin", CommonTags.ResourceType.INGOTS)))));

        //Amethyst Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AMETHYST_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.GEMS_AMETHYST)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.GEMS_AMETHYST))
                .save(consumer);

        //Quartz Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.QUARTZ_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.GEMS_QUARTZ)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.GEMS_QUARTZ))
                .save(consumer);

        //String Mesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STRING_MESH)
                .pattern("SBS")
                .pattern("BBB")
                .pattern("SBS")
                .define('B', Tags.Items.STRINGS)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("strainers")
                .unlockedBy("has_item", has(Items.STRING))
                .save(consumer);

        //Strainer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WOODEN_STRAINER)
                .pattern("SMS")
                .pattern("SMS")
                .pattern("LLL")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('L', ItemTags.LOGS)
                .define('M', ModTags.Items.MESHES)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        //STONE
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.COBBLESTONE)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModItems.STONE_PEBBLE)
                .group("strainers")
                .unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "stone_from_pebble"));


        // ********** Strainer Recipes ********** //
        BlockState WATER = Blocks.WATER.defaultBlockState();
        BlockState LAVA = Blocks.LAVA.defaultBlockState();
        BlockState ERODING_WATER = StrainersFluids.ERODING_WATER.getBlock().defaultBlockState();
        BlockState PURIFYING_WATER = StrainersFluids.PURIFYING_WATER.getBlock().defaultBlockState();

        //Dirt -> Stone Pebble
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(Items.DIRT), createMeshChanceResultList(
                new ItemStack(ModItems.STONE_PEBBLE.get(), 4),
                0.8f, 1,5,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/dirt/stone_pebble"));

        //Dirt -> Stone Pebble (RARE)
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(Items.DIRT), createMeshChanceResultList(
                new ItemStack(ModItems.STONE_PEBBLE.get(), 4),
                0.2f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/dirt/stone_pebble_rar"));

        //Dirt -> Stick
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(Items.DIRT), createMeshChanceResultList(
                new ItemStack(Items.STICK),
                0.1f, 1,5,0.1f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/dirt/stick"));

        //Leaves -> String
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ItemTags.LEAVES), createMeshChanceResultList(
                new ItemStack(Items.STRING),
                0.05f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/leaves/string"));

        //Leaves -> Stick
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ItemTags.LEAVES), createMeshChanceResultList(
                new ItemStack(Items.STICK),
                0.7f, 1,10,0.1f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/leaves/stick"));

        //Leaves -> Leaf Pile
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ItemTags.LEAVES), createMeshChanceResultList(
                new ItemStack(ModItems.LEAF_PILE.get(), 1),
                0.5f, 1,10,0.1f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/leaves/leaf_pile"));

        //Leaves -> Leaf Pile (RARE)
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ItemTags.LEAVES), createMeshChanceResultList(
                new ItemStack(ModItems.LEAF_PILE.get(), 4),
                0.2f, 1,10,0.1f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/leaves/leaf_pile_rare"));

        //Cobblestone -> Gravel
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(Blocks.COBBLESTONE), createMeshChanceResultList(
                new ItemStack(Items.GRAVEL),
                0.75f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/cobblestone/gravel"));

        //Gravel -> Sand
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(Items.GRAVEL), createMeshChanceResultList(
                new ItemStack(Items.SAND),
                0.75f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/gravel/sand"));

        //Gravel -> Flint
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(Items.GRAVEL), createMeshChanceResultList(
                new ItemStack(Items.FLINT),
                0.1f, 2,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/gravel/flint"));

        //Purified Dirt -> Melon Seeds
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.MELON_SEEDS),
                0.15f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/melon_seeds"));

        //Purified Dirt -> Beetroot Seeds
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.BEETROOT_SEEDS),
                0.25f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/beetroot_seeds"));

        //Purified Dirt -> Carrot
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.CARROT),
                0.2f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/carrot"));

        //Purified Dirt -> Potato
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.POTATO),
                0.2f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/potato"));

        //Purified Dirt -> Pumpkin Seeds
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.PUMPKIN_SEEDS),
                0.15f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/pumpkin_seeds"));

        //Purified Dirt -> Sweet Berries
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.SWEET_BERRIES),
                0.15f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/sweet_berries"));

        //Purified Dirt -> Wheat Seeds
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.WHEAT_SEEDS),
                0.35f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/wheat_seeds"));

        //Purified Dirt -> Bone Meal
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.BONE_MEAL),
                0.2f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/bone_meal"));

        //Purified Dirt -> Clay Ball
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.CLAY_BALL),
                0.5f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/clay"));

        //Purified Dirt -> Clay Ball (RARE)
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.CLAY_BALL, 4),
                0.2f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/clay_rare"));

        //Purified Dirt -> Oak Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.OAK_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/oak_sapling"));

        //Purified Dirt -> Birch Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.BIRCH_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/birch_sapling"));

        //Purified Dirt -> Spruce Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.SPRUCE_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/spruce_sapling"));

        //Purified Dirt -> Jungle Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.JUNGLE_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/jungle_sapling"));

        //Purified Dirt -> Acacia Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.ACACIA_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/acacia_sapling"));

        //Purified Dirt -> Dark Oak Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.DARK_OAK_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/dark_oak_sapling"));

        //Purified Dirt -> Cherry Sapling
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.CHERRY_SAPLING),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/cherry_sapling"));

        //Purified Dirt -> Mangrove Propagule
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.MANGROVE_PROPAGULE),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/mangrove_propagule"));

        //Purified Gravel -> Aluminum Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.ALUMINUM_ORE_PIECE.get()),
                0.4f, 2,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("aluminum", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/aluminum_ore_piece"));

        //Purified Gravel -> Tin Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.TIN_ORE_PIECE.get()),
                0.35f, 3,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("tin", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/tin_ore_piece"));

        //Purified Gravel -> Redstone Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.REDSTONE_ORE_PIECE.get()),
                0.3f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/redstone_ore_piece"));

        //Purified Gravel -> Silver Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.SILVER_ORE_PIECE.get()),
                0.35f, 5,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("silver", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/silver_ore_piece"));

        //Purified Gravel -> Nickel Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.NICKEL_ORE_PIECE.get()),
                0.3f, 5,10,0.1f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("nickel", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/nickel_ore_piece"));

        //Purified Gravel -> Amethyst Shard
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(Items.AMETHYST_SHARD),
                0.3f, 6,10,0.1f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/amethyst_shard"));

        //Purified Gravel -> Uranium Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.URANIUM_ORE_PIECE.get()),
                0.4f, 8,10,0.1f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("uranium", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/uranium_ore_piece"));

        //Purified Sand -> Prismarine Crystals
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.PRISMARINE_CRYSTALS),
                0.2f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/prismarine_crystals"));

        //Purified Sand -> Prismarine Shard
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.PRISMARINE_SHARD),
                0.3f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/prismarine_shard"));

        //Purified Soul Sand -> Quartz Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SAND), createMeshChanceResultList(
                new ItemStack(ModItems.QUARTZ_ORE_PIECE.get()),
                0.3f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_soul_sand/quartz_ore_piece"));

        //Purified Gravel -> Coal Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.COAL_ORE_PIECE.get()),
                0.4f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/coal_ore_piece"));

        //Purified Gravel -> Copper Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.COPPER_ORE_PIECE.get()),
                0.35f, 3,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/copper_ore_piece"));

        //Purified Gravel -> Iron Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.IRON_ORE_PIECE.get()),
                0.35f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/iron_ore_piece"));

        //Purified Gravel -> Zinc Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.ZINC_ORE_PIECE.get()),
                0.35f, 4,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("zinc", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/zinc_ore_piece"));

        //Purified Gravel -> Gold Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.GOLD_ORE_PIECE.get()),
                0.2f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/gold_ore_piece"));

        //Purified Gravel -> Lead Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.LEAD_ORE_PIECE.get()),
                0.35f, 6,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("lead", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/lead_ore_piece"));

        //Purified Gravel -> Lapis Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.LAPIS_ORE_PIECE.get()),
                0.30f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/lapis_ore_piece"));

        //Purified Gravel -> Osmium Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.OSMIUM_ORE_PIECE.get()),
                0.35f, 8,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("osmium", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/osmium_ore_piece"));

        //Purified Gravel -> Platinum Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.PLATINUM_ORE_PIECE.get()),
                0.35f, 9,10,0.05f
        )).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CommonTags.getTag("platinum", CommonTags.ResourceType.ORES)))),
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/platinum_ore_piece"));

        //Purified Gravel -> Diamond Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.DIAMOND_ORE_PIECE.get()),
                0.1f, 6,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/diamond_ore_piece"));

        //Purified Gravel -> Emerald Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_GRAVEL), createMeshChanceResultList(
                new ItemStack(ModItems.EMERALD_ORE_PIECE.get()),
                0.1f, 6,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_gravel/emerald_ore_piece"));

        //Purified Soul Sand -> Blaze Powder
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SAND), createMeshChanceResultList(
                new ItemStack(Items.BLAZE_POWDER),
                0.4f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_soul_sand/blaze_powder"));

        //Purified Soul Sand -> Debris Ore Piece
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SAND), createMeshChanceResultList(
                new ItemStack(ModItems.DEBRIS_ORE_PIECE.get()),
                0.05f, 7,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_soul_sand/debris_ore_piece"));

        //Gravel -> Purified Gravel
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Items.GRAVEL), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_GRAVEL.get()),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/gravel/purified_gravel"));

        //Dirt -> Purified Dirt
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(ItemTags.DIRT), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_DIRT.get()),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/dirt/purified_dirt"));

        //Sand -> Purified Sand
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(ItemTags.SAND), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_SAND.get()),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/sand/purified_sand"));

        //Soul Soil -> Purified Soil Soil
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Items.SOUL_SOIL), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_SOUL_SOIL.get()),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/soul_soil/purified_soul_soil"));

        //Stone -> Purified Stone
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Blocks.STONE), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_STONE.get()),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/stone/purified_stone"));

        //Soul Sand -> Purified Soul Sand
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Items.SOUL_SAND), createMeshChanceResultList(
                new ItemStack(ModBlocks.PURIFIED_SOUL_SAND.get()),
                0.8f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/soul_sand/purified_soul_sand"));

        //Purifying Sand -> Soul Sand
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.SOUL_SAND),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/soul_sand"));

        //Purified Dirt -> Purified Soul Soil
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(ModBlocks.PURIFIED_DIRT), createMeshChanceResultList(
                new ItemStack(Items.SOUL_SOIL),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_dirt/soul_soil"));

        //Mulch -> DirtSoil
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(ModBlocks.MULCH), createMeshChanceResultList(
                new ItemStack(Items.DIRT),
                0.8f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/mulch/dirt"));

        //Copper Block -> Breeze Rod
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Blocks.COPPER_BLOCK), createMeshChanceResultList(
                new ItemStack(Items.BREEZE_ROD),
                0.5f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/copper_block/breeze_rod"));

        //Sculk Block -> Echo Shard
        StrainerRecipeBuilder.strainerRecipe(PURIFYING_WATER, Ingredient.of(Blocks.SCULK), createMeshChanceResultList(
                new ItemStack(Items.ECHO_SHARD),
                0.2f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/sculk/echo_shard"));

        //Purified Soul Soil -> Crimson Fungus
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SOIL), createMeshChanceResultList(
                new ItemStack(Items.CRIMSON_FUNGUS),
                0.4f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/soul_soil/crimson_fungus"));

        //Purified Soul Soil -> Warped Fungus
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SOIL), createMeshChanceResultList(
                new ItemStack(Items.WARPED_FUNGUS),
                0.4f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/soul_soil/warped_fungus"));

        //Purified Soul Soil -> Nehter Wart
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SOUL_SOIL), createMeshChanceResultList(
                new ItemStack(Items.NETHER_WART),
                0.2f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/soul_soil/nether_wart"));

        //Purified Stone -> Granite
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.GRANITE),
                0.05f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/granite"));

        //Purified Stone -> Diorite
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.DIORITE),
                0.05f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/diorite"));

        //Purified Stone -> Andesite
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.ANDESITE),
                0.05f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/andesite"));

        //Purified Stone -> Calcite
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.CALCITE),
                0.05f, 5,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/calcite"));

        //Purified Stone -> Dripstone
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.DRIPSTONE_BLOCK),
                0.05f, 6,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/dripstone"));

        //Dripstone -> Pointed Dripstone
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(Blocks.DRIPSTONE_BLOCK), createMeshChanceResultList(
                new ItemStack(Blocks.POINTED_DRIPSTONE, 2),
                0.4f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/dripstone_block/pointed_dripstone"));

        //Purified Stone -> Deepslate
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.DEEPSLATE),
                0.05f, 7,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/deepslate"));

        //Purified Stone -> Tuff
        StrainerRecipeBuilder.strainerRecipe(ERODING_WATER, Ingredient.of(ModBlocks.PURIFIED_STONE), createMeshChanceResultList(
                new ItemStack(Blocks.TUFF),
                0.4f, 4,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_stone/tuff"));

        //Purified Sand -> Cactus
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.CACTUS),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/cactus"));

        //Purified Sand -> Sugar Cane
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.SUGAR_CANE),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/sugar_cane"));

        //Purified Sand -> Sea Pickle
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.SEA_PICKLE ),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/sea_pickle"));

        //Purified Sand -> Kelp
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.KELP),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/kelp"));

        //Purified Sand -> Sea Grass
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.SEAGRASS),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/sea_grass"));

        //Purified Sand -> Bamboo
        StrainerRecipeBuilder.strainerRecipe(WATER, Ingredient.of(ModBlocks.PURIFIED_SAND), createMeshChanceResultList(
                new ItemStack(Items.BAMBOO),
                0.1f, 1,10,0.05f
        )).save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer/purified_sand/bamboo"));

    }



    public static NonNullList<MeshChanceResult> createMeshChanceResultList(ItemStack resultItem, float baseChance, int minMeshTier, int maxMeshTier, float chancePerTier) {
        NonNullList<MeshChanceResult> results = NonNullList.create();

        for (int tier = minMeshTier; tier <= maxMeshTier; tier++) {
            float chance = Math.round((baseChance + chancePerTier * (tier - minMeshTier)) * 100f) / 100f;
            if (chance >= 1.0f) {
                chance = 1.0f;
            }
            results.add(new MeshChanceResult(new ChanceResult(resultItem, chance),
                    Ingredient.of(getMeshTag(tier))));
        }

        return results;
    }

    private static TagKey<Item> getMeshTag(int tier) {
        return switch (tier) {
            case 1 -> ModTags.Items.TIER_1_MESHES;
            case 2 -> ModTags.Items.TIER_2_MESHES;
            case 3 -> ModTags.Items.TIER_3_MESHES;
            case 4 -> ModTags.Items.TIER_4_MESHES;
            case 5 -> ModTags.Items.TIER_5_MESHES;
            case 6 -> ModTags.Items.TIER_6_MESHES;
            case 7 -> ModTags.Items.TIER_7_MESHES;
            case 8 -> ModTags.Items.TIER_8_MESHES;
            case 9 -> ModTags.Items.TIER_9_MESHES;
            default -> ModTags.Items.TIER_10_MESHES;
        };
    }

    private void addOreRecipe(RecipeOutput consumer, ItemStack result, ItemLike input) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(input.asItem());
        String baseName = id.getPath().replace("_ore_piece", "");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.getItem())
                .pattern("AA")
                .pattern("AA")
                .define('A', input)
                .group("strainers")
                .unlockedBy("has_item", has(input))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "ore_piece/" + baseName));
    }


}
