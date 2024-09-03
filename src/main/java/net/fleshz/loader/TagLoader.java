package net.fleshz.loader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class TagLoader extends FabricTagProvider.BlockTagProvider {


    public TagLoader(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (Block entry : BlockInit.RACKS.keySet()) {
            getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(entry);
        }
    }
}
