package net.hasagj.teamod.damage;

import net.hasagj.teamod.TeaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> WATER_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "water_damage"));
    public static final ResourceKey<DamageType> BOIL_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "boil_damage"));

    public static DamageSource water(ServerLevel level) {
        Holder.Reference<DamageType> holder = level.registryAccess()
                .lookup(Registries.DAMAGE_TYPE).get()
                .getOrThrow(WATER_DAMAGE);
        return new DamageSource(holder);

    }
    public static DamageSource boil(ServerLevel level) {
        Holder.Reference<DamageType> holder = level.registryAccess()
                .lookup(Registries.DAMAGE_TYPE).get()
                .getOrThrow(BOIL_DAMAGE);
        return new DamageSource(holder);

    }


}
