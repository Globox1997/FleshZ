package net.rotten.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.rotten.FleshMain;
import net.rotten.block.entity.WoodRackEntity;
import net.rotten.recipe.RecipeInit;
import net.minecraft.block.ShapeContext;
import org.jetbrains.annotations.Nullable;

public class WoodRack extends Block implements BlockEntityProvider {

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
    return checkType(type, FleshMain.WOOD_RACK_ENTITY, world.isClient ? WoodRackEntity::clientTick : WoodRackEntity::serverTick);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    WoodRackEntity woodReckEntity = (WoodRackEntity) world.getBlockEntity(pos);
    ItemStack stack = woodReckEntity.getStack(0);
    if (!stack.isEmpty()) {
      // Remove hanging item
      if (!world.isClient && !player.giveItemStack(stack.split(1))) {
        player.dropItem(stack.split(1), false);
      }
      woodReckEntity.clear();
      return ActionResult.SUCCESS;
    } else {
      // Hang item on rack
      ItemStack heldItem = player.getMainHandStack();
      if (!heldItem.isEmpty() && RecipeInit.RACK_ITEM_LIST.contains(heldItem.getItem())) {
        if (!world.isClient) {
          int index = RecipeInit.RACK_ITEM_LIST.indexOf(heldItem.getItem());
          woodReckEntity.dryingTime = RecipeInit.RACK_RESULT_TIME_LIST.get(index);
          woodReckEntity.result = RecipeInit.RACK_RESULT_ITEM_LIST.get(index);
          woodReckEntity.index = index;
          if (player.isCreative()) {
            woodReckEntity.setStack(0, heldItem.copy());
          } else
            woodReckEntity.setStack(0, heldItem.split(1));
        }
        return ActionResult.SUCCESS;
      }
      return ActionResult.FAIL;
    }
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
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
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
      WorldAccess world, BlockPos pos, BlockPos posFrom) {
    if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
      return Blocks.AIR.getDefaultState();
    } else {
      if ((Boolean) state.get(WATERLOGGED)) {
        world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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

  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
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
