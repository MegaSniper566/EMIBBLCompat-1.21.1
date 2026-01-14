package com.benbenlaw.strainers.recipe;

import com.benbenlaw.core.recipe.ChanceResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record MeshChanceResult(ChanceResult chanceResult, Ingredient mesh) {

    public static final MeshChanceResult EMPTY = new MeshChanceResult(ChanceResult.EMPTY, Ingredient.EMPTY);

    public ItemStack rollOutput(RandomSource rand, ItemStack meshStack) {
        if (!mesh.isEmpty() && !mesh.test(meshStack)) {
            return ItemStack.EMPTY;
        }
        return chanceResult.rollOutput(rand);
    }

    public static final Codec<MeshChanceResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(r -> r.chanceResult().stack()),
            Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(r -> r.chanceResult().chance()),
            Ingredient.CODEC.optionalFieldOf("mesh", Ingredient.EMPTY).forGetter(MeshChanceResult::mesh)
    ).apply(instance, (stack, chance, mesh) -> new MeshChanceResult(new ChanceResult(stack, chance), mesh)));


    public static final StreamCodec<RegistryFriendlyByteBuf, MeshChanceResult> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, value) -> value.write(buffer), MeshChanceResult::read);

    public void write(RegistryFriendlyByteBuf buffer) {
        ChanceResult result = this.chanceResult();
        result.write(buffer);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, this.mesh());
    }

    public static MeshChanceResult read(RegistryFriendlyByteBuf buffer) {
        ChanceResult result = ChanceResult.read(buffer);
        Ingredient mesh = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        return new MeshChanceResult(result, mesh);

    }
}
