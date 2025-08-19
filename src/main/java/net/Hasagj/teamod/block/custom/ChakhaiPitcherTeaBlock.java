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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChakhaiPitcherTeaBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<ChakhaiPitcherTeaBlock> CODEC = simpleCodec(ChakhaiPitcherTeaBlock::new);
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);
    public static final IntegerProperty IS_TEA_INSIDE = IntegerProperty.create("is_tea_inside", 0, 1);
    public ChakhaiPitcherTeaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(IS_TEA_INSIDE, 1)));
    }
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        if (state.getValue(IS_TEA_INSIDE) == 0) {
            return new ItemStack((ItemLike) ModItems.CHAKHAI);
        }else {return new ItemStack((ItemLike) ModItems.CHAKHAI_PITCHER_TEA);}
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean flag = false;
        Item item = stack.getItem();
        if ((int)state.getValue(IS_TEA_INSIDE) == 1) {
            if (stack.is(ModItems.CUP)) {
                stack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack((ItemLike) ModItems.CUP_PITCHER_TEA));
                } else if (!player.getInventory().add(new ItemStack((ItemLike) ModItems.CUP_PITCHER_TEA))) {
                    player.drop(new ItemStack((ItemLike) ModItems.CUP_PITCHER_TEA), false);
                }
                flag = true;
                level.setBlockAndUpdate(pos, state.setValue(IS_TEA_INSIDE, 0));
            }
        }

        if (!level.isClientSide() && flag) {
            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{IS_TEA_INSIDE, FACING});
    }

}