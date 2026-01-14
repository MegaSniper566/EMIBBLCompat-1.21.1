package com.benbenlaw.strainers.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.IInventoryHandlingBlockEntity;
import com.benbenlaw.core.block.entity.handler.InputOutputItemHandler;
import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.strainers.item.custom.UpgradeItem;
import com.benbenlaw.strainers.recipe.MeshChanceResult;
import com.benbenlaw.strainers.recipe.ModRecipes;
import com.benbenlaw.strainers.recipe.StrainerRecipe;
import com.benbenlaw.strainers.recipe.StrainerRecipeInput;
import com.benbenlaw.strainers.screen.custom.CompactorMenu;
import com.benbenlaw.strainers.screen.custom.WoodenStrainerMenu;
import com.benbenlaw.strainers.util.StrainersIngredientDurations;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompactorBlockEntity extends SyncableBlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {


    private final ItemStackHandler itemHandler = new ItemStackHandler(18) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;

            // If this is an input slot, use custom logic
            if (isInputSlot(slot, stack)) {
                return insertIntoInputs(stack, simulate);
            }

            // If this is an output slot, use custom logic
            if (isOutputSlot(slot)) {
                return insertIntoOutputs(stack, simulate);
            }

            // Fallback: behave normally
            return super.insertItem(slot, stack, simulate);
        }
    };


    public final ContainerData data;
    public static final int[] INPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static final int[] OUTPUT_SLOTS = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    private final IItemHandler compactorItemHandler = new InputOutputItemHandler(
            itemHandler,
            this::isInputSlot,
            this::isOutputSlot
    );

    private boolean isInputSlot(int slot, ItemStack itemStack) {
        for (int inputSlot : INPUT_SLOTS) {
            if (slot == inputSlot) {
                return true;
            }
        }
        return false;
    }
    private boolean isOutputSlot(int slot) {
        for (int outputSlot : OUTPUT_SLOTS) {
            if (slot == outputSlot) {
                return true;
            }
        }
        return false;
    }


    public IItemHandler getItemHandlerCapability(Direction side) {
        if (side == null)
            return itemHandler;

        return compactorItemHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }


    public CompactorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COMPACTOR_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new SimpleContainerData(2);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.strainers.compactor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new CompactorMenu(container, inventory, this.getBlockPos(), data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.setChanged();
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.put("inventory", this.itemHandler.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        super.loadAdditional(compoundTag, provider);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public void tick() {

        if (!level.isClientSide() && level.getGameTime() % 20 == 0) {

            for (int slot : INPUT_SLOTS) {
                ItemStack stack = itemHandler.getStackInSlot(slot);
                if (stack.isEmpty() || stack.getCount() < 4) continue;

                if (stack.getCount() >= 9 && tryCraft(stack, 3, slot)) {
                    return;
                }

                if (stack.getCount() >= 4 && tryCraft(stack, 2, slot)) {
                    return;
                }
            }
        }
    }

    private boolean tryCraft(ItemStack stack, int currentSize, int inputSlot) {
        assert level != null;
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            if (!(recipe instanceof ShapedRecipe shapedRecipe)) continue;

            if (shapedRecipe.getWidth() != currentSize || shapedRecipe.getHeight() != currentSize) continue;

            boolean matches = true;
            for (Ingredient ingredient : shapedRecipe.getIngredients()) {
                if (!ingredient.test(stack)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                ItemStack result = recipe.getResultItem(level.registryAccess()).copy();

                if (!result.isEmpty() && insertResult(result)) {
                    stack.shrink(currentSize * currentSize);
                    itemHandler.setStackInSlot(inputSlot, stack);
                    setChanged();
                    sync();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean insertResult(ItemStack result) {
        // Try merging with existing stacks first
        for (int slot : OUTPUT_SLOTS) {
            ItemStack outputStack = itemHandler.getStackInSlot(slot);

            if (!outputStack.isEmpty() &&
                    ItemStack.isSameItemSameComponents(outputStack, result) &&
                    outputStack.getCount() < outputStack.getMaxStackSize()) {

                int transferable = Math.min(result.getCount(),
                        outputStack.getMaxStackSize() - outputStack.getCount());

                outputStack.grow(transferable);
                result.shrink(transferable);
                itemHandler.setStackInSlot(slot, outputStack);

                if (result.isEmpty()) return true;
            }
        }

        // If not fully inserted, try empty slots
        for (int slot : OUTPUT_SLOTS) {
            if (itemHandler.getStackInSlot(slot).isEmpty()) {
                itemHandler.setStackInSlot(slot, result.copy());
                result.setCount(0);
                return true;
            }
        }

        return result.isEmpty();
    }

    private boolean insertInput(ItemStack stack) {
        // Try merging into existing stacks first
        for (int slot : INPUT_SLOTS) {
            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (!existing.isEmpty() &&
                    ItemStack.isSameItemSameComponents(existing, stack) &&
                    existing.getCount() < existing.getMaxStackSize()) {

                int transferable = Math.min(stack.getCount(),
                        existing.getMaxStackSize() - existing.getCount());

                existing.grow(transferable);
                stack.shrink(transferable);
                itemHandler.setStackInSlot(slot, existing);

                if (stack.isEmpty()) return true;
            }
        }

        // If not fully inserted, try empty slots
        for (int slot : INPUT_SLOTS) {
            if (itemHandler.getStackInSlot(slot).isEmpty()) {
                itemHandler.setStackInSlot(slot, stack.copy());
                stack.setCount(0);
                return true;
            }
        }

        return stack.isEmpty();
    }

    private ItemStack insertIntoInputs(ItemStack stack, boolean simulate) {
        ItemStack remaining = stack.copy();

        // Merge with existing stacks
        for (int slot : INPUT_SLOTS) {
            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (!existing.isEmpty() &&
                    ItemStack.isSameItemSameComponents(existing, remaining)) {

                int transferable = Math.min(remaining.getCount(),
                        existing.getMaxStackSize() - existing.getCount());

                if (transferable > 0) {
                    if (!simulate) {
                        existing.grow(transferable);
                        itemHandler.setStackInSlot(slot, existing);
                    }
                    remaining.shrink(transferable);
                    if (remaining.isEmpty()) return ItemStack.EMPTY;
                }
            }
        }

        // Fill empty slots
        for (int slot : INPUT_SLOTS) {
            if (itemHandler.getStackInSlot(slot).isEmpty()) {
                if (!simulate) {
                    itemHandler.setStackInSlot(slot, remaining.copy());
                }
                return ItemStack.EMPTY;
            }
        }

        return remaining; // Couldn’t insert all
    }

    private ItemStack insertIntoOutputs(ItemStack stack, boolean simulate) {
        ItemStack remaining = stack.copy();

        // Merge with existing stacks
        for (int slot : OUTPUT_SLOTS) {
            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (!existing.isEmpty() &&
                    ItemStack.isSameItemSameComponents(existing, remaining)) {

                int transferable = Math.min(remaining.getCount(),
                        existing.getMaxStackSize() - existing.getCount());

                if (transferable > 0) {
                    if (!simulate) {
                        existing.grow(transferable);
                        itemHandler.setStackInSlot(slot, existing);
                    }
                    remaining.shrink(transferable);
                    if (remaining.isEmpty()) return ItemStack.EMPTY;
                }
            }
        }

        // Fill empty slots
        for (int slot : OUTPUT_SLOTS) {
            if (itemHandler.getStackInSlot(slot).isEmpty()) {
                if (!simulate) {
                    itemHandler.setStackInSlot(slot, remaining.copy());
                }
                return ItemStack.EMPTY;
            }
        }

        return remaining;
    }

}

