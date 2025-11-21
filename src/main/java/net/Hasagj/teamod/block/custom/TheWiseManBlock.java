package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TheWiseManBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<TheWiseManBlock> CODEC = simpleCodec(TheWiseManBlock::new);
    public static final IntegerProperty XP = IntegerProperty.create("xp", 0, 20);
    public static final BooleanProperty CAN_COLLECT = BooleanProperty.create("can_collect");
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 11.0, 14.0);
    private UUID playerUUID;
    public MapCodec<TheWiseManBlock> codec() {
        return CODEC;
    }
    public TheWiseManBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(XP, 0).setValue(CAN_COLLECT, false)));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        return new ItemStack(ModItems.THE_WISE_MAN.get());

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
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(XP) != 0 && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    5,
                    0.2, 0.2, 0.2,
                    0.5);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        List<Item> tea_list = List.of(ModItems.CUP_GREEN_TEA.get(), ModItems.CUP_BLACK_TEA.get(), ModItems.CUP_HIBISCUS_TEA.get(), ModItems.CUP_DAISY_TEA.get(), ModItems.CUP_PALE_TEA.get(), ModItems.CUP_PITCHER_TEA.get(), ModItems.CUP_CACTUS_TEA.get(), ModItems.CUP_CHORUS_TEA.get(), ModItems.CUP_ANCIENT_TEA.get(), ModItems.CUP_WANDERERS_TEA.get());
        for (Item tea : tea_list) {
            if (stack.is(tea) && player.experienceLevel >= 20 && !state.getValue(CAN_COLLECT) && state.getValue(XP) == 0 && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
                stack.consume(1, player);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ModItems.CUP.get()));
                } else if (!player.getInventory().add(new ItemStack(ModItems.CUP.get()))) {
                    player.drop(new ItemStack(ModItems.CUP.get()), false);
                }
                serverLevel.sendParticles(ParticleTypes.SPLASH,
                        pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                        30,
                        0.25F, 0.25F, 0.25F,
                        0);
                this.emitParticles(serverPlayer, serverLevel, pos, 50, false);
                playerUUID = player.getUUID();
                level.scheduleTick(pos, this, 1);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            } else if (stack.is(tea) && player.experienceLevel < 20 && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
                stack.consume(1, player);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ModItems.CUP.get()));
                } else if (!player.getInventory().add(new ItemStack(ModItems.CUP.get()))) {
                    player.drop(new ItemStack(ModItems.CUP.get()), false);
                }
                serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        5,
                        0.2, 0.2, 0.2,
                        0.3);
                return InteractionResult.SUCCESS;
            } else if (stack.is(Items.BRUSH) && state.getValue(CAN_COLLECT) && state.getValue(XP) == 20 && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
                stack.hurtWithoutBreaking(10 ,player);
                this.emitParticles(serverPlayer, serverLevel, pos, 50, true);
                playerUUID = player.getUUID();
                level.scheduleTick(pos, this, 2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ServerPlayer player = (ServerPlayer)level.getPlayerByUUID(playerUUID);
        if (player != null) {
            if (state.getValue(XP) == 20) {
                level.setBlockAndUpdate(pos, state.setValue(CAN_COLLECT, true));
            } else if (state.getValue(XP) == 0) {
                level.setBlockAndUpdate(pos, state.setValue(CAN_COLLECT, false));
            }
            if (state.getValue(XP) < 20 && !state.getValue(CAN_COLLECT)) {
                player.setExperienceLevels(player.experienceLevel - 1);
                level.setBlockAndUpdate(pos, state.setValue(XP, state.getValue(XP) + 1));
                level.scheduleTick(pos, this, 1);
            }
            if (state.getValue(XP) > 0 && state.getValue(CAN_COLLECT)) {
                player.setExperienceLevels(player.experienceLevel + 1);
                level.setBlockAndUpdate(pos, state.setValue(XP, state.getValue(XP) - 1));
                level.scheduleTick(pos, this, 1);
            }


        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, XP, CAN_COLLECT});
    }

    private void emitParticles(ServerPlayer player, ServerLevel level, BlockPos pos, int count, boolean reverseDirection) {
            RandomSource randomsource = level.random;

            for(double d0 = (double)0.0F; d0 < (double)count; ++d0) {
                int i = d0 % 2 != 0 ? 0x31D660 : 0xF2E922;
                AABB aabb = player.getBoundingBox();
                Vec3 vec3 = aabb.getMinPosition().add(randomsource.nextDouble() * aabb.getXsize(), randomsource.nextDouble() * aabb.getYsize(), randomsource.nextDouble() * aabb.getZsize());
                Vec3 vec31 = Vec3.atLowerCornerOf(pos).add(randomsource.nextDouble(), randomsource.nextDouble(), randomsource.nextDouble());
                if (reverseDirection) {
                    Vec3 vec32 = vec3;
                    vec3 = vec31;
                    vec31 = vec32;
                }

                TrailParticleOption trailparticleoption = new TrailParticleOption(vec31, i, randomsource.nextInt(40) + 10);
                level.sendParticles(trailparticleoption, true, true, vec3.x, vec3.y, vec3.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
            }

    }



}
