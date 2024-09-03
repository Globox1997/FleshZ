package net.fleshz.loader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LootTableLoader extends FabricBlockLootTableProvider {

    public LootTableLoader(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        for (Block entry : BlockInit.RACKS.keySet()) {
            addDrop(entry, block -> this.nameableContainerDrops(entry));
        }
    }

}
