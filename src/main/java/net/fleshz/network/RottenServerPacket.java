package net.fleshz.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fleshz.network.packet.RackPacket;

public class RottenServerPacket {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(RackPacket.PACKET_ID, RackPacket.PACKET_CODEC);
    }

}
