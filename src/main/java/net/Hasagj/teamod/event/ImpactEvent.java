package net.hasagj.teamod.event;


import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent;


public class ImpactEvent {
    public ImpactEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult result) {
            if (result.getEntity() instanceof Player player) {
                if (player.hasEffect(ModEffects.ENDS_BLESSING_EFFECT) && player.getFoodData().getFoodLevel() > 0 && player.level() instanceof ServerLevel world) {
                    for(int i = 0; i < 16; ++i) {
                        if (player.isPassenger()) {
                            player.stopRiding();
                        }
                        double d0 = player.getX() + (player.getRandom().nextDouble() - (double)0.5F) * 16;
                        double d1 = Mth.clamp(player.getY() + (player.getRandom().nextDouble() - (double)0.5F) * 16, (double)world.getMinY(), (double)(world.getMinY() + ((ServerLevel)world).getLogicalHeight() - 1));
                        double d2 = player.getZ() + (player.getRandom().nextDouble() - (double)0.5F) * 16;

                        player.randomTeleport(d0, d1, d2, true);
                        if (player.randomTeleport(d0, d1, d2, true)) {
                            world.gameEvent(GameEvent.TELEPORT, player.position(), GameEvent.Context.of(player));
                            SoundSource soundsource;
                            SoundEvent soundevent;
                            soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                            soundsource = SoundSource.PLAYERS;


                            world.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), soundevent, soundsource);
                            player.resetFallDistance();
                            break;
                        }
                    }
                    player.getFoodData().eat(-1, -1);
                    event.setCanceled(true);
                }
            }
        }

    }
}
