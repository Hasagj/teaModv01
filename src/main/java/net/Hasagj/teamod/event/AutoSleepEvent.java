package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.reflect.Method;
import java.util.List;

public class AutoSleepEvent {
    private long lastSleepTime = -1;
    private ServerLevel world;

    public AutoSleepEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel world)) return;
        List<ServerPlayer> players = world.players();
        // Проверка: все активные игроки имеют эффект сонливости
        boolean allDrowsy = players.stream()
                .filter(p -> p.isAlive() && !p.isSpectator() && p.level().dimension() == ServerLevel.OVERWORLD)
                .allMatch(p -> p.hasEffect(ModEffects.DROWSY_EFFECT));
        for (ServerPlayer player : world.players()) {
            if (!player.isSleeping() && player.getFoodData().getFoodLevel() >= 12 && allDrowsy && !players.isEmpty() && !player.isSpectator() && player.isAlive() && world.dimension() == Level.OVERWORLD) {;
                player.canEat(false);
                player.stopUsingItem();
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40, 255, true, false));
                player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 255, true, false));
                player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 40, 255, true, false));
                player.displayClientMessage(Component.translatable("teamod.skipping_night"), true);
                if (lastSleepTime == -1) {
                    lastSleepTime = world.getDayTime(); // Сохраняем момент начала сна
                }
                if (world.getDayTime() - lastSleepTime >= 100) {

                    // Задержка на 100 тиков (~5 секунд)
                    world.setDayTime(world.getDayTime() + 12000); // Устанавливаем время на начало дня
                    lastSleepTime = -1; // Сбрасываем таймер
                    world.players().forEach(element -> element.removeEffect(ModEffects.DROWSY_EFFECT));
                    world.players().forEach(element -> element.getFoodData().eat(-12, 0));
                }
            } else if (!player.isSleeping() && player.getFoodData().getFoodLevel() < 12 && allDrowsy && !players.isEmpty() && !player.isSpectator() && player.isAlive() && world.dimension() == Level.OVERWORLD){
                player.displayClientMessage(Component.translatable("teamod.cant_skip_night"), true);
            }


        }
    }
}