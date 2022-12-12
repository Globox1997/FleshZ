package net.rotten;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.rotten.block.WoodRack;
import net.rotten.block.entity.WoodRackEntity;
import net.rotten.recipe.RecipeInit;

public class FleshMain implements ModInitializer {

    public static final Item ROTTEN_LEATHER = new Item(new Item.Settings());
    public static final Item HIDE = new Item(new Item.Settings());
    public static final Item PREPARED_HIDE = new Item(new Item.Settings());

    public static final WoodRack WOOD_RACK = new WoodRack(FabricBlockSettings.copy(Blocks.OAK_PLANKS));
    public static final BlockEntityType<WoodRackEntity> WOOD_RACK_ENTITY = FabricBlockEntityTypeBuilder.create(WoodRackEntity::new, WOOD_RACK).build(null);;

    @Override
    public void onInitialize() {

        Registry.register(Registries.ITEM, new Identifier("rotten", "rotten_leather"), ROTTEN_LEATHER);
        Registry.register(Registries.ITEM, new Identifier("rotten", "hide"), HIDE);
        Registry.register(Registries.ITEM, new Identifier("rotten", "prepared_hide"), PREPARED_HIDE);
        Registry.register(Registries.ITEM, new Identifier("rotten", "wood_rack"), new BlockItem(WOOD_RACK, new Item.Settings()));
        Registry.register(Registries.BLOCK, new Identifier("rotten", "wood_rack"), WOOD_RACK);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, "rotten:wood_rack_entity", WOOD_RACK_ENTITY);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(HIDE);
            entries.add(PREPARED_HIDE);
            entries.add(ROTTEN_LEATHER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> entries.add(WOOD_RACK));
        RecipeInit.init();
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!