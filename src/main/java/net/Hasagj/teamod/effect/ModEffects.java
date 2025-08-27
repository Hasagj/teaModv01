package net.hasagj.teamod.effect;

import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.particle.ModParticles;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, TeaMod.MOD_ID);

    public static final Holder<MobEffect> SOLID_EFFECT = MOB_EFFECTS.register("solid",
            () -> new SolidEffect(MobEffectCategory.BENEFICIAL, 0x888888)
                    .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "solid"), 10, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "solid"), 6, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "solid"), 2, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> DROWSY_EFFECT = MOB_EFFECTS.register("drowsy",
            () -> new DrowsyEffect(MobEffectCategory.NEUTRAL, 0xae34eb)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "drowsy"), -0.95, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "drowsy"), -0.95, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "drowsy"), -0.95, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "drowsy"), -0.95, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> GARDENS_BLESSING_EFFECT = MOB_EFFECTS.register("gardens_blessing",
            () -> new GardensBlessingEffect(MobEffectCategory.BENEFICIAL, 0xff8c00));
    public static final Holder<MobEffect> POTENTIAL_EFFECT = MOB_EFFECTS.register("potential",
            () -> new PotentialEffect(MobEffectCategory.NEUTRAL, 0x0000FF));
    public static final Holder<MobEffect> APPETITE_EFFECT = MOB_EFFECTS.register("appetite",
            () -> new AppetiteEffect(MobEffectCategory.BENEFICIAL, 0x0000FF));
    public static final Holder<MobEffect> SWEET_EFFECT = MOB_EFFECTS.register("sweet",
            () -> new SweetEffect(MobEffectCategory.BENEFICIAL, 0x0000FF));
    public static final Holder<MobEffect> THORNY_EFFECT = MOB_EFFECTS.register("thorny",
            () -> new ThornyEffect(MobEffectCategory.BENEFICIAL, 0x006734)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "thorny"), -100, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> BITTER_EFFECT = MOB_EFFECTS.register("bitter",
            () -> new BitterEffect(MobEffectCategory.HARMFUL, 0x000000));
    public static final Holder<MobEffect> ENDS_BLESSING_EFFECT = MOB_EFFECTS.register("ends_blessing",
            () -> new EndsBlessingEffect(MobEffectCategory.BENEFICIAL, 0xC71585));
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
