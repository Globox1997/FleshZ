package net.rotten.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.rotten.recipe.RecipeInit;

public class RottenServerPacket {

    public static final Identifier UPDATE_RACK_RECIPES = new Identifier("rotten", "update_rack_recipes");

    public static void writeS2CRackRecipesPacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < RecipeInit.RACK_ITEM_LIST.size(); i++)
            list.add(Registries.ITEM.getRawId(RecipeInit.RACK_ITEM_LIST.get(i)));
        buf.writeIntList(new IntArrayList(list));
        list.clear();
        for (int u = 0; u < RecipeInit.RACK_RESULT_ITEM_LIST.size(); u++)
            list.add(Registries.ITEM.getRawId(RecipeInit.RACK_RESULT_ITEM_LIST.get(u)));
        buf.writeIntList(new IntArrayList(list));
        list.clear();
        for (int k = 0; k < RecipeInit.RACK_RESULT_ITEM_LIST.size(); k++)
            list.add(RecipeInit.RACK_RESULT_TIME_LIST.get(k));
        buf.writeIntList(new IntArrayList(list));
        list.clear();
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(UPDATE_RACK_RECIPES, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }
}
