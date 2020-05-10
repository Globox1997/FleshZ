package net.rotten;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WoodReck extends Block implements BlockEntityProvider {

  public static final VoxelShape SHAPENORTH;
  public static final VoxelShape SHAPEWEST;
  public static final VoxelShape SHAPEEAST;
  public static final VoxelShape SHAPESOUTH;
  public static final DirectionProperty FACING;
  public static final BooleanProperty WATERLOGGED;

  public WoodReck(Settings settings) {
    super(settings);
    this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    ItemStack item = player.getMainHandStack();
    if (state.getBlock() instanceof WoodReck) {
      if (!world.isClient && Leather.WOOD_RECK_ENTITY.get(world, pos).dryingTime == 200) {
        item.decrement(1);
        Leather.WOOD_RECK_ENTITY.get(world, pos).isOnRack = true;
      }
      return ActionResult.SUCCESS;
    } else
      return ActionResult.PASS;
  }

  @Override
  public BlockEntity createBlockEntity(BlockView view) {
    return new WoodReckEntity();
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
    switch ((Direction) state.get(FACING)) {
      case NORTH:
        return SHAPENORTH;
      case SOUTH:
        return SHAPESOUTH;
      case WEST:
        return SHAPEWEST;
      case EAST:
        return SHAPEEAST;
      default:
        return SHAPEEAST;
    }
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
  public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState,
      IWorld world, BlockPos pos, BlockPos neighborPos) {
    if (facing.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
      return Blocks.AIR.getDefaultState();
    } else {
      if ((Boolean) state.get(WATERLOGGED)) {
        world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
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

  static {
    FACING = HorizontalFacingBlock.FACING;
    SHAPESOUTH = Block.createCuboidShape(0D, 13D, 0D, 16D, 16D, 3D);
    SHAPEEAST = Block.createCuboidShape(0D, 13D, 0D, 3D, 16D, 16D);
    SHAPEWEST = Block.createCuboidShape(13D, 13D, 0D, 16D, 16D, 16D);
    SHAPENORTH = Block.createCuboidShape(0D, 13D, 13D, 16D, 16D, 16D);
    WATERLOGGED = Properties.WATERLOGGED;
  }
}
