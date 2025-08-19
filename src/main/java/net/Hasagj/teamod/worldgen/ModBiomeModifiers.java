package net.hasagj.teamod.worldgen;
import net.hasagj.teamod.TeaMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_TEA_BUSH = registerKey("add_tea_bush");
    public static final ResourceKey<BiomeModifier> ADD_TREE_HIBISCUS = registerKey("add_tree_hibiscus");
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Example for individual Biomes!
        // context.register(ADD_BISMUTH_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
        //         HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.SAVANNA)),
        //         HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BISMUTH_ORE_PLACED_KEY)),
        //         GenerationStep.Decoration.UNDERGROUND_ORES));


        context.register(ADD_TEA_BUSH, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CHERRY_GROVE)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TEA_BUSH_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_TREE_HIBISCUS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SPARSE_JUNGLE)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.HIBISCUS_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, name));
    }
}
