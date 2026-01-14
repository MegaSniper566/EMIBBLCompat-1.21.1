package com.benbenlaw.strainers.screen;

import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.screen.custom.CompactorMenu;
import com.benbenlaw.strainers.screen.custom.WoodenStrainerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, Strainers.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WoodenStrainerMenu>> WOODEN_STRAINER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<CompactorMenu>> COMPACTOR_MENU;


    static {
        WOODEN_STRAINER_MENU = MENUS.register("wooden_strainer_menu", () ->
                IMenuTypeExtension.create(WoodenStrainerMenu::new));

        COMPACTOR_MENU = MENUS.register("compactor_menu", () ->
                IMenuTypeExtension.create(CompactorMenu::new));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }



}
