package com.benbenlaw.strainers.integration.jei;

import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record StrainerRecipeDisplay(BlockState aboveBlock, Ingredient input, Ingredient mesh, List<ChanceResult> outputs) {

    public StrainerRecipeDisplay(BlockState aboveBlock, Ingredient input, Ingredient mesh, List<ChanceResult> outputs) {
        this.aboveBlock = aboveBlock;
        this.input = input;
        this.mesh = mesh;
        this.outputs = outputs;
    }

    public BlockState getAboveBlock() {
        return aboveBlock;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getMesh() {
        return mesh;
    }

    public List<ChanceResult> getChanceResults() {
        return outputs;
    }

}
