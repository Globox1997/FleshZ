package net.fleshz.network.packet;

import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RackPacket(List<Integer> rackItems, List<Integer> rackResultItems, List<Integer> rackResultTimes) implements CustomPayload {

    public static final CustomPayload.Id<RackPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("fleshz", "rack_recipes_packet"));

    public static final PacketCodec<RegistryByteBuf, RackPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeIntList(new IntArrayList(value.rackItems));
        buf.writeIntList(new IntArrayList(value.rackResultItems));
        buf.writeIntList(new IntArrayList(value.rackResultTimes));
    }, buf -> new RackPacket(buf.readIntList(), buf.readIntList(), buf.readIntList()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
