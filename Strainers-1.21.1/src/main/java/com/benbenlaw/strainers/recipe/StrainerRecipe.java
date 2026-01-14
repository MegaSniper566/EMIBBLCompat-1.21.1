package com.benbenlaw.strainers.recipe;

import com.benbenlaw.core.block.TankBlockEntity;
import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.strainers.block.custom.StrainerTankBlock;
import com.benbenlaw.strainers.block.entity.StrainerTankBlockEntity;
import com.benbenlaw.strainers.block.entity.WoodenStrainerBlockEntity;
import com.benbenlaw.strainers.util.ModTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record StrainerRecipe(
        BlockState aboveBlock,
        Ingredient input,
        NonNullList<MeshChanceResult> results) implements Recipe<RecipeInput> {

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        if (level.isClientSide()) {
            return false;
        }

        boolean hasInput = input.test(container.getItem(WoodenStrainerBlockEntity.INPUT_SLOT));
        ItemStack meshStack = container.getItem(WoodenStrainerBlockEntity.MESH_SLOT);

        if (container instanceof StrainerRecipeInput strainerRecipeInput) {

            if (meshStack.isEmpty()) {
                return false;
            }

            BlockState aboveBlockState = level.getBlockState(strainerRecipeInput.getPos().above());

            //Check for tank and change block above in theory to the fluid
            if (level.getBlockState(strainerRecipeInput.getPos().above()).getBlock() instanceof StrainerTankBlock) {
                BlockEntity entity = level.getBlockEntity(strainerRecipeInput.getPos().above());
                if (entity instanceof StrainerTankBlockEntity tankEntity) {
                    Fluid fluid = tankEntity.FLUID_TANK.getFluid().getFluid();
                    aboveBlockState = fluid.defaultFluidState().createLegacyBlock();
                }
            }

            return hasInput && aboveBlockState.equals(aboveBlock) && results.stream().anyMatch(
                    meshChanceResult -> meshChanceResult.mesh().test(meshStack)
            );
        }

        return false;
    }


    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput container, HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return results.getFirst().chanceResult().stack();
    }

    public List<ItemStack> getResults() {
        return getRollResults().stream()
                .map(results -> results.chanceResult().stack())
                .collect(Collectors.toList());
    }

    public NonNullList<MeshChanceResult> getRollResults() {
        return this.results;
    }

    public List<ItemStack> rollResults(RandomSource rand, ItemStack meshStack) {
        List<ItemStack> resultsList = new ArrayList<>();
        for (MeshChanceResult output : this.results) {
            ItemStack stack = output.rollOutput(rand, meshStack);
            if (!stack.isEmpty()) {
                resultsList.add(stack);
            }
        }
        return resultsList;
    }

    public BlockState getBlockAbove() {
        return aboveBlock;
    }

    public Ingredient getMesh() {
        return results.getFirst().mesh();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<StrainerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();}

    public static class Serializer implements RecipeSerializer<StrainerRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<StrainerRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        BlockState.CODEC.fieldOf("above_block").forGetter(StrainerRecipe::aboveBlock),
                        Ingredient.CODEC.fieldOf("input").forGetter(StrainerRecipe::input),
                        Codec.list(MeshChanceResult.CODEC).fieldOf("results").flatXmap(resultList -> {
                            NonNullList<MeshChanceResult> nonNullList = NonNullList.create();
                            nonNullList.addAll(resultList);
                            return DataResult.success(nonNullList);
                        }, DataResult::success).forGetter(StrainerRecipe::results)
                ).apply(instance, StrainerRecipe::new)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, StrainerRecipe> STREAM_CODEC = StreamCodec.of(
                StrainerRecipe.Serializer::write, StrainerRecipe.Serializer::read);

        @Override
        public @NotNull MapCodec<StrainerRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, StrainerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static StrainerRecipe read(RegistryFriendlyByteBuf buffer) {
            BlockState aboveBlock = Block.stateById(buffer.readInt());
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int size = buffer.readVarInt();
            NonNullList<MeshChanceResult> outputs = NonNullList.withSize(size, MeshChanceResult.EMPTY);
            outputs.replaceAll(ignored -> MeshChanceResult.read(buffer));
            return new StrainerRecipe(aboveBlock, input, outputs);
        }

        private static void write(RegistryFriendlyByteBuf buffer, StrainerRecipe recipe) {
            buffer.writeInt(Block.getId(recipe.aboveBlock));
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
            buffer.writeVarInt(recipe.results.size());
            for (MeshChanceResult output : recipe.results) {
                output.write(buffer);
            }
        }
    }
}