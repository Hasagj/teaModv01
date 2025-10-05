package net.hasagj.teamod.event;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

public class CameraEvent {
    public CameraEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void camera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = event.getRenderer().getMinecraft();
        if (mc.player.hasEffect(ModEffects.DROWSY_EFFECT)) {
            event.setRoll((float) Math.sin(mc.level.getGameTime() * 0.05F) * 1.5F);
            event.setYaw(((float) Math.sin(mc.level.getGameTime() * 0.1F) * 1.5F) + event.getYaw());

        }
    }
}
