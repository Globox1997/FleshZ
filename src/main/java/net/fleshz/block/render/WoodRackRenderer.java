package net.fleshz.block.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fleshz.block.entity.WoodRackEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.model.json.ModelTransformationMode;

@Environment(EnvType.CLIENT)
public class WoodRackRenderer implements BlockEntityRenderer<WoodRackEntity> {

    public WoodRackRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(WoodRackEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!blockEntity.isEmpty() && blockEntity.getWorld() != null) {
            BlockState blockState = blockEntity.getWorld().getBlockState(blockEntity.getPos());
            if (!blockState.isAir()) {
                Direction blockDirection = blockState.get(HorizontalFacingBlock.FACING);
                matrices.push();
                if (blockDirection == Direction.NORTH) {
                    matrices.scale(1.8F, 1.8F, 1.8F);
                    matrices.translate(0.28D, 0.12D, 0.5D);
                } else if (blockDirection == Direction.SOUTH) {
                    matrices.scale(1.8F, 1.8F, 1.8F);
                    matrices.translate(0.28D, 0.12D, 0.05D);
                } else if (blockDirection == Direction.EAST) {
                    matrices.scale(1.8F, 1.8F, 1.8F);
                    matrices.translate(0.05D, 0.12D, 0.28D);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((90F)));
                } else if (blockDirection == Direction.WEST) {
                    matrices.scale(1.8F, 1.8F, 1.8F);
                    matrices.translate(0.5D, 0.12D, 0.28D);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((90F)));
                }
                MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(0), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, blockEntity.getWorld(),
                        (int) blockEntity.getPos().asLong());
                matrices.pop();
            }
        }
    }
}