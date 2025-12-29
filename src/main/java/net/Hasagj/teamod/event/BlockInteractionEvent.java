package net.hasagj.teamod.event;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


public class BlockInteractionEvent {
    public BlockInteractionEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();

        if (!level.isClientSide &&
                state.getBlock() == Blocks.CAMPFIRE &&
                heldItem.getItem() == Items.CAULDRON && !player.isCrouching()) {


            level.setBlock(pos, ModBlocks.CAULDRON_ON_FIRE.get().defaultBlockState(), 3);
            heldItem.shrink(1);

            level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            event.setCanceled(true);
        }
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ConduitBlockEntity conduit &&
                conduit.isActive() &&
                heldItem.is(ModItems.BROKEN_NAUTILUS_CUP) && player.getItemInHand(InteractionHand.OFF_HAND).is(Items.NAUTILUS_SHELL)) {

            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.NAUTILUS_CUP.get()));
            player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);

            level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "sculk"));
        if (level instanceof ServerLevel serverLevel && level.getBlockState(pos).is(tag) && player.hasEffect(ModEffects.RESONANCE_EFFECT) && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && (player.getFoodData().getFoodLevel() != 20 || player.getHealth() != player.getMaxHealth())) {
            if (level.getBlockState(pos).getBlock() instanceof SculkShriekerBlock) {
               player.addEffect(new MobEffectInstance(ModEffects.RESONANCE_EFFECT, player.getEffect(ModEffects.RESONANCE_EFFECT).getDuration() < 24000 ? player.getEffect(ModEffects.RESONANCE_EFFECT).getDuration() + 400 : player.getEffect(ModEffects.RESONANCE_EFFECT).getDuration(), 0));
            }
            BlockParticleOption particleOption = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos));
            serverLevel.sendParticles(particleOption,
                    pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                    30, 0.5F, 0.5F, 0.5F, 1);
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
            serverLevel.playSound(null, pos, SoundEvents.GENERIC_EAT.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            player.heal(1);
            player.getFoodData().eat(1, 1);
        }

        if (level instanceof ServerLevel serverLevel && level.getBlockState(pos).is(Blocks.REINFORCED_DEEPSLATE) && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.MACE) && player.getItemInHand(InteractionHand.OFF_HAND).is(ModItems.YIXING_CLAY)) {
            player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
            player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(100, player, EquipmentSlot.MAINHAND);
            serverLevel.sendParticles(ParticleTypes.GUST_EMITTER_LARGE, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 7, 2, 2, 2, 1);
            serverLevel.playSound(null, pos, SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.destroyBlock(pos, false);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.BROKEN_REINFORCED_CUP.get())));
            player.hurtMarked = true;
            player.knockback(2, player.getLookAngle().normalize().x(), player.getLookAngle().normalize().z());
        }
        if (level instanceof ServerLevel serverLevel && level.getBlockState(pos).is(Blocks.REINFORCED_DEEPSLATE) && player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.BROKEN_REINFORCED_CUP) && player.getItemInHand(InteractionHand.OFF_HAND).is(Items.ECHO_SHARD)) {
            player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.REINFORCED_CUP.get()));
            ShriekParticleOption particleOption = new ShriekParticleOption(1);
            serverLevel.sendParticles(particleOption, pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, 7, 0, 0, 0, 1);
            serverLevel.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, Blocks.DEEPSLATE.defaultBlockState(), 1);
        }


    }
}
