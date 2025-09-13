package net.hasagj.teamod.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.hasagj.teamod.TeaMod;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

public class BristleBackTrigger extends SimpleCriterionTrigger<BristleBackTrigger.TriggerInstance> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "bristleback_trigger");

    // В ваниле SimpleCriterionTrigger требует реализации codec()
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    // Вызов триггера: проверяет все привязанные инстансы и запустит criteria
    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    // Вложенный класс/record — условие (instance) для JSON / десериализации
    public static record TriggerInstance(Optional<ContextAwarePredicate> playerPredicate) implements SimpleCriterionTrigger.SimpleInstance {
        // Codec: поле "player" опционально (как в ванильных триггерах)
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerPredicate)
                ).apply(instance, TriggerInstance::new)
        );

        // Конструктор для создания instance из кода (если нужно)
        public TriggerInstance(Optional<ContextAwarePredicate> playerPredicate) {
            this.playerPredicate = playerPredicate;
        }

        // Возвращаем ID критерия — пригодится, если нужно создавать Criterion из кода
        public ResourceLocation getCriterionId() {
            return BristleBackTrigger.ID;
        }


        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}