package net.hasagj.teamod.block;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.custom.*;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TeaMod.MOD_ID);


    public static final DeferredBlock<Block> TEA_BUSH = registerBlock("tea_bush",
            (properties) -> new TeaBushBlock(properties.mapColor(MapColor.PLANT).strength(0.2F)
                    .randomTicks().sound(SoundType.CROP).noOcclusion().pushReaction(PushReaction.DESTROY).noCollission()));
    public static final DeferredBlock<Block> CRIMSON_BUSH = registerBlock("crimson_bush",
            (properties) -> new CrimsonBushBlock(properties.mapColor(MapColor.CRIMSON_NYLIUM).strength(0.2F)
                    .randomTicks().sound(SoundType.CROP).noOcclusion().pushReaction(PushReaction.DESTROY).noCollission()));
    public static final DeferredBlock<Block> HIBISCUS_SAPLING = registerBlock("hibiscus_sapling",
            (properties) -> new ModSaplingBlock(ModTreeGrowers.HIBISCUS, properties.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
                    .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY).noCollission(), () -> Blocks.GRASS_BLOCK));
    public static final DeferredBlock<Block> HIBISCUS_LEAVES = registerBlock("hibiscus_leaves",
            (properties) -> new UntintedParticleLeavesBlock(0.01F, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 00, 80, 00), properties.mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.CHERRY_LEAVES)
                    .noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> FLOWERLESS_LEAVES = registerBlock("flowerless_leaves",
            (properties) -> new UntintedParticleLeavesBlock(0.01F, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 00, 80, 00), properties.mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.CHERRY_LEAVES)
                    .noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> TEA_POT_BLOCK = registerBlock("tea_pot_block",
            (properties) -> new TeaPotBlock(properties.noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));


    public static final DeferredBlock<Block> CHAKHAI_BLOCK = registerBlock("chakhai_block",
            (properties) -> new ChakhaiBlockTest(properties.noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));

    public static final DeferredBlock<Block> PRESS = registerBlock("press", PressBlock::new);
    public static final DeferredBlock<Block> CAULDRON_ON_FIRE = registerBlock("cauldron_on_fire",
            (properties) -> new CauldronOnFire(properties.noOcclusion().requiresCorrectToolForDrops().strength(10F)));



    public static final DeferredBlock<Block> MOON_RAVEN_BLOCK = BLOCKS.registerBlock("moon_raven",
            MoonRavenBlock::new);
    public static final DeferredBlock<Block> THE_WISE_MAN_BLOCK = BLOCKS.registerBlock("the_wise_man",
            TheWiseManBlock::new);
    public static final DeferredBlock<Block> PISCES_BLOCK = BLOCKS.registerBlock("pisces",
            PiscesBlock::new);



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
