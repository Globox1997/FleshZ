package net.fleshz.block;

import net.fleshz.block.entity.WoodRackEntity;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.block.ShapeContext;
import org.jetbrains.annotations.Nullable;

public class WoodRack extends Block implements BlockEntityProvider, Waterloggable {

    public static final VoxelShape SHAPENORTH;
    public static final VoxelShape SHAPEWEST;
    public static final VoxelShape SHAPEEAST;
    public static final VoxelShape SHAPESOUTH;
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;

    public WoodRack(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodRackEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockInit.WOOD_RACK_ENTITY, world.isClient() ? null : WoodRackEntity::serverTick);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof WoodRackEntity woodRackEntity && woodRackEntity.isEmpty() && woodRackEntity.isValid(0, stack)) {
            if (!world.isClient()) {
                woodRackEntity.setStack(0, stack.splitUnlessCreative(1, player));
            }
            return ItemActionResult.success(world.isClient());
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof WoodRackEntity woodRackEntity && !woodRackEntity.isEmpty()) {
            if (!world.isClient()) {
                player.getInventory().offerOrDrop(woodRackEntity.removeStack(0));
            }
            return ActionResult.success(world.isClient());
        }
        return ActionResult.PASS;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof WoodRackEntity woodRackEntity ? woodRackEntity.getComparatorOutput() : 0;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch ((Direction) state.get(FACING)) {
            case NORTH -> SHAPENORTH;
            case SOUTH -> SHAPESOUTH;
            case WEST -> SHAPEWEST;
            case EAST -> SHAPEEAST;
            default -> SHAPEEAST;
        };
    }

    private boolean canPlaceOn(BlockView world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        return !blockState.emitsRedstonePower() && blockState.isSideSolidFullSquare(world, pos, side);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = (Direction) state.get(FACING);
        return this.canPlaceOn(world, pos.offset(direction.getOpposite()), direction);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if ((Boolean) state.get(WATERLOGGED)) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }

            return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState2;
        if (!ctx.canReplaceExisting()) {
            blockState2 = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite()));
            if (blockState2.getBlock() == this && blockState2.get(FACING) == ctx.getSide()) {
                return null;
            }
        }

        blockState2 = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] var6 = ctx.getPlacementDirections();
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState2 = (BlockState) blockState2.with(FACING, direction.getOpposite());
                if (blockState2.canPlaceAt(worldView, blockPos)) {
                    return (BlockState) blockState2.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }

        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return (Boolean) state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        SHAPESOUTH = Block.createCuboidShape(0D, 13D, 0D, 16D, 16D, 3D);
        SHAPEEAST = Block.createCuboidShape(0D, 13D, 0D, 3D, 16D, 16D);
        SHAPEWEST = Block.createCuboidShape(13D, 13D, 0D, 16D, 16D, 16D);
        SHAPENORTH = Block.createCuboidShape(0D, 13D, 13D, 16D, 16D, 16D);
        WATERLOGGED = Properties.WATERLOGGED;
    }
}
