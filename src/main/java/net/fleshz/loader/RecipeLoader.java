package net.fleshz.loader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeLoader extends FabricRecipeProvider {

    public RecipeLoader(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        for (Map.Entry<Block, List<Block>> entry : BlockInit.RACKS.entrySet()) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, entry.getKey()).pattern("###")
                    .input('#', entry.getValue().get(1))
                    .criterion(FabricRecipeProvider.hasItem(entry.getValue().get(1)),
                            FabricRecipeProvider.conditionsFromItem(entry.getValue().get(1)))
                    .offerTo(exporter);
        }
    }
}
