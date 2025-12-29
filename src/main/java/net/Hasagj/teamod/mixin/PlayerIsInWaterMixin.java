package net.hasagj.teamod.mixin;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class PlayerIsInWaterMixin {

    @Inject(method = "isInRain", at = @At("RETURN"), cancellable = true)
    private void injectIsInRain(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;

        if (!((Object) this instanceof LivingEntity livingEntity)) return;

        if (livingEntity.hasEffect(ModEffects.SEAS_BLESSING_EFFECT)) {
            cir.setReturnValue(true);
        }
    }
}
