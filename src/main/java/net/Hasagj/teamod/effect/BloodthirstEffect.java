package net.hasagj.teamod.effect;

import net.hasagj.teamod.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class BloodthirstEffect extends MobEffect {
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        livingEntity.hurtServer(level, livingEntity.damageSources().dryOut(), livingEntity instanceof Player player ? (player.getHealth() - (20 - player.getFoodData().getFoodLevel()) * 0.25F > 0 ? (20 - player.getFoodData().getFoodLevel()) * 0.25F : player.getHealth() - 1) : 0.2F);
        if (livingEntity instanceof Player player) {
            player.getFoodData().eat(-1, -1);
        }
        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    public BloodthirstEffect(MobEffectCategory category, int color) {super(category, color);}
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {

        return duration % 20 == 0;
    }


}
