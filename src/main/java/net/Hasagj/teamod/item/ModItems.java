package net.hasagj.teamod.item;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TeaMod.MOD_ID);


    public static final DeferredItem<Item> TEA_LEAF = ITEMS.register("tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTLY_DRIED_TEA_LEAF = ITEMS.register("lightly_dried_tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRIED_TEA_LEAF = ITEMS.register("dried_tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACK_TEA_LEAVES = ITEMS.register("black_tea_leaves",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> GREEN_TEA_LEAVES = ITEMS.register("green_tea_leaves",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> RAW_CUP = ITEMS.register("raw_cup",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CUP = ITEMS.register("cup",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> TEA_SEEDS = ITEMS.register("tea_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TEA_BUSH.get(), new Item.Properties()));
    public static final DeferredItem<Item> TEA_POT = ITEMS.register("tea_pot",
            () -> new ItemNameBlockItem(ModBlocks.TEA_POT_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RAW_TEA_POT = ITEMS.register("raw_tea_pot",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI = ITEMS.register("chakhai",
            () -> new ItemNameBlockItem(ModBlocks.CHAKHAI_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RAW_CHAKHAI = ITEMS.register("raw_chakhai",
            () -> new Item(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
