package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class AutoSleepEvent {
    private long lastSleepTime = -1;
    public AutoSleepEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        // УБРАЛ static
        if (!(event.getLevel() instanceof ServerLevel world)) {
            return;
        }

        long time = world.getDayTime() % 24000; // Время в тиках

        if (time >= 12500 && time < 24000) { // Если ночь (18:00 - 6:00)
            for (ServerPlayer player : world.players()) {
                if (!player.isSleeping() && player.isAlive() && !player.isSpectator() && player.hasEffect(ModEffects.DROWSY_EFFECT)) {
                    BlockPos bedPos = player.blockPosition();
                    player.setPose(Pose.SLEEPING);
                    player.startSleepInBed(bedPos);
                    if (lastSleepTime == -1) {
                        lastSleepTime = world.getDayTime(); // Сохраняем момент начала сна
                    }
                    if (world.getDayTime() - lastSleepTime >= 100) {// Задержка на 100 тиков (~5 секунд)
                        world.setDayTime(0); // Устанавливаем время на начало дня
                        lastSleepTime = -1; // Сбрасываем таймер
                        player.removeEffect(ModEffects.DROWSY_EFFECT);
                    }
                }
                
            }
        }else {
            lastSleepTime = -1; // Если не ночь, сбрасываем таймер
        }
    }
}
