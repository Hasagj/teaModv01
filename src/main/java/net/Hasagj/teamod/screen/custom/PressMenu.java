package net.hasagj.teamod.screen.custom;


import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.block.entity.PressBlockEntity;
import net.hasagj.teamod.screen.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PressMenu extends AbstractContainerMenu {

    public final PressBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;


    public PressMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(10));
    }

    public PressMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.PRESS_MENU.get(), id);
        this.blockEntity = (PressBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 0, 38, 16));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 1, 56, 16));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 2, 74, 16));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 3, 38, 34));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 4, 56, 34));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 5, 74, 34));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 6, 38, 52));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 7, 56, 52));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 8, 74, 52));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 9, 123, 34));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 10, 9, 34));

        addDataSlots(data);
    }


    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
            player, ModBlocks.PRESS.get());
    }
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
