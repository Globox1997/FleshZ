package net.rotten;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class leth implements ModInitializer {

    public static final flesh FLESH = new flesh();
    public static final woodreck WOODRECK = new woodreck(FabricBlockSettings.copy(Blocks.OAK_PLANKS));
    public static final BlockEntityType<woodreckentity> WOODRECKENTITY = BlockEntityType.Builder
            .create(woodreckentity::new, WOODRECK).build(null);;

    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM, new Identifier("rotten", "flesh"), FLESH);
        Registry.register(Registry.ITEM, new Identifier("rotten", "woodreck"),
                new BlockItem(WOODRECK, new Item.Settings()));
        Registry.register(Registry.BLOCK, new Identifier("rotten", "woodreck"), WOODRECK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "rotten:woodreckentity", WOODRECKENTITY);
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!