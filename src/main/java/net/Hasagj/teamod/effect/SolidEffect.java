package net.hasagj.teamod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SolidEffect extends MobEffect {
    public SolidEffect(MobEffectCategory category, int color) {super(category, color);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
