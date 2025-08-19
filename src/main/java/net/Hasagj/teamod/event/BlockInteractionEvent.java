package net.hasagj.teamod.event;

import net.hasagj.teamod.block.ModBlocks;
import net.minecraft.core.BlockPos;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

            // Пример действия: заменить камень на светящийся блок
            level.setBlock(pos, ModBlocks.CAULDRON_ON_FIRE.get().defaultBlockState(), 3);
            heldItem.shrink(1);
            // Звук
            level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);

            // Предотвратить стандартное поведение
            event.setCanceled(true);
        }
    }
}
