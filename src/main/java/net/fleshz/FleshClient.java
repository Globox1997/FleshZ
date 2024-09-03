package net.fleshz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fleshz.block.render.WoodRackRenderer;
import net.fleshz.init.BlockInit;
import net.fleshz.network.RottenClientPacket;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class FleshClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Block entry : BlockInit.RACKS.keySet()) {
            BlockRenderLayerMap.INSTANCE.putBlock(entry, RenderLayer.getCutout());
        }
        BlockEntityRendererFactories.register(BlockInit.WOOD_RACK_ENTITY, WoodRackRenderer::new);
        RottenClientPacket.init();
    }

}