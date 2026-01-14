package com.benbenlaw.strainers.datagen;

import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.item.ModItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class StrainersLootTableProvider extends VanillaBlockLoot {

    public StrainersLootTableProvider(HolderLookup.Provider p_344962_) {
        super(p_344962_);
    }
    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.WOODEN_STRAINER.get());
        //this.dropSelf(ModBlocks.ORE_MULCH.get());
        this.dropSelf(ModBlocks.MULCH.get());
        this.dropSelf(ModBlocks.PURIFIED_SOUL_SAND.get());
        this.dropSelf(ModBlocks.PURIFIED_SAND.get());
        this.dropSelf(ModBlocks.PURIFIED_GRAVEL.get());
        this.dropSelf(ModBlocks.PURIFIED_DIRT.get());
        this.dropSelf(ModBlocks.PURIFIED_SOUL_SOIL.get());
        this.dropSelf(ModBlocks.PURIFIED_STONE.get());
        this.dropSelf(ModBlocks.COMPACTOR.get());

        this.add(ModBlocks.STRAINER_TANK.get(),loot -> LootTable.lootTable()
                .withPool(new LootPool.Builder().add(LootItem.lootTableItem(ModBlocks.STRAINER_TANK)
                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY))
                )));

    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        //Overwrite the core register method to add to our list of known blocks
        super.add(block, table);
        knownBlocks.add(block);
    }
    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    @NotNull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}
