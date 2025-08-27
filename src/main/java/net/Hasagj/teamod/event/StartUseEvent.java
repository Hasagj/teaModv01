package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.Objects;

public class StartUseEvent {
    public StartUseEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        LivingEntity entity = event.getEntity();
        ItemStack usingItem = event.getItem();

        if (entity.hasEffect(ModEffects.BITTER_EFFECT) && entity.level() instanceof ServerLevel level) {
            if (entity instanceof Player player) {
                if (usingItem.is(Items.MILK_BUCKET)) {
                    player.stopUsingItem();
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.USING_ITEM.trigger(serverPlayer, player.getItemInHand(InteractionHand.MAIN_HAND));
                    }
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                    player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 600));
                    player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 2400, 1));
                    level.sendParticles(ParticleTypes.ITEM_SLIME,
                            entity.getX(), entity.getY() + 1, entity.getZ(),
                            100,
                            0.5, 0.5, 0.5,
                            0);
                }
            }
        }

    }
}
