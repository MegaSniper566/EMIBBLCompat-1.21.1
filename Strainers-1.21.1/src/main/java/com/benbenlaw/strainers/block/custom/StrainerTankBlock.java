package com.benbenlaw.strainers.block.custom;

import com.benbenlaw.strainers.block.entity.ModBlockEntities;
import com.benbenlaw.strainers.block.entity.StrainerTankBlockEntity;
import com.benbenlaw.strainers.item.StrainersDataComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrainerTankBlock extends BaseEntityBlock {

    public static final MapCodec<StrainerTankBlock> CODEC = simpleCodec(StrainerTankBlock::new);
    public StrainerTankBlock(Properties properties) {
        super(properties);
    }

    public FluidState fluidState = Fluids.EMPTY.defaultFluidState();

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        // Retrieve the block entity at the position
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof StrainerTankBlockEntity)) {
            return InteractionResult.FAIL;
        }

        StrainerTankBlockEntity entity = (StrainerTankBlockEntity) blockEntity;

        //FILL BUCKET//
         StrainerTankBlockEntity strainerTankBlockEntity = (StrainerTankBlockEntity) level.getBlockEntity(blockPos);
            if (strainerTankBlockEntity != null && strainerTankBlockEntity.onPlayerUse(player, InteractionHand.MAIN_HAND)) {
                return InteractionResult.SUCCESS;
            }
        return InteractionResult.PASS;
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState currentState, boolean allow) {
        super.onPlace(blockState, level, blockPos, currentState, allow);

        StrainerTankBlockEntity tankEntity = (StrainerTankBlockEntity) level.getBlockEntity(blockPos);
        assert tankEntity != null;

        if (!tankEntity.getFluidStack().isEmpty()) {
            return;
        }

        if (currentState.getFluidState().isSource()) {
            tankEntity.setFluid(new FluidStack(currentState.getFluidState().getType(), 1000));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, entity, itemStack);

        // Fetch the block entity
        StrainerTankBlockEntity tankEntity = (StrainerTankBlockEntity) level.getBlockEntity(blockPos);
        assert tankEntity != null;

        // Check if the itemStack has fluid data or use the current fluid state
        if (itemStack.has(StrainersDataComponents.FLUID_TYPE)) {
            SimpleFluidContent fluidContent = itemStack.get(StrainersDataComponents.FLUID_TYPE);
            assert fluidContent != null;
            Fluid fluid = fluidContent.getFluid();
            tankEntity.setFluid(new FluidStack(fluid, 1000));
        }
    }



    @Override
    public void appendHoverText(ItemStack itemStack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        if (itemStack.has(StrainersDataComponents.FLUID_TYPE)) {
            SimpleFluidContent fluidContent = itemStack.get(StrainersDataComponents.FLUID_TYPE);
            assert fluidContent != null;
            String fluidAsString = fluidContent.getFluid().getFluidType().getDescriptionId().toString();
            FluidType fluid = BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(fluidAsString)).getFluidType();
            components.add(Component.literal("Contains: ").append(Component.translatable(fluidAsString)).withStyle(ChatFormatting.GREEN));
        }

        super.appendHoverText(itemStack, context, components, flag);

    }

    //BLOCK ENTITY


    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        blockState.getBlock();
        newBlockState.getBlock();
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StrainerTankBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.STRAINER_TANK_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }

}