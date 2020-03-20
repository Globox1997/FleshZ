package net.rotten;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class leth implements ModInitializer {

    public static final flesh FLESH = new flesh();

    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM, new Identifier("rotten", "flesh"), FLESH);

    }
}

// You are LOVED!!!
// Jesus loves you unconditional!