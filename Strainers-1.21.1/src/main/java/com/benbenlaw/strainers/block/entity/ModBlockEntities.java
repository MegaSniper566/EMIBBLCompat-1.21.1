package com.benbenlaw.strainers.block.entity;

import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Strainers.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodenStrainerBlockEntity>> WOODEN_STRAINER_BLOCK_ENTITY =
            register("wooden_strainer_block_entity", () ->
                    BlockEntityType.Builder.of(WoodenStrainerBlockEntity::new, ModBlocks.WOODEN_STRAINER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StrainerTankBlockEntity>> STRAINER_TANK_BLOCK_ENTITY =
            register("strainer_tank_block_entity", () ->
                    BlockEntityType.Builder.of(StrainerTankBlockEntity::new, ModBlocks.STRAINER_TANK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompactorBlockEntity>> COMPACTOR_BLOCK_ENTITY =
            register("compactor_block_entity", () ->
                    BlockEntityType.Builder.of(CompactorBlockEntity::new, ModBlocks.COMPACTOR.get()));


    //Capability Registration (Item Handler)
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.WOODEN_STRAINER_BLOCK_ENTITY.get(), WoodenStrainerBlockEntity::getItemHandlerCapability);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.COMPACTOR_BLOCK_ENTITY.get(), CompactorBlockEntity::getItemHandlerCapability);

    }


    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(@Nonnull String name, @Nonnull Supplier<BlockEntityType.Builder<T>> initializer) {
        return BLOCK_ENTITIES.register(name, () -> initializer.get().build(null));
    }
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
