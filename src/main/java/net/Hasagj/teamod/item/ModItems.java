package net.hasagj.teamod.item;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.block.custom.ChakhaiBlackTeaBlock;
import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TeaMod.MOD_ID);


    public static final DeferredItem<Item> TEA_LEAF = ITEMS.registerItem("tea_leaf",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> LIGHTLY_DRIED_TEA_LEAF = ITEMS.registerItem("lightly_dried_tea_leaf",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> DRIED_TEA_LEAF = ITEMS.registerItem("dried_tea_leaf",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> BLACK_TEA_LEAVES = ITEMS.registerItem("black_tea_leaves",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> PALE_TEA_LEAVES = ITEMS.registerItem("pale_tea_leaves",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> DAISY_TEA_LEAVES = ITEMS.registerItem("daisy_tea_leaves",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> GREEN_TEA_LEAVES = ITEMS.registerItem("green_tea_leaves",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> TEA_SEEDS = ITEMS.registerItem("tea_seeds",
            (properties) -> new BlockItem(ModBlocks.TEA_BUSH.get(), properties));
    public static final DeferredItem<Item> HIBISCUS_FLOWER = ITEMS.registerItem("hibiscus_flower",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> DRIED_HIBISCUS_PETALS = ITEMS.registerItem("dried_hibiscus_petals",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> DRIED_DAISY = ITEMS.registerItem("dried_daisy",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> DRIED_PITCHER_PLANT = ITEMS.registerItem("dried_pitcher_plant",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> CACTUS_TEA_LEAVES = ITEMS.registerItem("cactus_tea_leaves",
            (properties) -> new Item(properties));
    public static final DeferredItem<Item> CHORUS_TEA_LEAVES = ITEMS.registerItem("chorus_tea_leaves",
            (properties) -> new Item(properties));

    public static final DeferredItem<Item> TEA_HANDBOOK = ITEMS.registerItem("tea_handbook",
            (properties) -> new TeaHandbookItem(properties.stacksTo(1)));


    public static final DeferredItem<Item> APPLE_SLICE = ITEMS.registerItem("apple_slice",
            (properties) -> new Item(properties.food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.15f).build())));
    public static final DeferredItem<Item> DRIED_APPLE_SLICE = ITEMS.registerItem("dried_apple_slice",
            (properties) -> new Item(properties.food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.15f).build())));
    public static final DeferredItem<Item> PITCHER_TURNIP = ITEMS.registerItem("pitcher_turnip",
            (properties) -> new Item(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(1f).build())));

    public static final DeferredItem<Item> BOILED_WATER = ITEMS.registerItem("boiled_water",
            (properties) -> new Item(properties));

    public static final DeferredItem<Item> RAW_CUP = ITEMS.registerItem("raw_cup",
            (properties) -> new Item(properties.stacksTo(1)));
    public static final DeferredItem<Item> CUP = ITEMS.registerItem("cup",
            (properties) -> new Item(properties.stacksTo(1)));
    public static final DeferredItem<Item> CUP_GREEN_TEA = ITEMS.registerItem("cup_green_tea",
            (properties) -> new Item(properties.stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE).build())
                    .usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_green_tea.tooltip").withColor(0xADFF2F));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> CUP_BLACK_TEA = ITEMS.registerItem("cup_black_tea",
            (properties) -> new Item(properties.stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE).build())
                            .usingConvertsTo(CUP.get())){

                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_black_tea.tooltip").withColor(0xA0522D));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);
                }
            }
    );
    public static final DeferredItem<Item> CUP_HIBISCUS_TEA = ITEMS.registerItem("cup_hibiscus_tea",
            (properties) -> new Item(properties.stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.SOLID_EFFECT, 2400, 0))).build())
                    .usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_hibiscus_tea.tooltip").withColor(0xFF0000));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);
                }
            }
    );

    public static final DeferredItem<Item> CUP_DAISY_TEA = ITEMS.registerItem("cup_daisy_tea",
            (properties) -> new Item(properties.stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.DROWSY_EFFECT, 1200, 0))).build())
                    .usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_daisy_tea.tooltip").withColor(0xFFD700));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);
                }
            });

    public static final DeferredItem<Item> CUP_PALE_TEA = ITEMS.registerItem("cup_pale_tea",
            (properties) -> new Item(properties.stacksTo(1)
                    .component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.GARDENS_BLESSING_EFFECT, 6000, 0, true, false)))
                            .soundAfterConsume(Holder.direct(SoundEvents.CREAKING_ACTIVATE)).build()).usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_pale_tea.tooltip").withColor(0xFF4500));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);

                }
            });
    public static final DeferredItem<Item> CUP_PITCHER_TEA = ITEMS.registerItem("cup_pitcher_tea",
            (properties) -> new Item(properties.stacksTo(1)
                    .component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.POTENTIAL_EFFECT, 240)))
                            .build()).usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_pitcher_tea.tooltip").withColor(0x0000FF));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);

                }
            });
    public static final DeferredItem<Item> CUP_CACTUS_TEA = ITEMS.registerItem("cup_cactus_tea",
            (properties) -> new Item(properties.stacksTo(1)
                    .component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.THORNY_EFFECT, 2400)))
                            .build()).usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_cactus_tea.tooltip").withColor(0x006734));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);

                }
            });
    public static final DeferredItem<Item> CUP_CHORUS_TEA = ITEMS.registerItem("cup_chorus_tea",
            (properties) -> new Item(properties.stacksTo(1)
                    .component(DataComponents.CONSUMABLE, Consumables.defaultDrink()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.ENDS_BLESSING_EFFECT, 6000)))
                            .build()).usingConvertsTo(CUP.get())) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.cup_chorus_tea.tooltip").withColor(0xC71585));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);

                }
            });

    public static final DeferredItem<Item> TEA_POT = ITEMS.registerItem("tea_pot",
            (properties) -> new BlockItem(ModBlocks.TEA_POT_BLOCK.get(), properties.stacksTo(1)));

    public static final DeferredItem<Item> RAW_TEA_POT = ITEMS.registerItem("raw_tea_pot",
            (properties) -> new Item(properties.stacksTo(1)));





    public static final DeferredItem<Item> CHAKHAI = ITEMS.registerItem("chakhai",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_GREEN_TEA = ITEMS.registerItem("chakhai_green_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_GREEN_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_BLACK_TEA = ITEMS.registerItem("chakhai_black_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_BLACK_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_HIBISCUS_TEA = ITEMS.registerItem("chakhai_hibiscus_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_HIBISCUS_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_DAISY_TEA = ITEMS.registerItem("chakhai_daisy_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_DAISY_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_PALE_TEA = ITEMS.registerItem("chakhai_pale_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_PALE_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_PITCHER_TEA = ITEMS.registerItem("chakhai_pitcher_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_PITCHER_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_CACTUS_TEA = ITEMS.registerItem("chakhai_cactus_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_CACTUS_TEA_BLOCK.get(), properties.stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_CHORUS_TEA = ITEMS.registerItem("chakhai_chorus_tea",
            (properties) -> new BlockItem(ModBlocks.CHAKHAI_CHORUS_TEA_BLOCK.get(), properties.stacksTo(1)));

    public static final DeferredItem<Item> RAW_CHAKHAI = ITEMS.registerItem("raw_chakhai",
            (properties) -> new Item(properties.stacksTo(1)));
    public static final DeferredItem<Item> MOON_RAVEN = ITEMS.registerItem("moon_raven",
            (properties) -> new BlockItem(ModBlocks.MOON_RAVEN_BLOCK.get(), properties.stacksTo(1).rarity(Rarity.RARE)){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag tooltipFlag) {
                    component.accept(Component.translatable("tooltip.teamod.moon_raven.tooltip").withColor(0x808080));
                    super.appendHoverText(stack, context, display, component, tooltipFlag);

                }
            }) ;



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
