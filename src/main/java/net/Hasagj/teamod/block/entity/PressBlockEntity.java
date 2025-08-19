package net.hasagj.teamod.block.entity;

import net.hasagj.teamod.block.custom.PressBlock;
import net.hasagj.teamod.recipe.ModRecipes;
import net.hasagj.teamod.recipe.PressRecipe;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.recipe.PressRecipeInput;
import net.hasagj.teamod.screen.custom.PressMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;



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
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        drops();
        super.preRemoveSideEffects(pos, state);
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

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory").get());
        progress = pTag.getInt("press.progress").get();
        maxProgress = pTag.getInt("press.max_progress").get();
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
        Optional<RecipeHolder<PressRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();

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

        Optional<RecipeHolder<PressRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().assemble(new PressRecipeInput(
                itemHandler.getStackInSlot(INPUT_SLOT1),
                itemHandler.getStackInSlot(INPUT_SLOT2),
                itemHandler.getStackInSlot(INPUT_SLOT3),
                itemHandler.getStackInSlot(INPUT_SLOT4),
                itemHandler.getStackInSlot(INPUT_SLOT5),
                itemHandler.getStackInSlot(INPUT_SLOT6),
                itemHandler.getStackInSlot(INPUT_SLOT7),
                itemHandler.getStackInSlot(INPUT_SLOT8),
                itemHandler.getStackInSlot(INPUT_SLOT9)), level.registryAccess());
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);

    }
    private Optional<RecipeHolder<PressRecipe>> getCurrentRecipe() {
        return ((ServerLevel)this.level).recipeAccess()
                .getRecipeFor(ModRecipes.PRESS_TYPE.get(),
                        new PressRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT1),
                        itemHandler.getStackInSlot(INPUT_SLOT2),
                        itemHandler.getStackInSlot(INPUT_SLOT3),
                        itemHandler.getStackInSlot(INPUT_SLOT4),
                        itemHandler.getStackInSlot(INPUT_SLOT5),
                        itemHandler.getStackInSlot(INPUT_SLOT6),
                        itemHandler.getStackInSlot(INPUT_SLOT7),
                        itemHandler.getStackInSlot(INPUT_SLOT8),
                        itemHandler.getStackInSlot(INPUT_SLOT9)),  level);
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
