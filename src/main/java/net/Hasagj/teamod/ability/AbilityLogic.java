package net.hasagj.teamod.ability;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AbilityLogic {


    public static void use(Player player) {

        if (!player.hasEffect(ModEffects.RESONANCE_EFFECT)) return;


        double maxDistance = 20;
        Vec3 start = player.getEyePosition(1f);
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(maxDistance));

        // Прямоугольная область вокруг луча
        AABB aabb = new AABB(start, end)
                .inflate(1.0); // ширина "луча"

        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        int steps = 40; // чем больше — тем плотнее луч
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 point = start.lerp(end, t);

            serverLevel.sendParticles(
                    ParticleTypes.SONIC_BOOM,
                    point.x, point.y, point.z,
                    1,
                    0, 0, 0,
                    0      // скорость 0 — чтобы частицы висели на месте
            );
        }
        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS);
        serverLevel.getEntities(player, aabb)
                .stream()
                .filter(e -> e instanceof LivingEntity living && e != player)
                .forEach(entity -> {
                    // Проверяем, что цель действительно на линии взгляда
                    if (isLookingAt(player, entity, 1.5)) {
                        entity.hurtServer(serverLevel,
                                level.damageSources().sonicBoom(player),
                                player.getFoodData().getFoodLevel() * 1.5F // Урон
                        );
                    }
                });
        player.hurtMarked = true;
        player.knockback(player.getFoodData().getFoodLevel() * 0.1F, player.getLookAngle().normalize().x(), player.getLookAngle().normalize().z());
        player.getFoodData().setFoodLevel(0);
         }

    // Проверяем, что существо примерно совпадает с направлением взгляда
    private static boolean isLookingAt(Player player, net.minecraft.world.entity.Entity entity, double angleCosLimit) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 direction = entity.position().subtract(player.getEyePosition()).normalize();
        double dot = look.dot(direction);
        return dot > (1.0 - angleCosLimit * 0.1);
    }
}
