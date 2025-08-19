package net.hasagj.teamod.worldgen;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> TEA_BUSH_PLACED_KEY = registerKey("tea_bush_placed");
    public static final ResourceKey<PlacedFeature> HIBISCUS_PLACED_KEY = registerKey("hibiscus_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);


        register(context, HIBISCUS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.HIBISCUS_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ModBlocks.HIBISCUS_SAPLING.get()));

        register(context, TEA_BUSH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TEA_BUSH_KEY),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
