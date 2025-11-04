package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChakhaiBlockTest extends HorizontalDirectionalBlock {
    public static final MapCodec<ChakhaiBlockTest> CODEC = simpleCodec(ChakhaiBlockTest::new);
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);
    public static final IntegerProperty IS_TEA_INSIDE = IntegerProperty.create("is_tea_inside", 0, 15);

    public ChakhaiBlockTest(Properties properties) {
        super(properties);
    }


    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        List<Item> tea_list = List.of(ModItems.CHAKHAI.get(), ModItems.CHAKHAI_GREEN_TEA.get(), ModItems.CHAKHAI_BLACK_TEA.get(), ModItems.CHAKHAI_HIBISCUS_TEA.get(), ModItems.CHAKHAI_DAISY_TEA.get(), ModItems.CHAKHAI_PALE_TEA.get(), ModItems.CHAKHAI_PITCHER_TEA.get(), ModItems.CHAKHAI_CACTUS_TEA.get(), ModItems.CHAKHAI_CHORUS_TEA.get());
        return new ItemStack(tea_list.get(state.getValue(IS_TEA_INSIDE)));

    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tick, BlockPos pos,
                                  Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState(); // уничтожаем блок
        }
        return super.updateShape(state, level, tick, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean flag = false;
        Item item = stack.getItem();
        if (state.getValue(IS_TEA_INSIDE) != 0 && stack.is(ModItems.CUP)) {
            flag = fillCup(stack, state, level, pos, player, hand);
        }

        if (!level.isClientSide() && flag) {
            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

    }

    private boolean fillCup(ItemStack cup, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        List<Item> tea_list = List.of(ModItems.CUP_GREEN_TEA.get(), ModItems.CUP_BLACK_TEA.get(), ModItems.CUP_HIBISCUS_TEA.get(), ModItems.CUP_DAISY_TEA.get(), ModItems.CUP_PALE_TEA.get(), ModItems.CUP_PITCHER_TEA.get(), ModItems.CUP_CACTUS_TEA.get(), ModItems.CUP_CHORUS_TEA.get(), ModItems.CUP_ANCIENT_TEA.get(), ModItems.CUP_WANDERERS_TEA.get());
        cup.shrink(1);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (cup.isEmpty()) {
            player.setItemInHand(hand, new ItemStack(tea_list.get(state.getValue(IS_TEA_INSIDE) - 1)));
        } else if (!player.getInventory().add(new ItemStack(tea_list.get(state.getValue(IS_TEA_INSIDE) - 1)))) {
            player.drop(new ItemStack(tea_list.get(state.getValue(IS_TEA_INSIDE) - 1)), false);
        }
        level.setBlockAndUpdate(pos, state.setValue(IS_TEA_INSIDE, 0));
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{IS_TEA_INSIDE, FACING});
    }

}