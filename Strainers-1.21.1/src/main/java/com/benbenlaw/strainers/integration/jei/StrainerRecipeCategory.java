package com.benbenlaw.strainers.integration.jei;

import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.ModBlocks;
import com.benbenlaw.strainers.recipe.StrainerRecipe;
import com.benbenlaw.strainers.util.ModTags;
import com.benbenlaw.strainers.util.StrainersIngredientDurations;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IScrollGridWidgetFactory;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StrainerRecipeCategory implements IRecipeCategory<StrainerRecipeDisplay> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "strainer");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Strainers.MOD_ID, "textures/gui/jei_strainer.png");

    public static final RecipeType<StrainerRecipeDisplay> RECIPE_TYPE = RecipeType.create(Strainers.MOD_ID, "strainer", StrainerRecipeDisplay.class);


    private final IDrawable background;
    private final IDrawable icon;
    private final IScrollGridWidgetFactory<?> scrollGridWidgetFactory;

    public StrainerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 155, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WOODEN_STRAINER.get()));
        this.scrollGridWidgetFactory = helper.createScrollGridFactory(5, 3);
        this.scrollGridWidgetFactory.setPosition(50, 1);
    }

    @Override
    public RecipeType<StrainerRecipeDisplay> getRecipeType() {
        return JEIStrainersPlugin.STRAINER;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Strainers");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public @Nullable ResourceLocation getRegistryName(StrainerRecipeDisplay recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(StrainerRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StrainerRecipeDisplay recipe, IFocusGroup focusGroup) {

        Fluid fluidState = recipe.aboveBlock().getFluidState().getType();

        if (fluidState == Fluids.EMPTY) {
            builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).addItemStack(recipe.aboveBlock().getBlock().asItem().getDefaultInstance())
                    .setCustomRenderer(VanillaTypes.ITEM_STACK, new IIngredientRenderer<>() {
                        @Override
                        public void render(GuiGraphics guiGraphics, ItemStack stack) {
                            JEIBlockRenderHelper.renderBlock(guiGraphics, recipe.aboveBlock(), 1,12, 0.60f);
                        }

                        @Override
                        public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
                            List<Component> tooltip = new ArrayList<>();

                            tooltip.add(recipe.aboveBlock().getBlock().getName());

                            BlockState targetState = recipe.aboveBlock();
                            BlockState defaultState = targetState.getBlock().defaultBlockState();

                            for (Map.Entry<Property<?>, Comparable<?>> entry : targetState.getValues().entrySet()) {
                                Property<?> property = entry.getKey();
                                Comparable<?> recipeValue = entry.getValue();
                                Comparable<?> defaultValue = defaultState.getValue(property);

                                // Only add to tooltip if it differs from the default
                                if (!recipeValue.equals(defaultValue)) {
                                    String key = property.getName();
                                    String value = recipeValue.toString();
                                    tooltip.add(Component.literal(key + ": " + value));
                                }
                            }
                            return tooltip;
                        }
                    });
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).addFluidStack(fluidState)
                    .setFluidRenderer(1000, true, 16, 16);
        }


        builder.addSlot(RecipeIngredientRole.INPUT, 2, 20).addIngredients(recipe.input())
                .addRichTooltipCallback((slotView, tooltip) -> {
                    Optional<ItemStack> currentItem = Optional.of(slotView.getDisplayedItemStack().orElse(ItemStack.EMPTY));
                    if (currentItem.get().is(ModTags.Items.NOT_CONSUMED)) {
                        tooltip.add(Component.translatable("block.strainer.jei.not_consumed").withStyle(ChatFormatting.GOLD));
                    }
                });

        builder.addSlot(RecipeIngredientRole.CATALYST, 2, 38).addIngredients(recipe.mesh());

        for (var result : recipe.getChanceResults()) {
            builder.addSlotToWidget(RecipeIngredientRole.OUTPUT, this.scrollGridWidgetFactory)
                    .addItemStack(result.stack())
                    .addRichTooltipCallback((slotView, tooltip) -> {
                        double baseChance = result.chance();
                        int asPercent = Math.round((float) (baseChance * 100));

                        tooltip.add(Component.translatable("block.strainer.jei.chance")
                                .append(String.valueOf(asPercent))
                                .append("%").withStyle(ChatFormatting.GOLD));
                    });
        }
    }

    @Override
    public void draw(StrainerRecipeDisplay recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 22, 20, 0, 0, 26, 19)) {
            int duration = StrainersIngredientDurations.getDuration(recipe.input().getItems()[0]);
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("jei.strainer.duration", duration), (int) mouseX, (int) mouseY);
        }


    }

}
