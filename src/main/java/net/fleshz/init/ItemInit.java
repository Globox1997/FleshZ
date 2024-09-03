package net.fleshz.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fleshz.FleshMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemInit {

    public static final Item ROTTEN_LEATHER = register("rotten_leather", new Item(new Item.Settings()));
    public static final Item HIDE = register("hide", new Item(new Item.Settings()));
    public static final Item PREPARED_HIDE = register("prepared_hide", new Item(new Item.Settings()));

    private static Item register(String id, Item item) {
        return register(FleshMain.identifierOf(id), item);
    }

    private static Item register(Identifier id, Item item) {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, id, item);
    }

    public static void init() {
    }
}
