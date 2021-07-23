package net.rotten.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rotten.FleshMain;
import net.rotten.recipe.RecipeInit;

public class WoodRackEntity extends BlockEntity implements Inventory, BlockEntityClientSerializable {
    public Item result;
    public int index;
    public int dryingTime;
    private int processTime;
    private DefaultedList<ItemStack> inventory;

    public WoodRackEntity(BlockPos pos, BlockState state) {
        super(FleshMain.WOOD_RACK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        dryingTime = nbt.getInt("Drying_Time");
        index = nbt.getInt("Rack_Index");
        result = RecipeInit.RACK_RESULT_ITEM_LIST.get(index);
        inventory.clear();
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Drying_Time", dryingTime);
        nbt.putInt("Rack_Index", index);
        Inventories.writeNbt(nbt, inventory);
        return nbt;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, WoodRackEntity blockEntity) {
        blockEntity.update();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, WoodRackEntity blockEntity) {
        blockEntity.update();
    }

    private void update() {
        if (!this.world.isClient && !isEmpty() && RecipeInit.RACK_ITEM_LIST.contains(this.getStack(0).getItem())) {
            ++processTime;
            if (processTime >= dryingTime) {
                this.setStack(0, new ItemStack(result));
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
        this.inventory.clear();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getStack(0).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(0);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, 1);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.clear();
        this.inventory.set(0, stack);
        this.markDirty();
    }

    @Override
    public ItemStack removeStack(int slot) {
        this.markDirty();
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        inventory.clear();
        Inventories.readNbt(tag, inventory);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        Inventories.writeNbt(tag, inventory);
        return tag;
    }

}