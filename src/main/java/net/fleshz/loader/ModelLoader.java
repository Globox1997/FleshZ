package net.fleshz.loader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fleshz.FleshMain;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelLoader extends FabricModelProvider {

    public static final Model RACK = new Model(Optional.of(FleshMain.identifierOf("block/rack")), Optional.empty(), TextureKey.TEXTURE);

    public ModelLoader(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        for (Map.Entry<Block, List<Block>> entry : BlockInit.RACKS.entrySet()) {
            blockStateModelGenerator.blockStateCollector
                    .accept(
                            VariantsBlockStateSupplier.create(entry.getKey(), BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(entry.getKey())))
                                    .coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates())
                    );
            RACK.upload(entry.getKey(), TextureMap.texture(ModelIds.getBlockModelId(entry.getValue().get(0))), blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.registerParentedItemModel(entry.getKey(), ModelIds.getBlockModelId(entry.getKey()));
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }

}

