package net.hasagj.teamod.item;

import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Set;

public enum ItemCategory {
    CUPS(List.of(
            ModItems.CUP_GREEN_TEA.get(),
            ModItems.CUP_BLACK_TEA.get(),
            ModItems.CUP_HIBISCUS_TEA.get(),
            ModItems.CUP_DAISY_TEA.get(),
            ModItems.CUP_PALE_TEA.get(),
            ModItems.CUP_PITCHER_TEA.get(),
            ModItems.CUP_CACTUS_TEA.get(),
            ModItems.CUP_CHORUS_TEA.get(),
            ModItems.CUP_ANCIENT_TEA.get(),
            ModItems.CUP_WANDERERS_TEA.get(),
            ModItems.CUP_CRIMSON_TEA.get(),
            ModItems.CUP_CORAL_TEA.get(),
            ModItems.CUP_SCULK_TEA.get(),
            ModItems.CUP_PERFECT_GREEN_TEA.get(),
            ModItems.CUP_PERFECT_BLACK_TEA.get()
    )),

    CHAKHAIS(List.of(
            ModItems.CHAKHAI_GREEN_TEA.get(),
            ModItems.CHAKHAI_BLACK_TEA.get(),
            ModItems.CHAKHAI_HIBISCUS_TEA.get(),
            ModItems.CHAKHAI_DAISY_TEA.get(),
            ModItems.CHAKHAI_PALE_TEA.get(),
            ModItems.CHAKHAI_PITCHER_TEA.get(),
            ModItems.CHAKHAI_CACTUS_TEA.get(),
            ModItems.CHAKHAI_CHORUS_TEA.get(),
            ModItems.CHAKHAI_ANCIENT_TEA.get(),
            ModItems.CHAKHAI_WANDERERS_TEA.get(),
            ModItems.CHAKHAI_CRIMSON_TEA.get(),
            ModItems.CHAKHAI_CORAL_TEA.get(),
            ModItems.CHAKHAI_SCULK_TEA.get(),
            ModItems.CHAKHAI_PERFECT_GREEN_TEA.get(),
            ModItems.CHAKHAI_PERFECT_BLACK_TEA.get()
    )),

    TEA_LEAVES(List.of(
            ModItems.GREEN_TEA_LEAVES.get(),
            ModItems.BLACK_TEA_LEAVES.get(),
            ModItems.DRIED_HIBISCUS_PETALS.get(),
            ModItems.DAISY_TEA_LEAVES.get(),
            ModItems.PALE_TEA_LEAVES.get(),
            ModItems.DRIED_PITCHER_PLANT.get(),
            ModItems.CACTUS_TEA_LEAVES.get(),
            ModItems.CHORUS_TEA_LEAVES.get(),
            ModItems.DRIED_TORCHFLOWER.get(),
            ModItems.STRANGE_PETALS.get(),
            ModItems.CRIMSON_TEA_LEAVES.get(),
            ModItems.CORAL_TEA_LEAVES.get(),
            ModItems.SCULK_BURGEON.get(),
            ModItems.PERFECT_GREEN_TEA_LEAVES.get(),
            ModItems.PERFECT_BLACK_TEA_LEAVES.get()
    ));

    private final List<Item> items;

    ItemCategory(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean contains(Item item) {
        return items.contains(item);
    }
}
