package net.hasagj.teamod.item;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.block.custom.TeaPotBlock;
import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.hasagj.teamod.block.custom.TeaPotBlock.WHAT_LEAVES_INSIDE;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TeaMod.MOD_ID);


    public static final DeferredItem<Item> TEA_LEAF = ITEMS.register("tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTLY_DRIED_TEA_LEAF = ITEMS.register("lightly_dried_tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRIED_TEA_LEAF = ITEMS.register("dried_tea_leaf",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACK_TEA_LEAVES = ITEMS.register("black_tea_leaves",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> GREEN_TEA_LEAVES = ITEMS.register("green_tea_leaves",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> RAW_CUP = ITEMS.register("raw_cup",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> TEA_SEEDS = ITEMS.register("tea_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TEA_BUSH.get(), new Item.Properties()));
    public static final DeferredItem<Item> HIBISCUS = ITEMS.register("hibiscus",
            () -> new ItemNameBlockItem(ModBlocks.HIBISCUS_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> HIBISCUS_FLOWER = ITEMS.register("hibiscus_flower",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRIED_HIBISCUS_PETALS = ITEMS.register("dried_hibiscus_petals",
            () -> new Item(new Item.Properties()));




    public static final DeferredItem<Item> CUP = ITEMS.register("cup",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CUP_GREEN_TEA = ITEMS.register("cup_green_tea",
            () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    if (entityLiving instanceof ServerPlayer serverplayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                        serverplayer.awardStat(Stats.ITEM_USED.get(this));
                    }

                    if (!level.isClientSide) {
                        entityLiving.removeEffectsCuredBy(EffectCures.HONEY);
                    }

                    if (entityLiving instanceof Player player) {
                        return ItemUtils.createFilledResult(stack, player, new ItemStack((ItemLike)CUP), false);
                    } else {
                        stack.consume(1, entityLiving);
                        return stack;
                    }
                }

                @Override
                public int getUseDuration(ItemStack stack, LivingEntity entity) {
                    return 32;
                }

                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }

                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    return ItemUtils.startUsingInstantly(level, player, hand);
                }

                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.teamod.cup_green_tea.tooltip").withColor(808080));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> CUP_BLACK_TEA = ITEMS.register("cup_black_tea",
            () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    if (entityLiving instanceof ServerPlayer serverplayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                        serverplayer.awardStat(Stats.ITEM_USED.get(this));
                    }

                    if (!level.isClientSide) {
                        entityLiving.removeEffectsCuredBy(EffectCures.MILK);
                    }

                    if (entityLiving instanceof Player player) {
                        return ItemUtils.createFilledResult(stack, player, new ItemStack((ItemLike)CUP), false);
                    } else {
                        stack.consume(1, entityLiving);
                        return stack;
                    }
                }

                @Override
                public int getUseDuration(ItemStack stack, LivingEntity entity) {
                    return 32;
                }

                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }

                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    return ItemUtils.startUsingInstantly(level, player, hand);
                }
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.teamod.cup_black_tea.tooltip").withColor(808080));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
    );
    public static final DeferredItem<Item> CUP_HIBISCUS_TEA = ITEMS.register("cup_hibiscus_tea",
            () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    if (entityLiving instanceof ServerPlayer serverplayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                        serverplayer.awardStat(Stats.ITEM_USED.get(this));
                    }

                    if (!level.isClientSide) {
                        entityLiving.addEffect(new MobEffectInstance(ModEffects.SOLID_EFFECT, 2400));
                    }

                    if (entityLiving instanceof Player player) {
                        return ItemUtils.createFilledResult(stack, player, new ItemStack((ItemLike)CUP), false);
                    } else {
                        stack.consume(1, entityLiving);
                        return stack;
                    }
                }

                @Override
                public int getUseDuration(ItemStack stack, LivingEntity entity) {
                    return 32;
                }

                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }

                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    return ItemUtils.startUsingInstantly(level, player, hand);
                }
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.teamod.cup_hibiscus_tea.tooltip").withColor(808080));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
    );



    public static final DeferredItem<Item> TEA_POT = ITEMS.register("tea_pot",
            () -> new ItemNameBlockItem(ModBlocks.TEA_POT_BLOCK.get(), new Item.Properties().stacksTo(1)));



    public static final DeferredItem<Item> RAW_TEA_POT = ITEMS.register("raw_tea_pot",
            () -> new Item(new Item.Properties().stacksTo(1)));



    public static final DeferredItem<Item> CHAKHAI = ITEMS.register("chakhai",
            () -> new ItemNameBlockItem(ModBlocks.CHAKHAI_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_GREEN_TEA = ITEMS.register("chakhai_green_tea",
            () -> new ItemNameBlockItem(ModBlocks.CHAKHAI_GREEN_TEA_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_BLACK_TEA = ITEMS.register("chakhai_black_tea",
            () -> new ItemNameBlockItem(ModBlocks.CHAKHAI_BLACK_TEA_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHAKHAI_HIBISCUS_TEA = ITEMS.register("chakhai_hibiscus_tea",
            () -> new ItemNameBlockItem(ModBlocks.CHAKHAI_HIBISCUS_TEA_BLOCK.get(), new Item.Properties().stacksTo(1)));


    public static final DeferredItem<Item> RAW_CHAKHAI = ITEMS.register("raw_chakhai",
            () -> new Item(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
