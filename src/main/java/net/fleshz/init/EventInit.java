package net.fleshz.init;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fleshz.network.packet.RackPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class EventInit {

    public static void init() {

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<Integer> rackItems = new ArrayList<Integer>();
            List<Integer> rackResultItems = new ArrayList<Integer>();

            for (int i = 0; i < RecipeInit.RACK_ITEM_LIST.size(); i++) {
                rackItems.add(Registries.ITEM.getRawId(RecipeInit.RACK_ITEM_LIST.get(i)));
            }
            for (int i = 0; i < RecipeInit.RACK_RESULT_ITEM_LIST.size(); i++) {
                rackResultItems.add(Registries.ITEM.getRawId(RecipeInit.RACK_RESULT_ITEM_LIST.get(i)));
            }
            ServerPlayNetworking.send(handler.player, new RackPacket(rackItems, rackResultItems, RecipeInit.RACK_RESULT_TIME_LIST));
        });

        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "adventurez_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("naturalist")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "naturalist_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "meadow_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }
}
