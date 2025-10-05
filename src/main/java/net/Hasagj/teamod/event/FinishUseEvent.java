package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.Objects;

public class FinishUseEvent {
    public FinishUseEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    public static int bitterNutrition;
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        ItemStack usedItem = event.getItem();


        if (entity instanceof Player player && usedItem.is(ModItems.CUP_DAISY_TEA)) {
            for (ItemStack tea : player.getInventory()) {
                if (tea.is(ModItems.CUP_DAISY_TEA)) {
                    player.getCooldowns().addCooldown(tea, 600);
                }
            }
        }
        if (entity.hasEffect(ModEffects.POTENTIAL_EFFECT) && entity.level() instanceof ServerLevel level) {
            if (usedItem.is(ModItems.PITCHER_TURNIP)) {
                entity.addEffect(new MobEffectInstance(ModEffects.APPETITE_EFFECT, 2400));
                entity.removeEffect(ModEffects.POTENTIAL_EFFECT);
                level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        30,
                        0.5, 0.5, 0.5,
                        0);
            }
            if (usedItem.is(Items.SWEET_BERRIES)) {
                entity.addEffect(new MobEffectInstance(ModEffects.SWEET_EFFECT, 6000));
                entity.removeEffect(ModEffects.POTENTIAL_EFFECT);
                level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        30,
                        0.5, 0.5, 0.5,
                        0);
            }
        }

        if (entity.hasEffect(ModEffects.APPETITE_EFFECT) && entity.level() instanceof ServerLevel level) {
            if (entity instanceof Player player && usedItem.getItem().components().has(DataComponents.FOOD)) {
                player.getFoodData().eat((int) (usedItem.getItem().components().get(DataComponents.FOOD).nutrition() * 0.5F) + 1, 0);
                if (usedItem.is(Items.POISONOUS_POTATO) || usedItem.is(Items.SPIDER_EYE) || usedItem.is(Items.CHICKEN) || usedItem.is(Items.PUFFERFISH) || usedItem.is(Items.ROTTEN_FLESH)) {
                    ModTriggers.APPETITE_TRIGGER.get().trigger((ServerPlayer) player);
                }
            }

        }

        if (entity.hasEffect(ModEffects.BITTER_EFFECT) && entity.level() instanceof ServerLevel level) {
            if (entity instanceof ServerPlayer player) {
                if (player.getFoodData().getFoodLevel() == 20) {
                    player.getFoodData().setFoodLevel(18);
                }
                if (usedItem.has(DataComponents.FOOD)) {
                    int nutrition = Objects.requireNonNull(usedItem.get(DataComponents.FOOD)).nutrition();
                    bitterNutrition = nutrition;
                    player.getFoodData().setFoodLevel(nutrition > 2 ? player.getFoodData().getFoodLevel() - nutrition + (int)(nutrition * 0.25) : player.getFoodData().getFoodLevel() - nutrition + 1);
                    player.getFoodData().setSaturation(0);
                    level.sendParticles(ParticleTypes.ITEM_SLIME,
                            entity.getX(), entity.getY() + 1, entity.getZ(),
                            10,
                            0.5, 0.5, 0.5,
                            0);
                }

            }
        }

    }
}
