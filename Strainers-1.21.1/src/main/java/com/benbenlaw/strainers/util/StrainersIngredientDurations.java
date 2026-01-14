package com.benbenlaw.strainers.util;

import com.benbenlaw.strainers.config.StrainersConfigFile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrainersIngredientDurations {

    private static final Map<Ingredient, Integer> map = new HashMap<>();

    public static void loadItemDurationsFromConfig(List<? extends String> entries) {
        for (String entry : entries) {
            String[] parts = entry.split("=");
            if (parts.length != 2) continue;
            String key = parts[0].trim();
            String durationString = parts[1].trim();

            Ingredient ingredient;
            if (key.startsWith("#")) {
                TagKey<Item> tag = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.parse(key.substring(1)));
                ingredient = Ingredient.of(tag);
            } else {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key));
                ingredient = Ingredient.of(item);
            }

            try {
                int duration = Integer.parseInt(durationString);
                map.put(ingredient, duration);
                System.out.println("Loaded block duration: " + key + " = " + duration);

            } catch (Exception e) {
                System.err.println("Error parsing block duration entry: " + entry);
            }

        }
    }

    public static Map<Ingredient, Integer> getMap() {
        return map;
    }

    public static int getDuration(ItemStack stack) {
        for (Map.Entry<Ingredient, Integer> entry : map.entrySet()) {
            if (entry.getKey().test(stack)) {
                return entry.getValue();
            }
        }
        return StrainersConfigFile.strainerMaxProgress.get();
    }
}
