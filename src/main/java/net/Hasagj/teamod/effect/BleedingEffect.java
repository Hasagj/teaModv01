package net.hasagj.teamod.effect;

import net.hasagj.teamod.damage.ModDamageTypes;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BleedingEffect extends MobEffect {
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        Vec3 vec3 = livingEntity.getKnownMovement();
        if (vec3.horizontalDistanceSqr() > (double)0.0F) {
            double d0 = Math.abs(vec3.x());
            double d1 = Math.abs(vec3.z());
            if (!(livingEntity instanceof AbstractSkeleton) && (d0 >= (double)0.003F || d1 >= (double)0.003F)) {
                livingEntity.hurtServer(level, ModDamageTypes.bleeding(level), (float)Math.hypot(d0, d1) + (amplifier * 0.3F));
                for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, livingEntity.getBoundingBox().inflate(5))) {
                    if (entity.hasEffect(ModEffects.BLOODTHIRST_EFFECT) && !livingEntity.hasEffect(ModEffects.BLOODTHIRST_EFFECT)) {
                        entity.heal((float)Math.hypot(d0, d1) + (amplifier * 0.3F));
                        this.emitParticles(livingEntity, level, entity.getOnPos().atY(entity.getOnPos().getY() + 1), 10, false);
                    }
                }
            }
        }
        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    public BleedingEffect(MobEffectCategory category, int color) {super(category, color);}
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    private void emitParticles(LivingEntity livingEntity, ServerLevel level, BlockPos pos, int count, boolean reverseDirection) {
        RandomSource randomsource = level.random;

        for(double d0 = (double)0.0F; d0 < (double)count; ++d0) {
            int i = 0xF90000;
            AABB aabb = livingEntity.getBoundingBox();
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
