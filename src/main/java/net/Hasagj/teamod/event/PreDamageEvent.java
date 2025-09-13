package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

public class PreDamageEvent {
    public PreDamageEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onPlayerHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            float damageAmount = event.getNewDamage();
            if (player.hasEffect(ModEffects.SWEET_EFFECT) && player.level() instanceof ServerLevel level && player instanceof ServerPlayer serverPlayer) {
                if (damageAmount >= player.getHealth() && player.getFoodData().getFoodLevel() != 0) {
                    event.setNewDamage(0);
                    player.setHealth(1);
                    player.heal(player.getFoodData().getFoodLevel());
                    player.getFoodData().setFoodLevel(0);
                    player.getFoodData().setSaturation(0);
                    player.removeEffect(ModEffects.SWEET_EFFECT);
                    player.addEffect(new MobEffectInstance(ModEffects.BITTER_EFFECT, 6000));
                    level.playSound(null, player.blockPosition(), SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, 1F, 1F);
                    level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                            player.getX(), player.getY() + 1, player.getZ(),
                            30,
                            0.5, 0.5, 0.5,
                            0);
                    ModTriggers.SWEET_TRIGGER.get().trigger(serverPlayer);
                }
            }

            if (player.hasEffect(ModEffects.THORNY_EFFECT) && player.level() instanceof ServerLevel world) {
                double radius = 7.0;
                if (event.getOriginalDamage() >= 2) {
                    player.getItemBySlot(EquipmentSlot.HEAD).hurtWithoutBreaking(3, player);
                    player.getItemBySlot(EquipmentSlot.BODY).hurtWithoutBreaking(3, player);
                    player.getItemBySlot(EquipmentSlot.LEGS).hurtWithoutBreaking(3, player);
                    player.getItemBySlot(EquipmentSlot.FEET).hurtWithoutBreaking(3, player);
                    // Список всех живых мобов в радиусе
                    List<LivingEntity> mobs = world.getEntitiesOfClass(LivingEntity.class,
                            player.getBoundingBox().inflate(radius),
                            entity -> !entity.hasEffect(ModEffects.THORNY_EFFECT));
                    world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.PLAYERS, 1F, 1F);
                    world.sendParticles(ParticleTypes.WITCH,
                            player.getX(), player.getY() + 1, player.getZ(),
                            100,
                            7, 7, 7,
                            2);
                    for (LivingEntity mob : mobs) {
                        mob.hurt(mob.damageSources().cactus(), mob.getStingerCount() + 1);
                        if (mob.getStingerCount() >= 7) {
                            mob.setStingerCount(7);
                            ModTriggers.STINGING_TRIGGER.get().trigger((ServerPlayer) player);
                        } else {
                            mob.setStingerCount(mob.getStingerCount() + 1);
                        }
                        mob.knockback(1, player.getX(), player.getZ());
                    }
                }
            }

        }

    }

}
