package net.rotten;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.rotten.block.render.WoodRackRenderer;

public class FleshClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(FleshMain.WOOD_RACK, RenderLayer.getCutout());
        BlockEntityRendererRegistry.INSTANCE.register(FleshMain.WOOD_RACK_ENTITY, WoodRackRenderer::new);
    }

}