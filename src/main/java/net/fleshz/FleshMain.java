package net.fleshz;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fleshz.block.WoodRack;
import net.fleshz.block.entity.WoodRackEntity;
import net.fleshz.network.RottenServerPacket;
import net.fleshz.network.packet.RackPacket;
import net.fleshz.recipe.RecipeInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FleshMain implements ModInitializer {

    public static final Item ROTTEN_LEATHER = new Item(new Item.Settings());
    public static final Item HIDE = new Item(new Item.Settings());
    public static final Item PREPARED_HIDE = new Item(new Item.Settings());

    public static final WoodRack WOOD_RACK = new WoodRack(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS));
    public static final BlockEntityType<WoodRackEntity> WOOD_RACK_ENTITY = BlockEntityType.Builder.create(WoodRackEntity::new, WOOD_RACK).build(null);;

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of("fleshz", "rotten_leather"), ROTTEN_LEATHER);
        Registry.register(Registries.ITEM, Identifier.of("fleshz", "hide"), HIDE);
        Registry.register(Registries.ITEM, Identifier.of("fleshz", "prepared_hide"), PREPARED_HIDE);
        Registry.register(Registries.ITEM, Identifier.of("fleshz", "wood_rack"), new BlockItem(WOOD_RACK, new Item.Settings()));
        Registry.register(Registries.BLOCK, Identifier.of("fleshz", "wood_rack"), WOOD_RACK);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, "fleshz:wood_rack_entity", WOOD_RACK_ENTITY);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(HIDE);
            entries.add(PREPARED_HIDE);
            entries.add(ROTTEN_LEATHER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> entries.add(WOOD_RACK));
        RecipeInit.init();
        RottenServerPacket.init();
        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "adventurez_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("naturalist")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "naturalist_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("fleshz", "meadow_compat"), FabricLoader.getInstance().getModContainer("fleshz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<Integer> rackItems = new ArrayList<Integer>();
            List<Integer> rackResultItems = new ArrayList<Integer>();

            for (int i = 0; i < RecipeInit.RACK_ITEM_LIST.size(); i++) {
                rackItems.add(Registries.ITEM.getRawId(RecipeInit.RACK_ITEM_LIST.get(i)));
            }
            for (int i = 0; i < RecipeInit.RACK_RESULT_ITEM_LIST.size(); i++) {
                rackResultItems.add(Registries.ITEM.getRawId(RecipeInit.RACK_RESULT_ITEM_LIST.get(i)));
            }
            ServerPlayNetworking.send(handler.player, new RackPacket(rackItems, rackResultItems, RecipeInit.RACK_RESULT_TIME_LIST));
        });
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!