package net.hasagj.teamod.effect;

import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.List;

public class EchoEffect extends MobEffect {
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    public EchoEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void onMobRemoved(ServerLevel level, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10))) {
            if (livingEntity.hasEffect(ModEffects.RESONANCE_EFFECT)) continue;
            VibrationParticleOption particleOption = new VibrationParticleOption(new EntityPositionSource(livingEntity, 0), 20);
            level.sendParticles(particleOption, entity.getX(), entity.getY() + 1, entity.getZ(), 1, 0, 0, 0, 1);
            livingEntity.addEffect(new MobEffectInstance(ModEffects.ECHO_EFFECT, 400));
            livingEntity.hurtServer(level, entity.damageSources().sonicBoom(entity), entity.getMaxHealth() / 4);
        }
        spreadSculkLikeCatalyst(level, new BlockPos((int)entity.getX(), (int)entity.getY() - 1, (int)entity.getZ()), 3);
    }
    private final SculkSpreader spreader = SculkSpreader.createLevelSpreader();
    private void spreadSculkLikeCatalyst(ServerLevel level, BlockPos origin, int charge) {
        // Создаём новый спредер, связанный с уровнем


        // Добавляем курсор — "начало" распространения
        spreader.addCursors(BlockPos.containing(origin.relative(Direction.UP.getAxis(), 1).getCenter()), charge);

        // Выполняем несколько шагов распространения
        spreader.updateCursors(level, origin, level.random, true);
        if (charge > 0) {
            spreadSculkLikeCatalyst(level, origin, charge - 1);
        }

    }
}