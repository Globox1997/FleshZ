package net.rotten;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.rotten.block.WoodRack;
import net.rotten.block.entity.WoodRackEntity;
import net.rotten.recipe.RecipeInit;

public class FleshMain implements ModInitializer {

    public static final Item ROTTEN_LEATHER = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item HIDE = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item PREPARED_HIDE = new Item(new Item.Settings().group(ItemGroup.MISC));

    public static final WoodRack WOOD_RACK = new WoodRack(FabricBlockSettings.copy(Blocks.OAK_PLANKS));
    public static final BlockEntityType<WoodRackEntity> WOOD_RACK_ENTITY = BlockEntityType.Builder
            .create(WoodRackEntity::new, WOOD_RACK).build(null);;

    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM, new Identifier("rotten", "rotten_leather"), ROTTEN_LEATHER);
        Registry.register(Registry.ITEM, new Identifier("rotten", "hide"), HIDE);
        Registry.register(Registry.ITEM, new Identifier("rotten", "prepared_hide"), PREPARED_HIDE);
        Registry.register(Registry.ITEM, new Identifier("rotten", "wood_rack"),
                new BlockItem(WOOD_RACK, new Item.Settings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("rotten", "wood_rack"), WOOD_RACK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "rotten:wood_rack_entity", WOOD_RACK_ENTITY);
        RecipeInit.init();
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!