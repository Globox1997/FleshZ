package net.fleshz.init;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class EventInit {

    public static void init() {
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
        if (FabricLoader.getInstance().isModLoaded("untamed")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "untamed_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }
}
