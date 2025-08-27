package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MoonRavenBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<MoonRavenBlock> CODEC = simpleCodec(MoonRavenBlock::new);
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 9.0, 14.0);
    public MapCodec<MoonRavenBlock> codec() {
        return CODEC;
    }
    public MoonRavenBlock(BlockBehaviour.Properties properties) {
        super(properties);

    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        MinecraftServer server = level.getServer();
        List<Item> tea_list = new ArrayList<>();
        tea_list.add(ModItems.CUP_BLACK_TEA.get());
        tea_list.add(ModItems.CUP_GREEN_TEA.get());
        tea_list.add(ModItems.CUP_HIBISCUS_TEA.get());
        tea_list.add(ModItems.CUP_DAISY_TEA.get());
        tea_list.add(ModItems.CUP_PALE_TEA.get());
        tea_list.add(ModItems.CUP_PITCHER_TEA.get());
        tea_list.add(ModItems.CUP_CACTUS_TEA.get());
        tea_list.add(ModItems.CUP_CHORUS_TEA.get());
        for (Item tea : tea_list) {
            if (stack.is(tea)) {
                stack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ModItems.CUP.get()));
                } else if (!player.getInventory().add(new ItemStack(ModItems.CUP.get()))) {
                    player.drop(new ItemStack(ModItems.CUP.get()), false);
                }
                if (!level.isRaining() && level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 0.5, 0.5, 0.5, 1);
                    player.hurtServer(serverLevel, player.damageSources().magic(), 2F);

                } else {
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 0.5, 0.5, 0.5, 1);
                    }
                    if (server != null) {
                        server.getCommands().performPrefixedCommand(
                                server.createCommandSourceStack()
                                        .withSuppressedOutput(),
                                "weather clear 12000"
                        );
                    }
                }
                return InteractionResult.SUCCESS;
            } else {
                continue;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }



}
