package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CauldronOnFire extends HorizontalDirectionalBlock {
    public static final MapCodec<CauldronOnFire> CODEC = simpleCodec(CauldronOnFire::new);
    public static final IntegerProperty IS_FULL = IntegerProperty.create("is_full", 0, 2);
    private static final VoxelShape SHAPE = Shapes.or(
            // Дно
            Block.box(1, 2, 1, 15, 5, 15),
            // Левая стенка
            Block.box(0, 2, 0, 2, 18, 16),
            // Правая стенка
            Block.box(14, 2, 0, 16, 18, 16),
            // Задняя стенка
            Block.box(2, 2, 0, 14, 18, 2),
            // Передняя стенка
            Block.box(2, 2, 14, 14, 18, 16)
    );

    public CauldronOnFire(Properties properties) {
        super(properties.randomTicks().lightLevel(state -> 12));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(IS_FULL, 0)));
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
        if (state.getValue(IS_FULL) == 0) {
            if (stack.is(Items.WATER_BUCKET) || stack.is(ModItems.BOILED_WATER.get())) {
                stack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                } else if (!player.getInventory().add(new ItemStack(Items.BUCKET))) {
                    player.drop(new ItemStack(Items.BUCKET), false);
                }
                flag = true;
                level.setBlockAndUpdate(pos, state.setValue(IS_FULL, 1));
                level.scheduleTick(pos, this, 300);
            }
            return InteractionResult.SUCCESS;
        }
        else if(state.getValue(IS_FULL) == 1) {
            if (stack.is(Items.BUCKET)) {
                stack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                } else if (!player.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
                    player.drop(new ItemStack(Items.WATER_BUCKET), false);
                }
                flag = true;
                level.setBlockAndUpdate(pos, state.setValue(IS_FULL, 0));
            }
            return InteractionResult.SUCCESS;
        }
        else if(state.getValue(IS_FULL) == 2) {
            if (stack.is(Items.BUCKET)) {
                stack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ModItems.BOILED_WATER.get()));
                } else if (!player.getInventory().add(new ItemStack(ModItems.BOILED_WATER.get()))) {
                    player.drop(new ItemStack(ModItems.BOILED_WATER.get()), false);
                }
                flag = true;
                level.setBlockAndUpdate(pos, state.setValue(IS_FULL, 0));
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(IS_FULL) == 1) {
            level.setBlock(pos, state.setValue(IS_FULL, 2), 3);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int n = state.getValue(IS_FULL);
        if (n == 1 && random.nextInt(7) == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + (double)random.nextInt(16)/16, (double)pos.getY() + 1, (double)pos.getZ() + (double)random.nextInt(16)/16, 0, 0.01, 0);
        }
        else if (n == 2 && random.nextInt(4) == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + (double)random.nextInt(16)/16, (double)pos.getY() + 1, (double)pos.getZ() + (double)random.nextInt(16)/16, 0, 0.02, 0);
        }
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effect) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.ZOMBIFIED_PIGLIN && entity.getType() != EntityType.WITHER_SKELETON && entity.getType() != EntityType.MAGMA_CUBE && !((LivingEntity) entity).hasEffect(MobEffects.FIRE_RESISTANCE) ) {
            entity.hurt(entity.damageSources().campfire(), 1F);
        }

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, IS_FULL});
    }

}
