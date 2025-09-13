package net.hasagj.teamod.trigger;

import net.hasagj.teamod.TeaMod;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA =
            DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, TeaMod.MOD_ID);

    public static final Supplier<SweetTrigger> SWEET_TRIGGER = CRITERIA.register("sweet_trigger", SweetTrigger::new);
    public static final Supplier<BitterTrigger> BITTER_TRIGGER = CRITERIA.register("bitter_trigger", BitterTrigger::new);
    public static final Supplier<AppetiteTrigger> APPETITE_TRIGGER = CRITERIA.register("appetite_trigger", AppetiteTrigger::new);
    public static final Supplier<CreakingTrigger> CREAKING_TRIGGER = CRITERIA.register("creaking_trigger", CreakingTrigger::new);
    public static final Supplier<BristleBackTrigger> BRISTLEBACK_TRIGGER = CRITERIA.register("bristleback_trigger", BristleBackTrigger::new);
    public static final Supplier<StingingTrigger> STINGING_TRIGGER = CRITERIA.register("stinging_trigger", StingingTrigger::new);
    public static final Supplier<TeleportTrigger> TELEPORT_TRIGGER = CRITERIA.register("teleport_trigger", TeleportTrigger::new);


    public static void register(IEventBus bus) {
        CRITERIA.register(bus);
    }
}
