package net.hasagj.teamod.block.custom;

import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jline.utils.Log;

public class SculkBushBlock extends SweetBerryBushBlock {
    public SculkBushBlock(Properties properties) {
        super(properties);
    }


    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        return new ItemStack(ItemStack.EMPTY.getItem());
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.SCULK);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return (Integer)state.getValue(AGE) < 1;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = (Integer)state.getValue(AGE);
        BlockPos[] neighbors = {
                pos.offset( 1,0, 0), pos.offset(-1,0, 0),
                pos.offset( 0,0, 1), pos.offset( 0,0,-1),
                pos.offset( 1,0, 1), pos.offset( 1,0,-1),
                pos.offset(-1,0, 1), pos.offset(-1,0,-1)
        };
        int sculk_near = 0;
        TrailParticleOption particleOption = new TrailParticleOption(new Vec3(pos.getX() + 0.5F, pos.getY() +0.75F, pos.getZ() + 0.5F), 0x034150, 20);
        for (BlockPos sculkPos : neighbors) {
            if (level.getBlockState(sculkPos).is(Blocks.SCULK_VEIN)) {
                sculk_near++;
                level.sendParticles(particleOption, sculkPos.getX() + 0.5F, sculkPos.getY(), sculkPos.getZ() + 0.5F, 2, 0.5F, 0, 0.5F, 1);
            }
        }
        if (i < 1 && random.nextInt(Math.max(1, 100 - sculk_near * 10)) == 0) {
            BlockState blockstate = (BlockState)state.setValue(AGE, i + 1);
            level.setBlock(pos, blockstate, 2);
            for (BlockPos sculkPos : neighbors) {
                if (level.getBlockState(sculkPos).is(Blocks.SCULK_VEIN)) {
                    level.destroyBlock(sculkPos, false);
                }
            }
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
        }
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int i = (Integer)state.getValue(AGE);
        if (i > 0) {
            popResource(level, pos, new ItemStack(ModItems.SCULK_BURGEON.get(), 1));
            level.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = (BlockState)state.setValue(AGE, 0);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effect) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.WARDEN) {
            entity.makeStuckInBlock(state, new Vec3((double)0.8F, (double)0.75F, (double)0.8F));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40));
            level.gameEvent(livingEntity, GameEvent.ENTITY_ACTION, pos);
        }

    }


}
