package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;

public class OnTickEvent {
    public OnTickEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel world)) return;
        List<ServerPlayer> players = world.players();
        for (ServerPlayer player : world.players()) {
            if (player.hasEffect(ModEffects.GARDENS_BLESSING_EFFECT)) {
                double randomRadius = RandomSource.create().nextInt(0, 2);
                if (RandomSource.create().nextInt(100) == 0) {
                    world.sendParticles(ModParticles.ORANGE_EYES_PARTICLES.get(), player.getX() + randomRadius, player.getY() + randomRadius / 4, player.getZ() + randomRadius, 0, 0, 0, 0, 0);
                }
                // Радиус в блоках
                double radius = 30.0;

                // Список всех живых мобов в радиусе
                List<LivingEntity> mobs = world.getEntitiesOfClass(LivingEntity.class,
                        player.getBoundingBox().inflate(radius),
                        entity -> entity != player && entity instanceof Enemy);

                for (LivingEntity mob : mobs) {
                    if (!mob.hasEffect(MobEffects.GLOWING)) {

                        // Применим, например, эффект слабости на 5 секунд
                        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, true, false));

                    }
                    if (RandomSource.create().nextInt(100) == 0) {
                        world.sendParticles(ModParticles.ORANGE_EYES_PARTICLES.get(), mob.getX() + randomRadius, mob.getY() + randomRadius / 4, mob.getZ() + randomRadius, 0, 0, 0, 0, 0);
                    }
                }
            }

            if (player.hasEffect(ModEffects.BITTER_EFFECT)) {
                player.removeEffect(MobEffects.SATURATION);
                player.removeEffect(ModEffects.POTENTIAL_EFFECT);
                player.removeEffect(ModEffects.APPETITE_EFFECT);
                player.removeEffect(ModEffects.SWEET_EFFECT);

            }

            if (player.hasEffect(ModEffects.THORNY_EFFECT) && player.getEffect(ModEffects.THORNY_EFFECT).endsWithin(1)) {
                player.setStingerCount(0);
            }


            
        }

    }
}
