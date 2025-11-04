package net.hasagj.teamod.event;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import java.util.Optional;


public class FishingEvent {
    public FishingEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onFish(ItemFishedEvent event) {
        if (!(event.getHookEntity().level() instanceof ServerLevel level)) return;
        BlockPos hookPos = event.getHookEntity().blockPosition();
        Player player = event.getEntity();
        RandomSource random = level.getRandom();

        ResourceLocation lootId = ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "well_fishing");
        ResourceKey<LootTable> lootKey = ResourceKey.create(Registries.LOOT_TABLE, lootId);
        LootTable loottable = level.getServer().reloadableRegistries().getLootTable(lootKey);
        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withLuck(player.getLuck())
                .withParameter(LootContextParams.THIS_ENTITY, player);

        Optional<Holder.Reference<Structure>> village = level.registryAccess()
                .lookupOrThrow(Registries.STRUCTURE)
                .get(ResourceLocation.withDefaultNamespace("village_plains"));

        if (village.isEmpty()) return;

        StructureStart start = level.structureManager().getStructureAt(hookPos, village.get().value());
        BoundingBox box = start.getPieces().getFirst().getBoundingBox();
        if (start.isValid() && (box.getLength().equals(new Vec3i(8, 3, 8)) || box.getLength().equals(new Vec3i(9, 6, 9))) && box.isInside(hookPos.getX(), hookPos.getY(), hookPos.getZ())) {
            event.setCanceled(true);
            ItemStack stack = loottable.getRandomItems(params.create(ContextKeySet.EMPTY)).getFirst();
            ItemEntity itementity = new ItemEntity(event.getHookEntity().level(), event.getHookEntity().getX(), event.getHookEntity().getY(), event.getHookEntity().getZ(), stack);
            double d0 = player.getX() - event.getHookEntity().getX();
            double d1 = player.getY() - event.getHookEntity().getY();
            double d2 = player.getZ() - event.getHookEntity().getZ();
            double d3 = 0.1;
            itementity.setDeltaMovement(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
            event.getHookEntity().level().addFreshEntity(itementity);
            player.level().addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + (double)0.5F, player.getZ() + (double)0.5F, random.nextInt(6) + 1));
        }
    }
}


