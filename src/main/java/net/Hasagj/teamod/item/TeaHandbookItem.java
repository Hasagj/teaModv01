package net.hasagj.teamod.item;

import net.hasagj.teamod.screen.custom.TeaHandbookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeaHandbookItem extends Item {
    public TeaHandbookItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            // открыть экран только на клиенте
            Minecraft.getInstance().setScreen(new TeaHandbookScreen());
        }
        return InteractionResult.SUCCESS;
    }
}
