package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hasagj.teamod.block.entity.ModBlockEntities;
import net.hasagj.teamod.block.entity.PressBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PressBlock extends BaseEntityBlock {
    public static final MapCodec<PressBlock> CODEC = simpleCodec(PressBlock::new);
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    public static final BooleanProperty HAS_ITEMS_INSIDE = BooleanProperty.create("has_items_inside");


    public PressBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HAS_ITEMS_INSIDE, false)));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PressBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PressBlockEntity pressBlockEntity) {
                pressBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof PressBlockEntity pressBlockEntity) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(pressBlockEntity, pressBlockEntity.getDisplayName()), pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }


        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntities.PRESS_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HAS_ITEMS_INSIDE});
    }

}
