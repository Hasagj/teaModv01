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
                        output.accept(ModItems.PERFECT_TEA_LEAF);
                        output.accept(ModItems.LIGHTLY_DRIED_TEA_LEAF);
                        output.accept(ModItems.PERFECT_LD_TEA_LEAF);
                        output.accept(ModItems.DRIED_TEA_LEAF);
                        output.accept(ModItems.PERFECT_D_TEA_LEAF);
                        output.accept(ModItems.CRIMSON_FRUIT);
                        output.accept(ModItems.CRIMSON_TEA_LEAF);
                        output.accept(ModBlocks.HIBISCUS_SAPLING.get());
                        output.accept(ModBlocks.FLOWERLESS_LEAVES.get());
                        output.accept(ModBlocks.HIBISCUS_LEAVES.get());
                        output.accept(ModItems.HIBISCUS_FLOWER);
                        output.accept(ModItems.DRIED_DAISY);
                        output.accept(ModItems.PITCHER_TURNIP);
                        output.accept(ModItems.GREEN_TEA_LEAVES);
                        output.accept(ModItems.PERFECT_GREEN_TEA_LEAVES);
                        output.accept(ModItems.BLACK_TEA_LEAVES);
                        output.accept(ModItems.PERFECT_BLACK_TEA_LEAVES);
                        output.accept(ModItems.DRIED_HIBISCUS_PETALS);
                        output.accept(ModItems.DAISY_TEA_LEAVES);
                        output.accept(ModItems.PALE_TEA_LEAVES);
                        output.accept(ModItems.DRIED_PITCHER_PLANT);
                        output.accept(ModItems.CACTUS_TEA_LEAVES);
                        output.accept(ModItems.CHORUS_TEA_LEAVES);
                        output.accept(ModItems.DRIED_TORCHFLOWER);
                        output.accept(ModItems.STRANGE_PETALS);
                        output.accept(ModItems.CRIMSON_TEA_LEAVES);
                        output.accept(ModItems.CORAL_TEA_LEAVES);
                        output.accept(ModItems.SCULK_BURGEON);
                        output.accept(ModItems.BOILED_WATER);



                    }).build());

    public static final Supplier<CreativeModeTab> DISHES_TAB = CREATIVE_MODE_TAB.register("dishes_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TEA_POT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "tea_items_tab"))
                    .title(Component.translatable("creativetab.teamod.dishes"))
                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(ModItems.TEA_HANDBOOK);
                        output.accept(ModItems.RAW_CUP);
                        output.accept(ModItems.RAW_TEA_POT);
                        output.accept(ModItems.RAW_CHAKHAI);
                        output.accept(ModItems.CUP);
                        output.accept(ModItems.TEA_POT);
                        output.accept(ModItems.CHAKHAI);
                        output.accept(ModItems.BROKEN_NETHER_CUP);
                        output.accept(ModItems.NETHER_CUP);
                        output.accept(ModItems.BROKEN_NAUTILUS_CUP);
                        output.accept(ModItems.NAUTILUS_CUP);
                        output.accept(ModItems.BROKEN_REINFORCED_CUP);
                        output.accept(ModItems.REINFORCED_CUP);
                        output.accept(ModBlocks.PRESS.get());
                        output.accept(ModItems.MOON_RAVEN);
                        output.accept(ModItems.FRAGMENT_HEAD);
                        output.accept(ModItems.FRAGMENT_BODY);
                        output.accept(ModItems.FRAGMENT_LEGS);
                        output.accept(ModItems.THE_WISE_MAN);
                        output.accept(ModItems.CLAY_COD);
                        output.accept(ModItems.CLAY_SALMON);
                        output.accept(ModItems.PISCES);
                        output.accept(ModItems.KAOLIN);
                        output.accept(ModItems.YIXING_CLAY);


                    }).build());
    public static final Supplier<CreativeModeTab> CUPS_TAB = CREATIVE_MODE_TAB.register("cups_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CUP.get()))
                    .title(Component.translatable("creativetab.teamod.cups"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CHAKHAI_BLACK_TEA);
                        output.accept(ModItems.CUP_BLACK_TEA);
                        output.accept(ModItems.CHAKHAI_PERFECT_BLACK_TEA);
                        output.accept(ModItems.CUP_PERFECT_BLACK_TEA);
                        output.accept(ModItems.CHAKHAI_GREEN_TEA);
                        output.accept(ModItems.CUP_GREEN_TEA);
                        output.accept(ModItems.CHAKHAI_PERFECT_GREEN_TEA);
                        output.accept(ModItems.CUP_PERFECT_GREEN_TEA);
                        output.accept(ModItems.CHAKHAI_HIBISCUS_TEA);
                        output.accept(ModItems.CUP_HIBISCUS_TEA);
                        output.accept(ModItems.CHAKHAI_DAISY_TEA);
                        output.accept(ModItems.CUP_DAISY_TEA);
                        output.accept(ModItems.CHAKHAI_PALE_TEA);
                        output.accept(ModItems.CUP_PALE_TEA);
                        output.accept(ModItems.CHAKHAI_PITCHER_TEA);
                        output.accept(ModItems.CUP_PITCHER_TEA);
                        output.accept(ModItems.CHAKHAI_CACTUS_TEA);
                        output.accept(ModItems.CUP_CACTUS_TEA);
                        output.accept(ModItems.CHAKHAI_CHORUS_TEA);
                        output.accept(ModItems.CUP_CHORUS_TEA);
                        output.accept(ModItems.CHAKHAI_ANCIENT_TEA);
                        output.accept(ModItems.CUP_ANCIENT_TEA);
                        output.accept(ModItems.CHAKHAI_WANDERERS_TEA);
                        output.accept(ModItems.CUP_WANDERERS_TEA);
                        output.accept(ModItems.CHAKHAI_CRIMSON_TEA);
                        output.accept(ModItems.CUP_CRIMSON_TEA);
                        output.accept(ModItems.NETHER_CUP_CRIMSON_TEA);
                        output.accept(ModItems.CHAKHAI_CORAL_TEA);
                        output.accept(ModItems.CUP_CORAL_TEA);
                        output.accept(ModItems.NAUTILUS_CUP_CORAL_TEA);
                        output.accept(ModItems.CHAKHAI_SCULK_TEA);
                        output.accept(ModItems.CUP_SCULK_TEA);
                        output.accept(ModItems.REINFORCED_CUP_SCULK_TEA);

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}