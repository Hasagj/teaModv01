package net.hasagj.teamod.recipe;

import net.hasagj.teamod.TeaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, TeaMod.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, TeaMod.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PressRecipe>> PRESS_SERIALIZER =
            SERIALIZERS.register("press", PressRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PressRecipe>> PRESS_TYPE =
            TYPES.register("press", () -> new RecipeType<PressRecipe>() {
                @Override
                public String toString() {
                    return "press";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
