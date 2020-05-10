package net.rotten;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class woodreckentity extends BlockEntity implements Tickable {
  private int dryingtime = 200;
  private int requiredPlayerRange = 16;

  public woodreckentity() {
    super(leth.WOODRECKENTITY);
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
      if (!world.isClient && leth.FLESH.isonrack == true) {
        dryingtime--;
        if (dryingtime <= 0) {
          ItemStack sulfurdrop = new ItemStack(Items.LEATHER, 1);
          leth.FLESH.isonrack = false;
          world.getBlockState(blockPos).getBlock();
          Block.dropStack(world, pos, sulfurdrop);
          dryingtime = 200;
        }
      }
    }
  }
}