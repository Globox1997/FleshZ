package net.fleshz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fleshz.block.render.WoodRackRenderer;
import net.fleshz.network.RottenClientPacket;
import net.minecraft.client.render.RenderLayer;

public class FleshClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(FleshMain.WOOD_RACK, RenderLayer.getCutout());
        BlockEntityRendererRegistry.register(FleshMain.WOOD_RACK_ENTITY, WoodRackRenderer::new);
        RottenClientPacket.init();
    }

}