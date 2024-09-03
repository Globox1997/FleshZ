package net.fleshz.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fleshz.FleshMain;
import net.fleshz.block.WoodRack;
import net.fleshz.block.entity.WoodRackEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockInit {

    public static final Map<Block, List<Block>> RACKS = new HashMap<>();

    public static final Block OAK_WOOD_RACK = register("oak_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)), Blocks.OAK_PLANKS, Blocks.OAK_SLAB);
    public static final Block SPRUCE_WOOD_RACK = register("spruce_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.SPRUCE_PLANKS)), Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB);
    public static final Block BIRCH_WOOD_RACK = register("birch_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.BIRCH_PLANKS)), Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB);
    public static final Block JUNGLE_WOOD_RACK = register("jungle_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.JUNGLE_PLANKS)), Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB);
    public static final Block ACACIA_WOOD_RACK = register("acacia_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS)), Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB);
    public static final Block DARK_OAK_WOOD_RACK = register("dark_oak_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS)), Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB);
    public static final Block CRIMSON_WOOD_RACK = register("crimson_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS)), Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB);
    public static final Block WARPED_WOOD_RACK = register("warped_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.WARPED_PLANKS)), Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB);
    public static final Block MANGROVE_WOOD_RACK = register("mangrove_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.MANGROVE_PLANKS)), Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB);
    public static final Block BAMBOO_WOOD_RACK = register("bamboo_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.BAMBOO_PLANKS)), Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SLAB);
    public static final Block CHERRY_WOOD_RACK = register("cherry_wood_rack", new WoodRack(AbstractBlock.Settings.copy(Blocks.CHERRY_PLANKS)), Blocks.CHERRY_PLANKS, Blocks.CHERRY_SLAB);

    public static final BlockEntityType<WoodRackEntity> WOOD_RACK_ENTITY = BlockEntityType.Builder.create(WoodRackEntity::new, OAK_WOOD_RACK, SPRUCE_WOOD_RACK, BIRCH_WOOD_RACK, JUNGLE_WOOD_RACK, ACACIA_WOOD_RACK, DARK_OAK_WOOD_RACK, CRIMSON_WOOD_RACK, WARPED_WOOD_RACK, MANGROVE_WOOD_RACK, BAMBOO_WOOD_RACK, CHERRY_WOOD_RACK).build(null);

    private static Block register(String id, Block block, Block plankVariant, Block recipeIngredient) {
        RACKS.put(block, List.of(plankVariant, recipeIngredient));
        return register(id, block);
    }

    private static Block register(String id, Block block) {
        return register(FleshMain.identifierOf(id), block);
    }

    private static Block register(Identifier id, Block block) {
        Item item = Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> entries.add(item));

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, FleshMain.identifierOf("wood_rack_entity"), WOOD_RACK_ENTITY);
    }
}
