package net.hasagj.teamod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class SolidEffect extends MobEffect {


    public SolidEffect(MobEffectCategory category, int color) {super(category, color);}
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        Player p = (Player) livingEntity;
        if(livingEntity.hasEffect(MobEffects.POISON) || livingEntity.hasEffect(MobEffects.HUNGER)) {
            livingEntity.removeEffect(MobEffects.POISON);
            livingEntity.removeEffect(MobEffects.HUNGER);

        }

        return super.applyEffectTick(level, livingEntity, amplifier);
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
