package net.hasagj.teamod.item;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TeaMod.MOD_ID);

    public static final Supplier<CreativeModeTab> TEA_ITEMS_TAB = CREATIVE_MODE_TAB.register("tea_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DRIED_TEA_LEAF.get()))
                    .title(Component.translatable("creativetab.teamod.tea_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TEA_SEEDS);
                        output.accept(ModItems.TEA_LEAF);
                        output.accept(ModItems.LIGHTLY_DRIED_TEA_LEAF);
                        output.accept(ModItems.DRIED_TEA_LEAF);
                        output.accept(ModItems.GREEN_TEA_LEAVES);
                        output.accept(ModItems.BLACK_TEA_LEAVES);
                        output.accept(ModItems.HIBISCUS);
                        output.accept(ModItems.HIBISCUS_FLOWER);
                        output.accept(ModItems.DRIED_HIBISCUS_PETALS);


                    }).build());

    public static final Supplier<CreativeModeTab> DISHES_TAB = CREATIVE_MODE_TAB.register("dishes_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TEA_POT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "tea_items_tab"))
                    .title(Component.translatable("creativetab.teamod.dishes"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_CUP);
                        output.accept(ModItems.RAW_TEA_POT);
                        output.accept(ModItems.RAW_CHAKHAI);
                        output.accept(ModItems.CUP);
                        output.accept(ModItems.TEA_POT);
                        output.accept(ModItems.CHAKHAI);


                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}