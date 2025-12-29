package net.hasagj.teamod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AbilityUsePacket() implements CustomPacketPayload {

    public static final Type<AbilityUsePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("teamod", "ability_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityUsePacket> CODEC =
            StreamCodec.of(
                    (buf, packet) -> { /* no payload, nothing to write */ },
                    buf -> new AbilityUsePacket()
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
