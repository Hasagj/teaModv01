package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

public class OnClientTickEvent {
    public OnClientTickEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;

            if (player.hasEffect(ModEffects.ENDS_BLESSING_EFFECT) ) {
                Minecraft.getInstance().gameRenderer.setPostEffect(ResourceLocation.withDefaultNamespace("invert"));
            } else if (!player.isSpectator()){
                Minecraft.getInstance().gameRenderer.clearPostEffect();
            }
        }

    }

}