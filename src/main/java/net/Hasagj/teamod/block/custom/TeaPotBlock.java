package net.hasagj.teamod.block.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.item.ItemCategory;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

public class TeaPotBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<TeaPotBlock> CODEC = simpleCodec(TeaPotBlock::new);
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);
    public static final IntegerProperty WHAT_LEAVES_INSIDE = IntegerProperty.create("what_leaves_inside", 0, 15);
    public static final IntegerProperty WHAT_TEA_INSIDE = IntegerProperty.create("what_tea_inside", 0, 15);
    public static final BooleanProperty IS_WATER_INSIDE = BooleanProperty.create("is_water_inside");
    public TeaPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WHAT_LEAVES_INSIDE, 0).setValue(WHAT_TEA_INSIDE, 0).setValue(IS_WATER_INSIDE, false));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        return new ItemStack(ModItems.TEA_POT.get());

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
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!state.getValue(IS_WATER_INSIDE)) {
            if (stack.is(ModItems.BOILED_WATER.get())) {
                stack.consume(1, player);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                } else if (!player.getInventory().add(new ItemStack(Items.BUCKET))) {
                    player.drop(new ItemStack(Items.BUCKET), false);
                }
                BlockState newState = state.setValue(IS_WATER_INSIDE, true);
                level.setBlock(pos, newState, 3);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));

            }
        }

        if (state.getValue(WHAT_LEAVES_INSIDE) == 0) {
            Item item = stack.getItem();
            checkLeaves(stack, state, level, pos, player);
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }


        }
        if (state.getValue(IS_WATER_INSIDE) &&
                state.getValue(WHAT_LEAVES_INSIDE) != 0 &&
                state.getValue(WHAT_TEA_INSIDE) == 0) {

            BlockState newState = state.setValue(WHAT_TEA_INSIDE, state.getValue(WHAT_LEAVES_INSIDE));
            level.setBlock(pos, newState, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));

        }

        if (state.getValue(WHAT_TEA_INSIDE) != 0 && stack.is(ModItems.CHAKHAI)) {
            fillChakhai(stack, state, level, pos, player, hand);
            return InteractionResult.SUCCESS;
        } else if (state.getValue(WHAT_TEA_INSIDE) != 0 && ItemCategory.CHAKHAIS.getItems().get(state.getValue(WHAT_TEA_INSIDE) - 1) == stack.getItem() && stack.getDamageValue() != 0) {
            fillChakhai(stack, state, level, pos, player, hand);
            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }
    private void checkLeaves(ItemStack leaves, BlockState state, Level level, BlockPos pos, Player player) {
        if (ItemCategory.TEA_LEAVES.getItems().contains(leaves.getItem())) {
            leaves.consume(1, player);
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            BlockState newState = state.setValue(WHAT_LEAVES_INSIDE, ItemCategory.TEA_LEAVES.getItems().indexOf(leaves.getItem()) + 1);
            level.setBlock(pos, newState, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
        }

    }
    private void fillChakhai(ItemStack chakhai, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        ItemStack newStack = new ItemStack(ItemCategory.CHAKHAIS.getItems().get(state.getValue(WHAT_TEA_INSIDE) - 1));
        newStack.setDamageValue(chakhai.is(ModItems.CHAKHAI) ? 5 : chakhai.getDamageValue() - 1);
        player.setItemInHand(hand, newStack);
        BlockState newState = state.setValue(WHAT_LEAVES_INSIDE, 0).setValue(WHAT_TEA_INSIDE, 0).setValue(IS_WATER_INSIDE, false);
        level.setBlock(pos, newState, 3);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int n = state.getValue(WHAT_TEA_INSIDE);
        if (n > 0 && random.nextInt(7) == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, 0, 0.01, 0);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{WHAT_LEAVES_INSIDE, IS_WATER_INSIDE, WHAT_TEA_INSIDE, FACING});
    }


}