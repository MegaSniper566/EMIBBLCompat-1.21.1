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
import com.benbenlaw.strainers.screen.custom.WoodenStrainerMenu;
import com.benbenlaw.strainers.util.ModTags;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Blocks;
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
import java.util.stream.Collectors;

public class WoodenStrainerBlockEntity extends SyncableBlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {


    private final ItemStackHandler itemHandler = new ItemStackHandler(38) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            if (slot == UPGRADE_SLOT_1 || slot == UPGRADE_SLOT_2 || slot == UPGRADE_SLOT_3 || slot == MESH_SLOT) {
                return 1;
            }
            return super.getStackLimit(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == UPGRADE_SLOT_1 || slot == UPGRADE_SLOT_2 || slot == UPGRADE_SLOT_3 || slot == MESH_SLOT) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }
    };
    private FakePlayer fakePlayer;

    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 220;
    public static final int INPUT_SLOT = 0;
    public static final int MESH_SLOT = 1;
    public static final int UPGRADE_SLOT_1 = 2; // Upgrade Slot Opolis Utilities speed upgrades
    public static final int UPGRADE_SLOT_2 = 3; // Mesh damage upgrades
    public static final int UPGRADE_SLOT_3 = 4; // Upgrade Slot Strainers chance increase upgrades

    private Optional<RecipeHolder<StrainerRecipe>> cachedRecipe = Optional.empty();
    private ItemStack lastInput = ItemStack.EMPTY;
    private ItemStack lastMesh = ItemStack.EMPTY;
    private BlockState lastAboveBlock = Blocks.AIR.defaultBlockState();
    public String errorMessage = "";


    //UPGRADE VALUES
    public double outputChanceIncrease = 0.0;
    public static final int[] OUTPUT_SLOTS;

    static {
        OUTPUT_SLOTS = new int[33];
        for (int i = 0; i < OUTPUT_SLOTS.length; i++) {
            OUTPUT_SLOTS[i] = 5 + i;
        }
    }

    private final IItemHandler strainerItemHandler = new InputOutputItemHandler(
            itemHandler,
            this::isInputSlot,
            this::isOutputSlot
    );

    private boolean isInputSlot(int slot, ItemStack itemStack) {

        if (slot == MESH_SLOT) {
            return itemStack.is(ModTags.Items.MESHES);
        }

        else if (slot == INPUT_SLOT) {
            return !itemStack.is(ModTags.Items.MESHES);
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

        return strainerItemHandler;
    }


    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }


    public WoodenStrainerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_STRAINER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> WoodenStrainerBlockEntity.this.progress;
                    case 1 -> WoodenStrainerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> WoodenStrainerBlockEntity.this.progress = value;
                    case 1 -> WoodenStrainerBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };

        if (level instanceof ServerLevel serverLevel) {
            this.fakePlayer = createFakePlayer(serverLevel);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.strainers.wooden_strainer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new WoodenStrainerMenu(container, inventory, this.getBlockPos(), data);
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
        compoundTag.putInt("strainer.progress", progress);
        compoundTag.putInt("strainer.maxProgress", maxProgress);
        compoundTag.putDouble("strainer.outputChanceIncrease", outputChanceIncrease);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("strainer.progress");
        maxProgress = compoundTag.getInt("strainer.maxProgress");
        outputChanceIncrease = compoundTag.getDouble("strainer.outputChanceIncrease");
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

        if (this.fakePlayer == null && level instanceof ServerLevel serverLevel) {
            this.fakePlayer = createFakePlayer(serverLevel);
        }

        sync();

        assert level != null;
        if (!level.isClientSide()) {

            updateCachedRecipe();
            Optional<RecipeHolder<StrainerRecipe>> match = cachedRecipe;

            if (match.isPresent()) {
                maxProgress = StrainersIngredientDurations.getDuration(itemHandler.getStackInSlot(INPUT_SLOT));
                maxProgress = getNewMaxProgress(maxProgress);

                StrainerRecipe currentRecipe = match.get().value();

                if (hasCorrectBlockAbove(currentRecipe)) {
                    List<ChanceResult> resultsToRoll = getAllCombinedResults(
                            itemHandler.getStackInSlot(INPUT_SLOT),
                            itemHandler.getStackInSlot(MESH_SLOT)
                    );

                    List<ItemStack> results = resultsToRoll.stream()
                            .map(r -> r.rollOutput(level.random))
                            .filter(r -> !r.isEmpty())
                            .toList();

                    if (!canFitResults(results)) {
                        if (!"block.cloche.error.output_full".equals(errorMessage)) {
                            errorMessage = "block.cloche.error.output_full";
                            sync();
                        }
                        return;
                    }

                    progress++;

                    if (progress >= maxProgress) {
                        resetProgress();
                        fillOutputSlots(results);
                        sync();
                    }
                }
            } else {
                resetProgress();
            }
        }
    }

    public int getNewMaxProgress(int adjustedMaxProgress) {

        int newMaxProgress = adjustedMaxProgress;

        ItemStack upgradeSlot1 = itemHandler.getStackInSlot(UPGRADE_SLOT_1);
        ItemStack upgradeSlot2 = itemHandler.getStackInSlot(UPGRADE_SLOT_2);
        ItemStack upgradeSlot3 = itemHandler.getStackInSlot(UPGRADE_SLOT_3);

        ItemStack[] upgradeSlots = {upgradeSlot1, upgradeSlot2, upgradeSlot3};

        for (ItemStack upgradeSlot : upgradeSlots) {
            if (upgradeSlot.isEmpty()) continue;

            if (upgradeSlot.getItem() instanceof UpgradeItem upgradeItem) {
                newMaxProgress -= upgradeItem.getSpeedReduction();
            }
        }
        return Math.max(20, newMaxProgress);
    }

    public void fillOutputSlots(List<ItemStack> results) {

        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);

        if (!inputStack.is(ModTags.Items.NOT_CONSUMED)) {
            inputStack.shrink(1);
        }

        ItemStack meshItem = this.itemHandler.getStackInSlot(MESH_SLOT);
        if (meshItem.isDamageableItem()) {
            meshItem.hurtAndBreak(1, fakePlayer, fakePlayer.getEquipmentSlotForItem(meshItem));
            if (meshItem.getCount() <= 0) {
                this.itemHandler.setStackInSlot(MESH_SLOT, ItemStack.EMPTY);
            }
        }

        for (ItemStack result : results) {
            if (result.isEmpty()) continue;

            for (int outputSlot : OUTPUT_SLOTS) {
                if (result.isEmpty()) break;

                ItemStack slotStack = itemHandler.getStackInSlot(outputSlot);

                if (!slotStack.isEmpty() && ItemStack.isSameItem(slotStack, result)) {
                    int maxStackSize = slotStack.getMaxStackSize();
                    int spaceLeft = maxStackSize - slotStack.getCount();

                    if (spaceLeft > 0) {
                        int toAdd = Math.min(spaceLeft, result.getCount());
                        slotStack.grow(toAdd);
                        result.shrink(toAdd);
                    }
                }
            }

            for (int outputSlot : OUTPUT_SLOTS) {
                if (result.isEmpty()) break;

                ItemStack slotStack = itemHandler.getStackInSlot(outputSlot);

                if (slotStack.isEmpty()) {
                    itemHandler.setStackInSlot(outputSlot, result.copy());
                    result.setCount(0);
                    break;
                }
            }
        }
    }

    private List<ChanceResult> getAllCombinedResults(ItemStack inputStack, ItemStack meshStack) {
        assert level != null;
        var recipeManager = level.getRecipeManager();

        List<StrainerRecipe> matchingRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.STRAINER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.input().test(inputStack))
                .filter(this::hasCorrectBlockAbove)
                .toList();

        List<ChanceResult> combinedResults = new ArrayList<>();
        for (StrainerRecipe recipe : matchingRecipes) {
            for (MeshChanceResult meshChance : recipe.getRollResults()) {
                if (meshChance == MeshChanceResult.EMPTY) continue;
                if (!meshChance.mesh().isEmpty() && !meshChance.mesh().test(meshStack)) continue;

                ChanceResult original = meshChance.chanceResult();
                ChanceResult booster = applyUpgradeBoost(original);

                combinedResults.add(booster);
            }
        }

        return combinedResults;
    }

    private ChanceResult applyUpgradeBoost(ChanceResult original) {

        double newChance = original.chance();

        ItemStack upgradeSlot1 = itemHandler.getStackInSlot(UPGRADE_SLOT_1);
        ItemStack upgradeSlot2 = itemHandler.getStackInSlot(UPGRADE_SLOT_2);
        ItemStack upgradeSlot3 = itemHandler.getStackInSlot(UPGRADE_SLOT_3);

        ItemStack[] upgradeSlots = {upgradeSlot1, upgradeSlot2, upgradeSlot3};

        for (ItemStack upgradeSlot : upgradeSlots) {
            if (upgradeSlot.isEmpty()) continue;

            if (upgradeSlot.getItem() instanceof UpgradeItem upgradeItem) {
                newChance += upgradeItem.getOutputIncrease();
            }
        }

        double boostedChance = original.chance() + Math.min(1, newChance);
        return new ChanceResult(original.stack(), (float) boostedChance);
    }

    private boolean hasCorrectBlockAbove(StrainerRecipe recipe) {
        return recipe.getBlockAbove() == getBlockAbove();
    }

    private void updateCachedRecipe() {
        if (inputsChanged()) {
            RecipeInput inventory = new StrainerRecipeInput(itemHandler, worldPosition);

            cachedRecipe = level.getRecipeManager().getRecipeFor(StrainerRecipe.Type.INSTANCE, inventory, level);

            // Update last inputs
            lastInput = itemHandler.getStackInSlot(INPUT_SLOT).copy();
            lastMesh = itemHandler.getStackInSlot(MESH_SLOT).copy();
            lastAboveBlock = getBlockAbove();
        }
    }

    public BlockState getBlockAbove() {
        BlockPos blockPos = this.worldPosition.above();
        assert level != null;

        BlockState state = level.getBlockState(blockPos);
        BlockEntity be = level.getBlockEntity(blockPos);

        if (be instanceof StrainerTankBlockEntity tank) {
            Fluid fluid = tank.FLUID_TANK.getFluid().getFluid();
            if (!fluid.getFluidType().isAir()) {
                return fluid.defaultFluidState().createLegacyBlock();
            }
        }

        return state;
    }

    private boolean inputsChanged() {
        return !ItemStack.matches(itemHandler.getStackInSlot(INPUT_SLOT), lastInput) ||
                !ItemStack.matches(itemHandler.getStackInSlot(MESH_SLOT), lastMesh) ||
                getBlockAbove() != lastAboveBlock;
    }

    public boolean canFitResults(List<ItemStack> results) {
        for (ItemStack result : results) {
            int remainingToFit = result.getCount();
            for (int outputSlot : OUTPUT_SLOTS) {
                ItemStack slotStack = itemHandler.getStackInSlot(outputSlot);
                if (slotStack.isEmpty()) {
                    remainingToFit = 0;
                    break;
                } else if (ItemStack.isSameItem(slotStack, result)) {
                    int maxStackSize = slotStack.getMaxStackSize();
                    int spaceLeft = maxStackSize - slotStack.getCount();

                    if (spaceLeft > 0) {
                        remainingToFit -= spaceLeft;
                        if (remainingToFit <= 0) {
                            break;
                        }
                    }
                }
            }
            if (remainingToFit > 0) {
                return false;
            }
        }
        return true;
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = Integer.MAX_VALUE;
    }

    private FakePlayer createFakePlayer(ServerLevel level) {
        return new FakePlayer(level, new GameProfile(UUID.randomUUID(), "Strainer"));
    }
}