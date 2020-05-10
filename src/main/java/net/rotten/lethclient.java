package net.rotten;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class lethclient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(leth.WOODRECK, RenderLayer.getCutout());
    BlockEntityRendererRegistry.INSTANCE.register(leth.WOODRECKENTITY, woodreckrenderer::new);
  }

}