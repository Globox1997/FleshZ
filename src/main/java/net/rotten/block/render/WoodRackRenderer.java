package net.rotten.block.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;
import net.rotten.block.entity.WoodRackEntity;
import net.minecraft.client.render.model.json.ModelTransformation;

public class WoodRackRenderer extends BlockEntityRenderer<WoodRackEntity> {

  public WoodRackRenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  public void render(WoodRackEntity blockEntity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
    Direction blockDirection = state.get(HorizontalFacingBlock.FACING);
    if (!blockEntity.isEmpty()) {
      if (blockDirection == Direction.NORTH) {
        matrices.push();
        matrices.scale(1.8F, 1.8F, 1.8F);
        matrices.translate(0.28D, 0.12D, 0.5D);
        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(0),
            ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
        matrices.pop();
      } else {
        if (blockDirection == Direction.SOUTH) {
          matrices.push();
          matrices.scale(1.8F, 1.8F, 1.8F);
          matrices.translate(0.28D, 0.12D, 0.05D);
          MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(0),
              ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
          matrices.pop();
        } else {
          if (blockDirection == Direction.EAST) {
            matrices.push();
            matrices.scale(1.8F, 1.8F, 1.8F);
            matrices.translate(0.05D, 0.12D, 0.28D);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((90F)));
            MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(0),
                ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
            matrices.pop();
          } else {
            if (blockDirection == Direction.WEST) {
              matrices.push();
              matrices.scale(1.8F, 1.8F, 1.8F);
              matrices.translate(0.5D, 0.12D, 0.28D);
              matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((90F)));
              MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(0),
                  ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
              matrices.pop();
            }
          }
        }
      }
    }
  }
}