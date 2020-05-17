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

public class Leather implements ModInitializer {

    public static final Flesh FLESH = new Flesh();
    public static final WoodReck WOOD_RECK = new WoodReck(FabricBlockSettings.copy(Blocks.OAK_PLANKS));
    public static final BlockEntityType<WoodReckEntity> WOOD_RECK_ENTITY = BlockEntityType.Builder
            .create(WoodReckEntity::new, WOOD_RECK).build(null);;

    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM, new Identifier("rotten", "flesh"), FLESH);
        Registry.register(Registry.ITEM, new Identifier("rotten", "woodreck"),
                new BlockItem(WOOD_RECK, new Item.Settings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("rotten", "woodreck"), WOOD_RECK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "rotten:woodreckentity", WOOD_RECK_ENTITY);
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!