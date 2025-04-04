package net.hasagj.teamod.screen;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.screen.custom.PressMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, TeaMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PressMenu>> PRESS_MENU =
            registerMenuType("press_menu", PressMenu::new);

    private static <T extends AbstractContainerMenu> @NotNull DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                                        IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}