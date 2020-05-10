package net.rotten;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WoodReckEntity extends BlockEntity implements Tickable {
  public int dryingTime = 200;
  private int requiredPlayerRange = 16;
  public boolean isOnRack = false;

  public WoodReckEntity() {
    super(Leather.WOOD_RECK_ENTITY);
  }

  @Override
  public void tick() {
    this.update();
  }

  private boolean isPlayerInRange() {
    BlockPos blockPos = this.getPos();
    return this.getWorld().isPlayerInRange((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D,
        (double) blockPos.getZ() + 0.5D, (double) this.requiredPlayerRange);
  }

  public void update() {
    if (this.isPlayerInRange()) {
      World world = this.getWorld();
      BlockPos blockPos = this.getPos();
      if (!world.isClient && this.isOnRack == true) {
        dryingTime--;
        if (dryingTime <= 0) {
          ItemStack sulfurdrop = new ItemStack(Items.LEATHER, 1);
          this.isOnRack = false;
          world.getBlockState(blockPos).getBlock();
          Block.dropStack(world, pos, sulfurdrop);
          dryingTime = 200;
        }
      }
    }
  }
}