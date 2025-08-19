package net.hasagj.teamod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record PressRecipe(Ingredient inputItem1,
                          Ingredient inputItem2,
                          Ingredient inputItem3,
                          Ingredient inputItem4,
                          Ingredient inputItem5,
                          Ingredient inputItem6,
                          Ingredient inputItem7,
                          Ingredient inputItem8,
                          Ingredient inputItem9,
                          ItemStack output) implements Recipe<PressRecipeInput> {

    public List<Ingredient> getIngredients() {
        List<Ingredient> list = NonNullList.create();
        list.add(inputItem1);
        list.add(inputItem2);
        list.add(inputItem3);
        list.add(inputItem4);
        list.add(inputItem5);
        list.add(inputItem6);
        list.add(inputItem7);
        list.add(inputItem8);
        list.add(inputItem9);
        return list;
    }

    @Override
    public boolean matches(PressRecipeInput pressRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem1.test(pressRecipeInput.getItem(0))
                && inputItem2.test(pressRecipeInput.getItem(1))
                && inputItem3.test(pressRecipeInput.getItem(2))
                && inputItem4.test(pressRecipeInput.getItem(3))
                && inputItem5.test(pressRecipeInput.getItem(4))
                && inputItem6.test(pressRecipeInput.getItem(5))
                && inputItem7.test(pressRecipeInput.getItem(6))
                && inputItem8.test(pressRecipeInput.getItem(7))
                && inputItem9.test(pressRecipeInput.getItem(8));
    }

    @Override
    public ItemStack assemble(PressRecipeInput pressRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }


    @Override
    public RecipeSerializer<? extends Recipe<PressRecipeInput>> getSerializer() {
        return ModRecipes.PRESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<PressRecipeInput>> getType() {
        return ModRecipes.PRESS_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(getIngredients());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<PressRecipe> {
        public static final MapCodec<PressRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient1").forGetter(PressRecipe::inputItem1),
                Ingredient.CODEC.fieldOf("ingredient2").forGetter(PressRecipe::inputItem2),
                Ingredient.CODEC.fieldOf("ingredient3").forGetter(PressRecipe::inputItem3),
                Ingredient.CODEC.fieldOf("ingredient4").forGetter(PressRecipe::inputItem4),
                Ingredient.CODEC.fieldOf("ingredient5").forGetter(PressRecipe::inputItem5),
                Ingredient.CODEC.fieldOf("ingredient6").forGetter(PressRecipe::inputItem6),
                Ingredient.CODEC.fieldOf("ingredient7").forGetter(PressRecipe::inputItem7),
                Ingredient.CODEC.fieldOf("ingredient8").forGetter(PressRecipe::inputItem8),
                Ingredient.CODEC.fieldOf("ingredient9").forGetter(PressRecipe::inputItem9),

                ItemStack.CODEC.fieldOf("result").forGetter(PressRecipe::output)
        ).apply(inst, PressRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PressRecipe> STREAM_CODEC =
                StreamCodec.of((buf, recipe) -> {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem1());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem2());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem3());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem4());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem5());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem6());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem7());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem8());
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem9());
                            ItemStack.STREAM_CODEC.encode(buf, recipe.output());
                        },
                        buf -> new PressRecipe(
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                                ItemStack.STREAM_CODEC.decode(buf)
                        )
                );

        @Override
        public MapCodec<PressRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PressRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
