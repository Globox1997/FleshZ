package net.fleshz.block.entity;

import java.util.Optional;

import net.fleshz.init.BlockInit;
import net.fleshz.init.RecipeInit;
import net.fleshz.recipe.RackRecipe;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WoodRackEntity extends BlockEntity implements SidedInventory {

    private static final int[] SLOTS = new int[] { 0 };
    private static final float ULTRAWARM_MULTIPLIER = 2.0F;
    private static final float HOT_DRY_BIOME_MULTIPLIER = 1.5F;
    private static final float COLD_BIOME_MULTIPLIER = 0.5F;
    private static final float HUMID_WEATHER_MULTIPLIER = 0.75F;
    private static final float SUNLIGHT_MULTIPLIER = 1.25F;
    private static final float HEAT_SOURCE_MULTIPLIER = 1.5F;
    private static final int HEAT_SOURCE_RANGE = 3;

    private final RecipeManager.MatchGetter<SingleStackRecipeInput, RackRecipe> matchGetter = RecipeManager.createCachedMatchGetter(RecipeInit.RACK_DRYING);
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float progress;
    private float dryingSpeed = -1.0F;
    private int comparatorOutput;

    public WoodRackEntity(BlockPos pos, BlockState state) {
        super(BlockInit.WOOD_RACK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        this.progress = nbt.getFloat("Drying_Progress");
    }

    @Override
    public void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putFloat("Drying_Progress", this.progress);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, WoodRackEntity blockEntity) {
        blockEntity.update(world, pos, state);
    }

    private void update(World world, BlockPos pos, BlockState state) {
        ItemStack stack = this.getStack(0);
        Optional<RecipeEntry<RackRecipe>> recipe = this.getRecipe(stack);
        if (recipe.isPresent()) {
            if (this.dryingSpeed < 0.0F || world.getTime() % 20L == 0L) {
                this.dryingSpeed = getDryingSpeed(world, pos, state);
                world.markDirty(pos);
            }
            this.progress += this.dryingSpeed;
            if (this.progress >= recipe.get().value().getTime()) {
                this.inventory.set(0, recipe.get().value().craft(new SingleStackRecipeInput(stack), world.getRegistryManager()));
                this.progress = 0.0F;
                this.markDirty();
            }
        } else if (this.progress != 0.0F) {
            this.progress = 0.0F;
            world.markDirty(pos);
        }

        int comparatorOutput = this.getComparatorOutput();
        if (comparatorOutput != this.comparatorOutput) {
            this.comparatorOutput = comparatorOutput;
            world.updateComparators(pos, state.getBlock());
        }
    }

    private static float getDryingSpeed(World world, BlockPos pos, BlockState state) {
        if (state.getOrEmpty(Properties.WATERLOGGED).orElse(false) || world.hasRain(pos.up())) {
            return 0.0F;
        }
        float speed = 1.0F;
        if (world.getDimension().ultrawarm()) {
            speed *= ULTRAWARM_MULTIPLIER;
        } else {
            Biome biome = world.getBiome(pos).value();
            if (!biome.hasPrecipitation() && biome.getTemperature() > 1.0F) {
                speed *= HOT_DRY_BIOME_MULTIPLIER;
            } else if (biome.getTemperature() < 0.15F) {
                speed *= COLD_BIOME_MULTIPLIER;
            }
            if (biome.hasPrecipitation() && world.isRaining()) {
                speed *= HUMID_WEATHER_MULTIPLIER;
            }
            if (world.isDay() && world.isSkyVisible(pos)) {
                speed *= SUNLIGHT_MULTIPLIER;
            }
        }
        if (isAboveHeatSource(world, pos)) {
            speed *= HEAT_SOURCE_MULTIPLIER;
        }
        return speed;
    }

    private static boolean isAboveHeatSource(World world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int i = 0; i < HEAT_SOURCE_RANGE; i++) {
            BlockState state = world.getBlockState(mutable.move(Direction.DOWN));
            if (state.isIn(BlockInit.RACK_HEAT_SOURCES) && state.getOrEmpty(Properties.LIT).orElse(true)) {
                return true;
            }
            if (state.isOpaqueFullCube(world, mutable)) {
                return false;
            }
        }
        return false;
    }

    private Optional<RecipeEntry<RackRecipe>> getRecipe(ItemStack stack) {
        if (this.world == null || stack.isEmpty()) {
            return Optional.empty();
        }
        return this.matchGetter.getFirstMatch(new SingleStackRecipeInput(stack), this.world);
    }

    public int getComparatorOutput() {
        ItemStack stack = this.getStack(0);
        if (stack.isEmpty()) {
            return 0;
        }
        Optional<RecipeEntry<RackRecipe>> recipe = this.getRecipe(stack);
        if (recipe.isEmpty()) {
            return 15;
        }
        int time = recipe.get().value().getTime();
        if (time <= 0) {
            return 14;
        }
        return 1 + MathHelper.floor(MathHelper.clamp(this.progress / time, 0.0F, 1.0F) * 13.0F);
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
        this.progress = 0.0F;
        this.markDirty();
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
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);
        if (!result.isEmpty()) {
            this.progress = 0.0F;
            this.markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(this.inventory, slot);
        if (!result.isEmpty()) {
            this.progress = 0.0F;
            this.markDirty();
        }
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (!ItemStack.areItemsAndComponentsEqual(this.inventory.get(0), stack)) {
            this.progress = 0.0F;
        }
        stack.capCount(this.getMaxCount(stack));
        this.inventory.set(0, stack);
        this.markDirty();
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return this.getRecipe(stack).isPresent();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isEmpty() && this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return this.getRecipe(stack).isEmpty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

}
