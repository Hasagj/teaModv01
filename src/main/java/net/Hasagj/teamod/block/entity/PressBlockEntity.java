package net.hasagj.teamod.block.entity;

import net.hasagj.teamod.block.custom.PressBlock;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.screen.custom.PressMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PressBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(11) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 11);
            }
        }
    };

    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;
    private static final int INPUT_SLOT4 = 3;
    private static final int INPUT_SLOT5 = 4;
    private static final int INPUT_SLOT6 = 5;
    private static final int INPUT_SLOT7 = 6;
    private static final int INPUT_SLOT8 = 7;
    private static final int INPUT_SLOT9 = 8;
    private static final int OUTPUT_SLOT = 9;
    private static final int PAPER_SLOT = 10;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 600;



    public PressBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PRESS_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> PressBlockEntity.this.progress;
                    case 1 -> PressBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: PressBlockEntity.this.progress = value;
                    case 1: PressBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.teamod.press");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {

        return new PressMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("press.progress", progress);
        pTag.putInt("press.max_progress", maxProgress);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("press.progress");
        maxProgress = pTag.getInt("press.max_progress");
    }

    public void tick(Level level, BlockPos pos, BlockState blockState) {
        if (hasRecipe() || hasOutput()) {
            level.setBlockAndUpdate(getBlockPos(), blockState.setValue(PressBlock.HAS_ITEMS_INSIDE, true));
        }else {level.setBlockAndUpdate(getBlockPos(), blockState.setValue(PressBlock.HAS_ITEMS_INSIDE, false));}
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, pos, blockState);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        }else {
            resetProgress();
        }
    }

    private void craftItem() {
        ItemStack output = new ItemStack(Items.BARRIER, 1);
        itemHandler.extractItem(PAPER_SLOT, 1, false);
        itemHandler.extractItem(INPUT_SLOT1, 1, false);
        itemHandler.extractItem(INPUT_SLOT2, 1, false);
        itemHandler.extractItem(INPUT_SLOT3, 1, false);
        itemHandler.extractItem(INPUT_SLOT4, 1, false);
        itemHandler.extractItem(INPUT_SLOT5, 1, false);
        itemHandler.extractItem(INPUT_SLOT6, 1, false);
        itemHandler.extractItem(INPUT_SLOT7, 1, false);
        itemHandler.extractItem(INPUT_SLOT8, 1, false);
        itemHandler.extractItem(INPUT_SLOT9, 1, false);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }


    public boolean hasRecipe() {

        ItemStack output = new ItemStack(ModItems.TEA_LEAF.get(), 8);

        return itemHandler.getStackInSlot(INPUT_SLOT1).is(ModItems.DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT2).is(ModItems.DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT3).is(ModItems.DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT4).is(ModItems.LIGHTLY_DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT5).is(ModItems.LIGHTLY_DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT6).is(ModItems.LIGHTLY_DRIED_TEA_LEAF)
                && itemHandler.getStackInSlot(INPUT_SLOT7).is(ModItems.DRIED_HIBISCUS_PETALS)
                && itemHandler.getStackInSlot(INPUT_SLOT8).is(ModItems.DRIED_HIBISCUS_PETALS)
                && itemHandler.getStackInSlot(INPUT_SLOT9).is(ModItems.DRIED_HIBISCUS_PETALS)
                && itemHandler.getStackInSlot(PAPER_SLOT).is(Items.PAPER)

                && canInsertAmountIntoOutputSlot(output.getCount())
                && canInsertItemIntoOutputSlot(output);

    }

    public boolean hasOutput() {
        return !itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()
                || itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();
        return maxCount >= currentCount + count;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


}
