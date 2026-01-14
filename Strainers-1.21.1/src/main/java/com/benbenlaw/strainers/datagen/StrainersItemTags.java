package com.benbenlaw.strainers.datagen;

import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.item.ModItems;
import com.benbenlaw.strainers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StrainersItemTags extends ItemTagsProvider {

    StrainersItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags.contentsGetter(), Strainers.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(ModTags.Items.UPGRADES)
                .add(ModItems.SPEED_UPGRADE_1.get())
                .add(ModItems.SPEED_UPGRADE_2.get())
                .add(ModItems.SPEED_UPGRADE_3.get())
                .add(ModItems.CHANCE_UPGRADE_1.get())
                .add(ModItems.CHANCE_UPGRADE_2.get())
                .add(ModItems.CHANCE_UPGRADE_3.get())
        ;

        tag(ModTags.Items.TIER_1_MESHES)
                .add(ModItems.WOODEN_MESH.get())
        ;

        tag(ModTags.Items.TIER_2_MESHES)
                .add(ModItems.STRING_MESH.get())
                .add(ModItems.BAMBOO_MESH.get())
        ;

        tag(ModTags.Items.TIER_3_MESHES)
                .add(ModItems.FLINT_MESH.get())
                .add(ModItems.BONE_MESH.get())
        ;

        tag(ModTags.Items.TIER_4_MESHES)
                .add(ModItems.COPPER_MESH.get())
                .add(ModItems.TIN_MESH.get())
                .add(ModItems.BRONZE_MESH.get())
        ;

        tag(ModTags.Items.TIER_5_MESHES)
                .add(ModItems.IRON_MESH.get())
                .add(ModItems.QUARTZ_MESH.get())
                .add(ModItems.LAPIS_MESH.get())
                .add(ModItems.PRISMARINE_MESH.get())
        ;

        tag(ModTags.Items.TIER_6_MESHES)
                .add(ModItems.GOLD_MESH.get())
                .add(ModItems.REDSTONE_MESH.get())
                .add(ModItems.ECHO_MESH.get())
                .add(ModItems.BLAZE_MESH.get())
                .add(ModItems.BREEZE_MESH.get())
        ;

        tag(ModTags.Items.TIER_7_MESHES)
                .add(ModItems.DIAMOND_MESH.get())
                .add(ModItems.EMERALD_MESH.get())
                .add(ModItems.AMETHYST_MESH.get())
        ;

        tag(ModTags.Items.TIER_8_MESHES)
                .add(ModItems.NETHERITE_MESH.get())
                .add(ModItems.OBSIDIAN_MESH.get())
        ;

        tag(ModTags.Items.TIER_9_MESHES)
                .add(ModItems.HEAVY_MESH.get())
        ;

        tag(ModTags.Items.TIER_10_MESHES)
                .add(ModItems.END_MESH.get());
        ;


        tag(ModTags.Items.MESHES)
                .addTag(ModTags.Items.TIER_1_MESHES)
                .addTag(ModTags.Items.TIER_2_MESHES)
                .addTag(ModTags.Items.TIER_3_MESHES)
                .addTag(ModTags.Items.TIER_4_MESHES)
                .addTag(ModTags.Items.TIER_5_MESHES)
                .addTag(ModTags.Items.TIER_6_MESHES)
                .addTag(ModTags.Items.TIER_7_MESHES)
                .addTag(ModTags.Items.TIER_8_MESHES)
                .addTag(ModTags.Items.TIER_9_MESHES)
                .addTag(ModTags.Items.TIER_10_MESHES)
        ;
    }

    @Override
    public @NotNull String getName() {
        return Strainers.MOD_ID + " Item Tags";
    }
}
