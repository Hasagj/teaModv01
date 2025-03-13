package net.hasagj.teamod.item;

import net.hasagj.teamod.TeaMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TeaMod.MOD_ID);


    public static final DeferredItem<Item> TEA_LEAF = ITEMS.register("tea_leaf",
            () -> new Item(new Item.Properties()));





    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
