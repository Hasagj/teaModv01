package net.hasagj.teamod.effect;

import net.hasagj.teamod.damage.ModDamageTypes;
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

public class EndsBlessingEffect extends MobEffect {
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        if (livingEntity.isInWaterOrRain()) {
            livingEntity.hurtServer(level, ModDamageTypes.water(level), 2);
        }

        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    public EndsBlessingEffect(MobEffectCategory category, int color) {super(category, color);}
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;

    }


}
