package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.sound.ModSounds;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.*;

public class FinishUseEvent  {

    public FinishUseEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    private static final SculkSpreader spreader = SculkSpreader.createLevelSpreader();
    public static void spreadSculkLikeCatalyst(ServerLevel level, BlockPos origin, int charge) {
        // Создаём новый спредер, связанный с уровнем


        // Добавляем курсор — "начало" распространения
        spreader.addCursors(BlockPos.containing(origin.relative(Direction.UP.getAxis(), 1).getCenter()), charge);

        // Выполняем несколько шагов распространения
        spreader.updateCursors(level, origin, level.random, true);
        if (charge > 0) {
            spreadSculkLikeCatalyst(level, origin, charge - 1);
        }

    }

    public static int bitterNutrition;
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        ItemStack usedItem = event.getItem();



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

        if (entity instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && usedItem.is(ModItems.CUP_WANDERERS_TEA)) {
            int x;
            int y;
            int z;
            if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(Items.RECOVERY_COMPASS) && player.getLastDeathLocation().isPresent()) {
                ServerLevel dimension = serverLevel.getServer().getLevel(player.getLastDeathLocation().get().dimension());
                x = player.getLastDeathLocation().get().pos().getX();
                y = player.getLastDeathLocation().get().pos().getY();
                z = player.getLastDeathLocation().get().pos().getZ();
                player.teleportTo(dimension, x, y, z, Set.of(), player.getYRot(), player.getXRot(), true);
                spreadSculkLikeCatalyst(serverLevel, new BlockPos(x,y - 1,z), 10);
                player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ModItems.BROKEN_RECOVERY_COMPASS.get()));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 1));
                player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600, 1));
                serverLevel.playSound(null, x, y, z, ModSounds.TELEPORT.get(), SoundSource.PLAYERS);
                return;
            }
            if (serverLevel.dimension() == ServerLevel.OVERWORLD) {
                x = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof BedBlock ? player.getRespawnConfig().pos().getX() : serverLevel.getSharedSpawnPos().getX();
                y = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof BedBlock ? player.getRespawnConfig().pos().getY() : serverLevel.getSharedSpawnPos().getY();
                z = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof BedBlock ? player.getRespawnConfig().pos().getZ() : serverLevel.getSharedSpawnPos().getZ();
                player.teleportTo(x, y, z);
            }
            else if (serverLevel.dimension() == ServerLevel.NETHER) {
                ServerLevel dimension = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof RespawnAnchorBlock && !serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE).equals(0) && player.getRespawnConfig().dimension() == ServerLevel.NETHER? serverLevel.getServer().getLevel(ServerLevel.NETHER) : serverLevel.getServer().getLevel(ServerLevel.OVERWORLD);

                x = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof RespawnAnchorBlock && !serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE).equals(0) && player.getRespawnConfig().dimension() == ServerLevel.NETHER ? player.getRespawnConfig().pos().getX() : serverLevel.getSharedSpawnPos().getX();
                y = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof RespawnAnchorBlock && !serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE).equals(0)  && player.getRespawnConfig().dimension() == ServerLevel.NETHER ? player.getRespawnConfig().pos().getY() : serverLevel.getSharedSpawnPos().getY();
                z = player.getRespawnConfig() != null && serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof RespawnAnchorBlock && !serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE).equals(0)  && player.getRespawnConfig().dimension() == ServerLevel.NETHER ? player.getRespawnConfig().pos().getZ() : serverLevel.getSharedSpawnPos().getZ();
                player.teleportTo(dimension, x, y, z, Set.of(), player.getYRot(), player.getXRot(), true);
                if (serverLevel.getBlockState(player.getRespawnConfig().pos()).getBlock() instanceof RespawnAnchorBlock) {
                    serverLevel.setBlockAndUpdate(player.getRespawnConfig().pos(), serverLevel.getBlockState(player.getRespawnConfig().pos()).setValue(RespawnAnchorBlock.CHARGE, serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE) != 0 ? serverLevel.getBlockState(player.getRespawnConfig().pos()).getValue(RespawnAnchorBlock.CHARGE) - 1 : 0));
                }
                serverLevel.playSound(null, x, y, z, ModSounds.TELEPORT.get(), SoundSource.PLAYERS);
            } else if (serverLevel.dimension() == ServerLevel.END) {
                x = 0;
                y = 65;
                z = 0;
                player.teleportTo(x, y, z);
                serverLevel.playSound(null, x, y, z, ModSounds.TELEPORT.get(), SoundSource.PLAYERS);
            }

        }

        if (entity instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && usedItem.is(ModItems.CUP_BLACK_TEA)) {
            if (player.getActiveEffects().isEmpty()) return;
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (!effect.getEffect().value().isBeneficial()) {
                    player.getFoodData().eat(2, 2);
                }
            }
            player.removeAllEffects();
        }
        if (entity instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && usedItem.is(ModItems.CUP_GREEN_TEA)) {
            if (player.getActiveEffects().isEmpty()) return;
            List<MobEffectInstance> effectList = player.getActiveEffects().stream().toList();
            for (MobEffectInstance effect : effectList) {
                if (!effect.getEffect().value().isBeneficial()) {
                    player.removeEffect(effect.getEffect());
                }
            }
        }

        if (entity instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && usedItem.is(ModItems.CUP_PERFECT_GREEN_TEA)) {
            if (player.getActiveEffects().isEmpty()) return;
            List<MobEffectInstance> effectList = player.getActiveEffects().stream().toList();
            for (MobEffectInstance effect : effectList) {
                if (!effect.getEffect().value().isBeneficial()) {
                    for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10))) {
                        livingEntity.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration() * 2, effect.getAmplifier()));
                    }
                    player.removeEffect(effect.getEffect());
                    serverLevel.sendParticles(effect.getParticleOptions(), player.getX(), player.getY() + 0.5F, player.getZ(), 50, 4, 4, 4, 2.0F);
                }
            }
            serverLevel.playSound(null, player.getOnPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.0F);

        }

        if (entity instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && usedItem.is(ModItems.CUP_PERFECT_BLACK_TEA)) {
            if (player.getActiveEffects().isEmpty()) return;
            List<MobEffectInstance> effectList = player.getActiveEffects().stream().toList();
            player.removeAllEffects();
            for (MobEffectInstance effect : effectList) {
                player.getFoodData().eat(2, 2);
                if (!effect.getEffect().value().isBeneficial()) {
                    if (player.getAbsorptionAmount() > 0) {
                        player.setAbsorptionAmount(player.getAbsorptionAmount() + 2);
                    } else {
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, Math.min(effectList.size() * 1200, 6000), 10));
                        player.setAbsorptionAmount(2);
                    }
                }
            }
            serverLevel.playSound(null, player.getOnPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.0F);

        }

    }
}
