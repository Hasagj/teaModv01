package net.hasagj.teamod.event;

import com.google.common.collect.ImmutableList;
import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntitySpawnEvent {
    public EntitySpawnEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onTrade(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof WanderingTrader trader && !event.getLevel().isClientSide) {
            RandomSource rand = event.getLevel().random;
            MerchantOffers offers = trader.getOffers();
            MerchantOffer newOffer = new MerchantOffer(
                    new ItemCost(Items.EMERALD, 9 + rand.nextInt(6)),
                    Optional.of(new ItemCost(ModItems.LIGHTLY_DRIED_TEA_LEAF.get(), rand.nextInt(5) + 1)),
                    new ItemStack(ModItems.STRANGE_PETALS.get(), 1), // Продаёт
                    1, rand.nextInt(7) + 1, 0.05F
            );
            if (!offers.getFirst().getResult().is(ModItems.STRANGE_PETALS)) {
                offers.addFirst(newOffer);
            }
        }
    }
}
