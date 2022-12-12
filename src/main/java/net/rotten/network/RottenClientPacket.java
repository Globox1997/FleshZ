package net.rotten.network;

import java.util.List;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.rotten.recipe.RecipeInit;

public class RottenClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(RottenServerPacket.UPDATE_RACK_RECIPES, (client, handler, buf, sender) -> {
            PacketByteBuf newBuffer = new PacketByteBuf(Unpooled.buffer());
            newBuffer.writeIntList(buf.readIntList());
            newBuffer.writeIntList(buf.readIntList());
            newBuffer.writeIntList(buf.readIntList());

            client.execute(() -> {
                RecipeInit.RACK_ITEM_LIST.clear();
                RecipeInit.RACK_RESULT_ITEM_LIST.clear();
                RecipeInit.RACK_RESULT_TIME_LIST.clear();

                List<Integer> list = newBuffer.readIntList();
                for (int i = 0; i < list.size(); i++)
                    RecipeInit.RACK_ITEM_LIST.add(Registries.ITEM.get(list.get(i)));
                list.clear();

                list = newBuffer.readIntList();
                for (int i = 0; i < list.size(); i++)
                    RecipeInit.RACK_RESULT_ITEM_LIST.add(Registries.ITEM.get(list.get(i)));
                list.clear();

                RecipeInit.RACK_RESULT_TIME_LIST.addAll(newBuffer.readIntList());

            });
        });
    }
}
