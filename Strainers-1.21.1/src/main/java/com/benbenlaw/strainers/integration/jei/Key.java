package com.benbenlaw.strainers.integration.jei;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;

public record Key(Ingredient mesh, Ingredient input, BlockState aboveBlock) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key key)) return false;
        return mesh.equals(key.mesh) && input.equals(key.input) && aboveBlock.equals(key.aboveBlock);
    }

}