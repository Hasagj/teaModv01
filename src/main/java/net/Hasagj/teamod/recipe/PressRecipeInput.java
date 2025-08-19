package net.hasagj.teamod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record PressRecipeInput(ItemStack input1,
                               ItemStack input2,
                               ItemStack input3,
                               ItemStack input4,
                               ItemStack input5,
                               ItemStack input6,
                               ItemStack input7,
                               ItemStack input8,
                               ItemStack input9) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {

        ItemStack stack;
        switch (i) {
            case 0 -> stack = this.input1;
            case 1 -> stack = this.input2;
            case 2 -> stack = this.input3;
            case 3 -> stack = this.input4;
            case 4 -> stack = this.input5;
            case 5 -> stack = this.input6;
            case 6 -> stack = this.input7;
            case 7 -> stack = this.input8;
            case 8 -> stack = this.input9;

            default -> throw new IllegalArgumentException("Recipe does not contain slot " + i);
        }

        return stack;
    }

    @Override
    public int size() {
        return 9;
    }
}