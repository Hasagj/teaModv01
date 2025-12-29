package net.hasagj.teamod.network;

import net.hasagj.teamod.ability.AbilityLogic;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ModNetwork {

    public static void register(RegisterPayloadHandlersEvent event) {

        var registrar = event.registrar("1");

        registrar.playToServer(
                AbilityUsePacket.TYPE,
                AbilityUsePacket.CODEC,
                (packet, context) -> {
                    ServerPlayer player = (ServerPlayer) context.player();

                    if (player != null) {
                        AbilityLogic.use(player);
                    }
                }
        );
    }
}
