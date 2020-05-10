package net.rotten;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class LeatherClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(Leather.WOOD_RECK, RenderLayer.getCutout());
    BlockEntityRendererRegistry.INSTANCE.register(Leather.WOOD_RECK_ENTITY, WoodReckRenderer::new);
  }

}