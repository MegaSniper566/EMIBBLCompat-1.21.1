package com.benbenlaw.strainers.datagen;

import com.benbenlaw.opolisutilities.fluid.FluidDeferredRegister;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.fluid.StrainersFluids;
import com.benbenlaw.strainers.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class StrainersBlockStatesProvider extends BlockStateProvider {

    public StrainersBlockStatesProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Strainers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.MULCH);
        blockWithItem(ModBlocks.PURIFIED_SOUL_SAND);
        blockWithItem(ModBlocks.PURIFIED_SAND);
        blockWithItem(ModBlocks.PURIFIED_GRAVEL);
        blockWithItem(ModBlocks.PURIFIED_DIRT);
        blockWithItem(ModBlocks.PURIFIED_SOUL_SOIL);
        blockWithItem(ModBlocks.PURIFIED_STONE);
        blockWithItem(ModBlocks.COMPACTOR);
       // blockWithItem(ModBlocks.ORE_MULCH);

        //Currently using empty pngs with both the fluid name and the fluid name _still version (fix for 1.22)
        fluidBlocks("purifying_water", StrainersFluids.PURIFYING_WATER.getBlock());
        fluidBlocks("eroding_water", StrainersFluids.ERODING_WATER.getBlock());

    }




    private void blockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void fluidBlocks(String name, Block block) {
        simpleBlock(block, models().getBuilder(name).texture("particle", ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "block/" + name + "_still")));
    }


    @Override
    public String getName() {
        return Strainers.MOD_ID + " Block States";
    }
}
