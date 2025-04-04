package net.hasagj.teamod.effect;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class DrowsyEffect extends MobEffect {
    RandomSource random = RandomSource.create();
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (random.nextInt(250) == 0 && !livingEntity.hasEffect(MobEffects.BLINDNESS)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40));
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    public DrowsyEffect(MobEffectCategory category, int color) {super(category, color);}
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    
}
