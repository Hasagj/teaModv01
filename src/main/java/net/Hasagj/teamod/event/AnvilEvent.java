package net.hasagj.teamod.event;

import net.hasagj.teamod.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class AnvilEvent {
    public AnvilEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // Проверяем нужные предметы
        if (left.is(ModItems.BROKEN_RECOVERY_COMPASS) && right.is(Items.ECHO_SHARD)) {
            // Создаём новый предмет
            ItemStack result = new ItemStack(Items.RECOVERY_COMPASS);

            // Устанавливаем результат
            event.setOutput(result);

            // Цена в уровнях (пример — 10)
            event.setXpCost(25);
        }
    }
}
