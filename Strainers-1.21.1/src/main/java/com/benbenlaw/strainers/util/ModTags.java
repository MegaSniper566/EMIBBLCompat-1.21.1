package com.benbenlaw.strainers.util;

import com.benbenlaw.strainers.Strainers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> EMPTY = tag("empty");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }


    public static class Items {

        public static final TagKey<Item> EMPTY = tag("empty");
        public static final TagKey<Item> TIER_1_MESHES = tag("tier_1_meshes");
        public static final TagKey<Item> TIER_2_MESHES = tag("tier_2_meshes");
        public static final TagKey<Item> TIER_3_MESHES = tag("tier_3_meshes");
        public static final TagKey<Item> TIER_4_MESHES = tag("tier_4_meshes");
        public static final TagKey<Item> TIER_5_MESHES = tag("tier_5_meshes");
        public static final TagKey<Item> TIER_6_MESHES = tag("tier_6_meshes");
        public static final TagKey<Item> TIER_7_MESHES = tag("tier_7_meshes");
        public static final TagKey<Item> TIER_8_MESHES = tag("tier_8_meshes");
        public static final TagKey<Item> TIER_9_MESHES = tag("tier_9_meshes");
        public static final TagKey<Item> TIER_10_MESHES = tag("tier_10_meshes");
        public static final TagKey<Item> UPGRADES = tag("upgrades");

        public static final TagKey<Item> MESHES = tag("meshes");

        public static final TagKey<Item> NOT_CONSUMED = tag("not_consumed");



        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }

    public static Ingredient getMeshIngredient(int tier) {
        return switch (tier) {
            case 1 -> Ingredient.of(Items.TIER_1_MESHES);
            case 2 -> Ingredient.of(Items.TIER_2_MESHES);
            case 3 -> Ingredient.of(Items.TIER_3_MESHES);
            case 4 -> Ingredient.of(Items.TIER_4_MESHES);
            case 5 -> Ingredient.of(Items.TIER_5_MESHES);
            case 6 -> Ingredient.of(Items.TIER_6_MESHES);
            case 7 -> Ingredient.of(Items.TIER_7_MESHES);
            case 8 -> Ingredient.of(Items.TIER_8_MESHES);
            case 9 -> Ingredient.of(Items.TIER_9_MESHES);
            case 10 -> Ingredient.of(Items.TIER_10_MESHES);
            default -> Ingredient.EMPTY;
        };
    }
}
