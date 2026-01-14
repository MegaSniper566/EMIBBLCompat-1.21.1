package com.benbenlaw.strainers.datagen;

import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.fluid.StrainersFluids;
import com.benbenlaw.strainers.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class StrainersItemModelProvider extends ItemModelProvider {

    public StrainersItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Strainers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //Buckets

        simpleBucketItem("purifying_water_bucket", StrainersFluids.PURIFYING_WATER.getFluid());
        simpleBucketItem("eroding_water_bucket", StrainersFluids.ERODING_WATER.getFluid());

        //Misc Items
        simpleItem(ModItems.STONE_PEBBLE);
        simpleItem(ModItems.PURIFYING_SALT_MULCH);
        simpleItem(ModItems.ERODING_SALT_MULCH);
        simpleItem(ModItems.BLAZING_SALT_MULCH);
        simpleItem(ModItems.LEAF_PILE);

        //Upgrades
        simpleItem(ModItems.SPEED_UPGRADE_1);
        simpleItem(ModItems.SPEED_UPGRADE_2);
        simpleItem(ModItems.SPEED_UPGRADE_3);
        simpleItem(ModItems.CHANCE_UPGRADE_1);
        simpleItem(ModItems.CHANCE_UPGRADE_2);
        simpleItem(ModItems.CHANCE_UPGRADE_3);

        //Meshes
        simpleItem(ModItems.WOODEN_MESH);
        simpleItem(ModItems.BAMBOO_MESH);
        simpleItem(ModItems.STRING_MESH);
        simpleItem(ModItems.BONE_MESH);

        simpleItem(ModItems.FLINT_MESH);
        simpleItem(ModItems.COPPER_MESH);
        simpleItem(ModItems.LAPIS_MESH);
        simpleItem(ModItems.TIN_MESH);

        simpleItem(ModItems.IRON_MESH);
        simpleItem(ModItems.AMETHYST_MESH);
        simpleItem(ModItems.BRONZE_MESH);
        simpleItem(ModItems.REDSTONE_MESH);

        simpleItem(ModItems.GOLD_MESH);
        simpleItem(ModItems.QUARTZ_MESH);
        simpleItem(ModItems.BLAZE_MESH);
        simpleItem(ModItems.BREEZE_MESH);

        simpleItem(ModItems.DIAMOND_MESH);
        simpleItem(ModItems.ECHO_MESH);
        simpleItem(ModItems.EMERALD_MESH);
        simpleItem(ModItems.PRISMARINE_MESH);

        simpleItem(ModItems.NETHERITE_MESH);
        simpleItem(ModItems.OBSIDIAN_MESH);
        simpleItem(ModItems.END_MESH);
        simpleItem(ModItems.HEAVY_MESH);

        //Ore Pieces
        simpleItem(ModItems.IRON_ORE_PIECE);
        simpleItem(ModItems.GOLD_ORE_PIECE);
        simpleItem(ModItems.COPPER_ORE_PIECE);
        simpleItem(ModItems.SILVER_ORE_PIECE);
        simpleItem(ModItems.TIN_ORE_PIECE);
        simpleItem(ModItems.LAPIS_ORE_PIECE);
        simpleItem(ModItems.REDSTONE_ORE_PIECE);
        simpleItem(ModItems.DIAMOND_ORE_PIECE);
        simpleItem(ModItems.EMERALD_ORE_PIECE);
        simpleItem(ModItems.LEAD_ORE_PIECE);
        simpleItem(ModItems.QUARTZ_ORE_PIECE);
        simpleItem(ModItems.NICKEL_ORE_PIECE);
        simpleItem(ModItems.ZINC_ORE_PIECE);
        simpleItem(ModItems.PLATINUM_ORE_PIECE);
        simpleItem(ModItems.OSMIUM_ORE_PIECE);
        simpleItem(ModItems.URANIUM_ORE_PIECE);
        simpleItem(ModItems.ALUMINUM_ORE_PIECE);
        simpleItem(ModItems.COAL_ORE_PIECE);
        simpleItem(ModItems.DEBRIS_ORE_PIECE);

    }


    private void simpleItem(DeferredItem<Item> item) {
        withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void simpleBucketItem(String name, Fluid fluid) {
        withExistingParent(name, ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(fluid);
    }

    private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID,"item/" + item.getId().getPath()));
    }


    @Override
    public String getName() {
        return Strainers.MOD_ID + " Item Models";
    }
}
