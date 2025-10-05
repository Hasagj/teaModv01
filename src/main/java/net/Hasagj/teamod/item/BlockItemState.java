package net.hasagj.teamod.item;

import net.hasagj.teamod.block.custom.ChakhaiBlockTest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class BlockItemState extends BlockItem {
    private final int tea;

    public BlockItemState(Block block, int tea, Properties properties) {
        super(block, properties);
        this.tea = tea;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        if (!level.isClientSide) {
            // Берём уже установленный state (с учётом FACING и прочего)
            BlockState placedState = level.getBlockState(pos);

            // Если у блока есть свойство COLOR — меняем только его
            if (placedState.hasProperty(ChakhaiBlockTest.IS_TEA_INSIDE)) {
                placedState = placedState.setValue(ChakhaiBlockTest.IS_TEA_INSIDE, tea);
                level.setBlock(pos, placedState, 3);
            }
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}