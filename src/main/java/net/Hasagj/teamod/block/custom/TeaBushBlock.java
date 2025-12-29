package net.hasagj.teamod.block.custom;

import net.hasagj.teamod.block.ModBlocks;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

public class TeaBushBlock extends SweetBerryBushBlock {
    public TeaBushBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(IDEAL, false)));
    }

    public static final BooleanProperty IDEAL = BooleanProperty.create("ideal");

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        return new ItemStack((ItemLike) ModItems.TEA_SEEDS);
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.getBlockState(pos.below()).is(Blocks.FARMLAND) && level.getBiome(pos).is(Biomes.CHERRY_GROVE)) {
            level.scheduleTick(pos, this, 1);
        }
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        boolean ideal = true;

        BlockPos below = pos.below();

        if (level.getBlockState(below).is(Blocks.FARMLAND)) {

            BlockPos[] neighbors = {
                    pos.offset( 1,0, 0), pos.offset(-1,0, 0),
                    pos.offset( 0,0, 1), pos.offset( 0,0,-1),
                    pos.offset( 1,0, 1), pos.offset( 1,0,-1),
                    pos.offset(-1,0, 1), pos.offset(-1,0,-1)
            };

            for (BlockPos neighbor : neighbors) {
                if (!level.getBlockState(neighbor).isAir()) {
                    ideal = false;
                    break;
                }
            }

            // обновляем состояние
            if (state.getValue(IDEAL) != ideal) {
                level.setBlock(pos, state.setValue(IDEAL, ideal), 2);
            }
        }

        // запрашиваем следующий тик
        if (state.getValue(AGE) < 3) {
            level.scheduleTick(pos, this, 20); // проверять каждый 1 секунду (20 тиков)
        }

    }



    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = (Integer)state.getValue(AGE);
        if (i < 3 && level.getRawBrightness(pos.above(), 0) >= 9 && CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {

            BlockState blockstate = (BlockState)state.setValue(AGE, i + 1);
            level.setBlock(pos, blockstate, 2);
            CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
        }

    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int i = (Integer)state.getValue(AGE);
        boolean flag = i == 3;
        if (i > 1) {
            int j = 1 + level.random.nextInt(2);
            if (state.getValue(IDEAL)) {
                popResource(level, pos, new ItemStack(ModItems.PERFECT_TEA_LEAF.get(), level.random.nextInt(10) == 0 ? 1 : 0));
            }
            popResource(level, pos, new ItemStack(ModItems.TEA_LEAF.get(), j + (flag ? 1 : 0)));
            popResource(level, pos, new ItemStack(ModItems.TEA_SEEDS.get(), 1));
            level.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = (BlockState)state.setValue(AGE, 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effect) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3((double)0.8F, (double)0.75F, (double)0.8F));
            if (livingEntity instanceof Warden warden && !warden.getAngerLevel().isAngry() && level instanceof ServerLevel serverLevel) {
                serverLevel.setBlock(pos, ModBlocks.SCULK_BUSH.get().defaultBlockState(), 3);
                serverLevel.setBlock(pos.below(), Blocks.SCULK.defaultBlockState(), 3);
            }
        }

    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(IDEAL) && random.nextInt(15) == 0) {
            level.addParticle(ParticleTypes.WAX_ON, (double)pos.getX() + (double)0.5F + random.nextDouble() * 0.5, (double)pos.getY() + (double)0.5F + random.nextDouble() * 0.5, (double)pos.getZ() + (double)0.5F + random.nextDouble() * 0.5, 0, 0, 0);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE, IDEAL});
    }


}
