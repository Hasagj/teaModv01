package net.hasagj.teamod.event;

import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.particle.ModParticles;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.EnchantmentActiveCheck;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnTickEvent {
    public OnTickEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    private final Map<UUID, Integer> mapTimer = new HashMap<>();
    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel world)) return;
        for (ServerPlayer player : world.players()) {
            if (player.hasEffect(ModEffects.GARDENS_BLESSING_EFFECT)) {
                double randomRadius = RandomSource.create().nextInt(0, 2);
                if (RandomSource.create().nextInt(100) == 0) {
                    world.sendParticles(ModParticles.ORANGE_EYES_PARTICLES.get(), player.getX() + randomRadius, player.getY() + randomRadius / 4, player.getZ() + randomRadius, 0, 0, 0, 0, 0);
                }
                // Радиус в блоках
                double radius = 30.0;

                // Список всех живых мобов в радиусе
                List<LivingEntity> mobs = world.getEntitiesOfClass(LivingEntity.class,
                        player.getBoundingBox().inflate(radius),
                        entity -> entity != player && entity instanceof Enemy);

                for (LivingEntity mob : mobs) {
                    if (!mob.hasEffect(MobEffects.GLOWING)) {

                        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, true, false));

                    }
                    if (RandomSource.create().nextInt(100) == 0) {
                        world.sendParticles(ModParticles.ORANGE_EYES_PARTICLES.get(), mob.getX() + randomRadius, mob.getY() + randomRadius / 4, mob.getZ() + randomRadius, 0, 0, 0, 0, 0);
                    }


                }

            }

            if (player.hasEffect(ModEffects.BITTER_EFFECT)) {
                player.removeEffect(MobEffects.SATURATION);
                player.removeEffect(ModEffects.POTENTIAL_EFFECT);
                player.removeEffect(ModEffects.APPETITE_EFFECT);
                player.removeEffect(ModEffects.SWEET_EFFECT);

            }

            if (player.hasEffect(ModEffects.THORNY_EFFECT)) {
                if (player.getItemBySlot(EquipmentSlot.CHEST).is(Items.NETHERITE_CHESTPLATE)) {
                    var enchantments = player.getItemBySlot(EquipmentSlot.CHEST).get(DataComponents.ENCHANTMENTS);
                    if (enchantments != null) {
                        if (enchantments.keySet().stream().anyMatch(holder -> holder.is(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("thorns"))))) {
                            ModTriggers.BRISTLEBACK_TRIGGER.get().trigger(player);
                        }
                    }
                }
            }



            if (!player.hasEffect(ModEffects.THORNY_EFFECT) && player.getStingerCount() >= 20) {
                player.setStingerCount(0);
            }


            
        }

//        КОД ДЛЯ ПОЛУЧЕНИЯ ПРЕДМЕТА ПО ЕГО РЕЦЕПТУ
        RecipeManager recipeManager = world.getServer().getRecipeManager();
        for (ServerPlayer player : world.players()) {
            if (player.hasEffect(ModEffects.PRIMORDIAL_FLAME_EFFECT) && !world.isClientSide) {
                for (ItemEntity item : world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(10))) {
                    Optional<RecipeHolder<SmeltingRecipe>> recipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(item.getItem()), world);

                    if (!mapTimer.containsKey(item.getUUID()) && recipe.isPresent()) {
                        mapTimer.put(item.getUUID(), (int) Math.floor(item.getItem().getCount() * 0.125D) * 40 + 140);
                    }
                }
                for (ItemEntity item : world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(10))) {
                    Optional<RecipeHolder<SmeltingRecipe>> recipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(item.getItem()), world);
                    if (player.getFoodData().getFoodLevel() - (int)Math.floor(item.getItem().getCount() * 0.125D) >= 6 && !mapTimer.isEmpty() && mapTimer.get(item.getUUID()) != null) {
                        if (mapTimer.get(item.getUUID()) == 0 && mapTimer.get(item.getUUID()) != null) {
                            if (item.getItem().has(DataComponents.FOOD)) {
                                ModTriggers.FLAME_TRIGGER.get().trigger(player);
                            }
                            recipe.ifPresent(smeltingRecipeRecipeHolder -> item.setItem(new ItemStack(item.getItem().has(DataComponents.FOOD) ? Items.CHARCOAL : smeltingRecipeRecipeHolder.value().assemble(new SingleRecipeInput(item.getItem()), HolderLookup.Provider.create(Stream.of())).getItem(), item.getItem().getCount())));
                            mapTimer.remove(item.getUUID());
                            world.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                    item.getX(), item.getY(), item.getZ(),
                                    15,
                                    0.1, 0.1, 0.1,
                                    0);
                            player.getFoodData().eat((int)Math.floor(item.getItem().getCount() * 0.125D) > 0 ? (int)Math.floor(item.getItem().getCount() * 0.125D) * -1 : -1, 0);
                            break;
                        } else if (mapTimer.get(item.getUUID()) != null) {
                            mapTimer.replace(item.getUUID(), mapTimer.get(item.getUUID()) - 1);
                            world.sendParticles(ParticleTypes.SMOKE,
                                    item.getX(), item.getY(), item.getZ(),
                                    1,
                                    0.1, 0.1, 0.1,
                                    0);
                            if (mapTimer.get(item.getUUID()) % 5 == 0) {
                                world.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                        item.getX(), item.getY(), item.getZ(),
                                        1,
                                        0.1, 0.1, 0.1,
                                        0);
                            }
                        }
                    }
                }
            }
        }
    }

}
