package net.fleshz.network;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fleshz.network.packet.RackPacket;
import net.fleshz.recipe.RecipeInit;
import net.minecraft.registry.Registries;

@Environment(EnvType.CLIENT)
public class RottenClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(RackPacket.PACKET_ID, (payload, context) -> {
            List<Integer> rackItems = payload.rackItems();
            List<Integer> rackResultItems = payload.rackResultItems();
            List<Integer> rackResultTimes = payload.rackResultTimes();

            context.client().execute(() -> {
                RecipeInit.RACK_ITEM_LIST.clear();
                RecipeInit.RACK_RESULT_ITEM_LIST.clear();
                RecipeInit.RACK_RESULT_TIME_LIST.clear();

                for (int i = 0; i < rackItems.size(); i++) {
                    RecipeInit.RACK_ITEM_LIST.add(Registries.ITEM.get(rackItems.get(i)));
                }
                for (int i = 0; i < rackResultItems.size(); i++) {
                    RecipeInit.RACK_RESULT_ITEM_LIST.add(Registries.ITEM.get(rackResultItems.get(i)));
                }
                RecipeInit.RACK_RESULT_TIME_LIST.addAll(rackResultTimes);
            });
        });
    }
}
