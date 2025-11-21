package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

public class OnEntityTickEvent {
    public OnEntityTickEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onArrowTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        Level level = arrow.level();
        if (level.isClientSide) return;
        LivingEntity livingEntity = (LivingEntity) arrow.getOwner();
        if (livingEntity == null) return;
        // Найти ближайшего моба
        if (livingEntity.hasEffect(ModEffects.GARDENS_BLESSING_EFFECT) && livingEntity.isCrouching() && !livingEntity.level().getEntitiesOfClass(LivingEntity.class,
                livingEntity.getBoundingBox().inflate(30),
                entity -> entity != livingEntity && entity instanceof Enemy).isEmpty()) {
            List<LivingEntity> mobs = livingEntity.level().getEntitiesOfClass(LivingEntity.class,
                    arrow.getBoundingBox().inflate(7),
                    entity -> entity != livingEntity && entity instanceof Enemy);

            if (mobs.isEmpty()) return;

            // Вектор до цели
            Vec3 arrowPos = arrow.position();
            Vec3 targetPos = mobs.getFirst().getEyePosition();
            Vec3 toTarget = targetPos.subtract(arrowPos).normalize();

            // Текущая скорость стрелы
            Vec3 velocity = arrow.getDeltaMovement();

            // Коэффициент автоповорота (0.05 — очень мягкий, 0.2 — сильный)
            double homingStrength = 0.3;

            // Комбинация скорости + автоприцеливания
            Vec3 newVelocity = velocity.scale(1 - homingStrength).add(toTarget.scale(homingStrength));

            arrow.setDeltaMovement(newVelocity);
            arrow.hasImpulse = true; // чтобы Minecraft понял, что скорость изменилась
        }
    }

}
