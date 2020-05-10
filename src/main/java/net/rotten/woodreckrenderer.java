package net.rotten;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.client.render.model.json.ModelTransformation;

public class woodreckrenderer extends BlockEntityRenderer<woodreckentity> {
  private ItemStack stack = new ItemStack(leth.FLESH, 1);

  public woodreckrenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  public void render(woodreckentity blockEntity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    World world = blockEntity.getWorld();
    BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
    Direction o = state.get(HorizontalFacingBlock.FACING);
    if (leth.FLESH.isonrack == true && world.isClient) {
      if (o == Direction.NORTH) {
        matrices.push();
        matrices.translate(0.5D, 0.5D, 0.9D);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light,
            overlay, matrices, vertexConsumers);
        matrices.pop();
      } else {
        if (o == Direction.SOUTH) {
          matrices.push();
          matrices.translate(0.5D, 0.5D, 0.1D);
          MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light,
              overlay, matrices, vertexConsumers);
          matrices.pop();
        } else {
          if (o == Direction.EAST) {
            matrices.push();
            matrices.translate(0.1D, 0.5D, 0.5D);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((90F)));
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light,
                overlay, matrices, vertexConsumers);
            matrices.pop();
          } else {
            if (o == Direction.WEST) {
              matrices.push();
              matrices.translate(0.9D, 0.5D, 0.5D);
              matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((90F)));
              MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light,
                  overlay, matrices, vertexConsumers);
              matrices.pop();
            }
          }
        }
      }
    }
  }
}