package com.benbenlaw.strainers.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

//This is literally shaped recipe but pick the output from a tag, very useful, move to core in the future
public class TagOutputRecipe extends ShapedRecipe {

    public final TagKey<Item> outputTag;
    private final int count;

    public TagOutputRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, TagKey<Item> outputTag, int count, boolean notification) {
        super(group, category, pattern, ItemStack.EMPTY, notification);
        this.outputTag = outputTag;
        this.count = count;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider provider) {
        Optional<Item> item = BuiltInRegistries.ITEM.getTag(outputTag)
                .flatMap(tag -> tag.stream().findFirst())
                .map(Holder::value);

        return item.map(value -> new ItemStack(value, count)).orElse(ItemStack.EMPTY);

    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        Optional<Item> item = BuiltInRegistries.ITEM.getTag(outputTag)
                .flatMap(tag -> tag.stream().findFirst())
                .map(Holder::value);

        return item.map(value -> new ItemStack(value, count)).orElse(ItemStack.EMPTY);
    }

    public TagKey<Item> outputTag() {
        return outputTag;
    }

    public int count() {
        return count;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHAPED_TAG_OUTPUT.get();
    }

    public static class Type implements RecipeType<TagOutputRecipe> {
        private Type() { }
        public static final TagOutputRecipe.Type INSTANCE = new TagOutputRecipe.Type();}

    public static class Serializer implements RecipeSerializer<TagOutputRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<Pair<TagKey<Item>, Integer>> RESULT_CODEC = RecordCodecBuilder.mapCodec(pairInstance ->
                pairInstance.group(
                        ResourceLocation.CODEC.fieldOf("tag").forGetter(pair -> pair.getFirst().location()),
                        Codec.INT.optionalFieldOf("count", 1).forGetter(Pair::getSecond)
                ).apply(pairInstance, (tagLoc, count) ->
                        Pair.of(TagKey.create(Registries.ITEM, tagLoc), count)
                )
        );

        public static final MapCodec<TagOutputRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(TagOutputRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").forGetter(TagOutputRecipe::category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                        RESULT_CODEC.fieldOf("result").forGetter(recipe -> Pair.of(recipe.outputTag, recipe.count)),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(TagOutputRecipe::showNotification)
                ).apply(instance, (group, category, pattern, resultPair, showNotif) ->
                        new TagOutputRecipe(group, category, pattern, resultPair.getFirst(), resultPair.getSecond(), showNotif)
                )
        );


        private final StreamCodec<RegistryFriendlyByteBuf, TagOutputRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<TagOutputRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, TagOutputRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static TagOutputRecipe read(RegistryFriendlyByteBuf buf) {
            String group = buf.readUtf();
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);

            ResourceLocation tagId = buf.readResourceLocation();
            int count = buf.readVarInt();
            boolean notification = buf.readBoolean();

            return new TagOutputRecipe(group, category, pattern, TagKey.create(Registries.ITEM, tagId), count, notification);
        }

        private static void write(RegistryFriendlyByteBuf buf, TagOutputRecipe recipe) {
            buf.writeUtf(recipe.getGroup());
            buf.writeEnum(recipe.category());
            ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);

            buf.writeResourceLocation(recipe.outputTag.location());
            buf.writeVarInt(recipe.count);
            buf.writeBoolean(recipe.showNotification());
        }
    }
}
