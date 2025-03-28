package net.hasagj.teamod.block;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.custom.*;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TeaMod.MOD_ID);


    public static final DeferredBlock<Block> TEA_BUSH = BLOCKS.register("tea_bush",
            () -> new TeaBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final DeferredBlock<Block> HIBISCUS_BLOCK = BLOCKS.register("hibiscus_block",
            () -> new HibiscusBlock(BlockBehaviour.Properties.of().noOcclusion().noCollission().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> TEA_POT_BLOCK = BLOCKS.register("tea_pot_block",
            () -> new TeaPotBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));


    public static final DeferredBlock<Block> CHAKHAI_BLOCK = BLOCKS.register("chakhai_block",
            () -> new ChakhaiBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));
    public static final DeferredBlock<Block> CHAKHAI_GREEN_TEA_BLOCK = BLOCKS.register("chakhai_green_tea_block",
            () -> new ChakhaiGreenTeaBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));
    public static final DeferredBlock<Block> CHAKHAI_BLACK_TEA_BLOCK = BLOCKS.register("chakhai_black_tea_block",
            () -> new ChakhaiBlackTeaBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));
    public static final DeferredBlock<Block> CHAKHAI_HIBISCUS_TEA_BLOCK = BLOCKS.register("chakhai_hibiscus_tea_block",
            () -> new ChakhaiHibiscusTeaBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.DECORATED_POT)));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlock(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
