package net.fleshz;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fleshz.loader.*;

public class FleshDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelLoader::new);
        pack.addProvider(RecipeLoader::new);
        pack.addProvider(LootTableLoader::new);
        pack.addProvider(TagLoader::new);
//        pack.addProvider(LangLoader::new);
    }
}
