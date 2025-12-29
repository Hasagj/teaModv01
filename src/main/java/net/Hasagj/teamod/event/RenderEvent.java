package net.hasagj.teamod.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.effect.ModEffects;
import net.hasagj.teamod.item.ModItems;
import net.hasagj.teamod.trigger.ModTriggers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.Objects;

public class RenderEvent {
    public RenderEvent() {
        // Регистрируем событие в NeoForge
        NeoForge.EVENT_BUS.register(this);
    }
    private int prevFood = -1;
    private int iter = 1;
    private int frame = 1;
    @SubscribeEvent
    public void onRenderHearts(RenderGuiLayerEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        GuiGraphics graphics = event.getGuiGraphics();
        if (player == null) return;
        if (player.hasEffect(ModEffects.SWEET_EFFECT) && !player.isSpectator() && !player.isCreative()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2 - 89;
            int top = mc.getWindow().getGuiScaledHeight() - 43;
            double hearts = player.getFoodData().getFoodLevel();
            for (int i = 0; i < hearts; i++) {
                int x = i % 2 == 0 ? left - 4 + i * 4 : left - 4 + (i - 1) * 4;
                int y;
                if (player.hasEffect(MobEffects.REGENERATION) && (mc.gui.getGuiTicks()) % Mth.ceil(player.getHealth() + 5.0F) == i / 2) {
                    y = top - 2;
                } else y = top;
                if (i % 2 != 0) {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/sweet/sweet_full.png"), x, y, 0, 0, 13, 15, 13, 15);
                } else {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/sweet/sweet_half.png"), x, y, 0, 0, 13, 15, 13, 15);
                }
            }
        }

        if (player.hasEffect(ModEffects.PRIMORDIAL_FLAME_EFFECT) && !player.isSpectator() && !player.isCreative()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2 + 9;
            int top = mc.getWindow().getGuiScaledHeight() - 40;
            int food = player.getFoodData().getFoodLevel();
            for (int i = 20 - food; i <= 19; i++) {
                int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                int y = top;
                if (i % 2 == 0) {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/flame/flame_full.png"), x, y, 0, 0, 11, 11, 11, 11);
                } else {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/flame/flame_half.png"), x, y, 0, 0, 11, 11, 11, 11);
                }
            }
            if (prevFood == -1 || food > prevFood) {
                prevFood = food;
                return;
            }
            if (food < prevFood) {
                for (int i = 20 - prevFood; i < 20 - food; i++) {
                    int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                    int y = top - 2;
                    if (i % 2 == 0) {
                        graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/flame/flame_full_to_half" + frame + ".png"), x, y, 0, 0, 11, 13, 13, 13);
                    } else {
                        graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/flame/flame_half_to_empty" + frame + ".png"), x, y, 0, 0, 11, 13, 11, 13);
                    }
                }
                iter++;
                if (iter % 60 == 0) {
                    frame++;
                }
                if (frame == 6) {
                    frame = 1;
                    iter = 1;
                    prevFood = food;
                }
            }

        } else if (player.hasEffect(ModEffects.ENDS_BLESSING_EFFECT) && !player.isSpectator() && !player.isCreative()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2 + 10;
            int top = mc.getWindow().getGuiScaledHeight() - 39;
            int food = player.getFoodData().getFoodLevel();
            for (int i = 20 - food; i <= 19; i++) {
                int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                int y = top;
                if (i % 2 == 0) {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/end/end_full.png"), x, y, 0, 0, 9, 9, 9, 9);
                } else {
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/end/end_half.png"), x, y, 0, 0, 9, 9, 9, 9);
                }
            }
            if (prevFood == -1 || food > prevFood) {
                prevFood = food;
                return;
            }
            if (food < prevFood) {
                for (int i = 20 - prevFood; i < 20 - food; i++) {
                    int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                    int y = top - 3;
                    if (i % 2 == 0) {
                        graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/end/end_full_to_half" + frame + ".png"), x - 3, y, 0, 0, 15, 15, 15, 15);
                    } else {
                        graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/end/end_half_to_empty" + frame + ".png"), x - 3, y, 0, 0, 15, 15, 15, 15);
                    }
                }
                iter++;
                if (iter % 60 == 0) {
                    frame++;
                }
                if (frame == 10) {
                    frame = 1;
                    iter = 1;
                    prevFood = food;
                }
            }
        } else if (player.hasEffect(ModEffects.APPETITE_EFFECT) && !player.isSpectator() && !player.isCreative()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2 + 10;
            int top = mc.getWindow().getGuiScaledHeight() - 39;
            int food = player.getFoodData().getFoodLevel();
            if (prevFood == -1 || food < prevFood) {
                prevFood = food;
                return;
            }
            if (food > prevFood) {
                for (int i = 20 - food ; i < (20 - food) + (food - prevFood) * 0.5 - 2; i++) {
                    int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                    int y = top - 4;
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/appetite/appetite" + frame + ".png"), x - 4, y, 0, 0, 15, 15, 15, 15);
                }
                iter++;
                if (iter % 60 == 0) {
                    frame++;
                }
                if (frame == 16) {
                    frame = 1;
                    iter = 1;
                    prevFood = food;
                }
            }
        } else if (player.hasEffect(ModEffects.BITTER_EFFECT) && !player.isSpectator() && !player.isCreative()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2 + 10;
            int top = mc.getWindow().getGuiScaledHeight() - 39;
            int food = player.getFoodData().getFoodLevel();
            if (prevFood == -1 || food < prevFood) {
                prevFood = food;
                return;
            }
            if (food > prevFood) {
                int foodDecrease = FinishUseEvent.bitterNutrition > 2 ? (int)(FinishUseEvent.bitterNutrition * 0.75) : FinishUseEvent.bitterNutrition - 1;
                for (int i = 20 - food - FinishUseEvent.bitterNutrition + 2 > 0 ? 20 - food - FinishUseEvent.bitterNutrition + 2 : 0; i < (20 - food) + (FinishUseEvent.bitterNutrition - foodDecrease) - 2 ; i++) {
                    int x = i % 2 == 0 ? left + i * 4 : left + (i - 1) * 4;
                    int y = top - 3;
                    graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/bitter/bitter" + frame + ".png"), x - 3, y, 0, 0, 15, 15, 15, 15);
                }
                iter++;
                if (iter % 60 == 0) {
                    frame++;
                }
                if (frame == 11) {
                    frame = 1;
                    iter = 1;
                    prevFood = food;
                }
            }
        }
        if (player.hasEffect(ModEffects.RESONANCE_EFFECT) && !player.isSpectator()) {
            int left = mc.getWindow().getGuiScaledWidth() / 2;
            int top = mc.getWindow().getGuiScaledHeight() / 2;
            if (OnClientTickEvent.HOLD_TIME != 0) {
                graphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/hud/charge/charge" + (int)(OnClientTickEvent.HOLD_TIME / 20 + 1) + ".png"), left - 16, top - 16, 0, 0, 32, 32, 32, 32);

            }

        }

    }

}
