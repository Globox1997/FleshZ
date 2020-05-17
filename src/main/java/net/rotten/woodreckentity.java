package net.rotten;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;

public class WoodReckEntity extends BlockEntity implements Tickable, Inventory {
  public int dryingTime = 4800;
  private int processTime;
  private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

  public WoodReckEntity() {
    super(Leather.WOOD_RECK_ENTITY);
  }

  @Override
  public void fromTag(CompoundTag tag) {
    super.fromTag(tag);
    Inventories.fromTag(tag, items);
  }

  @Override
  public CompoundTag toTag(CompoundTag tag) {
    Inventories.toTag(tag, items);
    return super.toTag(tag);
  }

  @Override
  public void tick() {
    this.update();
  }

  public void update() {
    if (!isInvEmpty()) {
      ++processTime;
      if (processTime >= dryingTime) {
        this.items.clear();
        setInvStack(0, new ItemStack(Items.LEATHER));
        processTime = 0;
      }
    }
  }

  @Override
  public void markDirty() {
    super.markDirty();
    sendUpdate();
  }

  private void sendUpdate() {
    if (this.world != null) {
      BlockState state = this.world.getBlockState(this.pos);
      (this.world).updateListeners(this.pos, state, state, 3);
    }
  }

  @Override
  public void clear() {
    this.items.clear();
  }

  @Override
  public int getInvSize() {
    return 1;
  }

  @Override
  public boolean isInvEmpty() {
    return this.getInvStack(0).isEmpty();
  }

  @Override
  public ItemStack getInvStack(int slot) {
    return this.items.get(0);
  }

  @Override
  public ItemStack takeInvStack(int slot, int amount) {
    ItemStack result = Inventories.splitStack(this.items, slot, 1);
    if (!result.isEmpty()) {
      markDirty();
    }
    return result;
  }

  @Override
  public ItemStack removeInvStack(int slot) {
    this.markDirty();
    return Inventories.removeStack(this.items, slot);
  }

  @Override
  public void setInvStack(int slot, ItemStack stack) {
    this.items.set(0, stack);
    this.markDirty();
  }

  @Override
  public boolean canPlayerUseInv(PlayerEntity player) {
    return true;
  }
}