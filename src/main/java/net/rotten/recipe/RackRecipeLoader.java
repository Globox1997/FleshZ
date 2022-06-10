package net.rotten.recipe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RackRecipeLoader implements SimpleSynchronousResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Identifier getFabricId() {
        return new Identifier("rotten", "rack_items");
    }

    @Override
    public void reload(ResourceManager manager) {

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("rack_items", id -> id.getPath().endsWith(".json")).entrySet()) {
            Identifier id = entry.getKey();
            try {
                InputStream stream = entry.getValue().getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                if (Registry.ITEM.get(new Identifier(data.get("item").getAsString())).toString().equals("air")) {
                    LOGGER.info("{} is not a valid item identifier at resouce {}", data.get("item").getAsString(), id.toString());
                    continue;
                }
                if (Registry.ITEM.get(new Identifier(data.get("result").getAsString())).toString().equals("air")) {
                    LOGGER.info("{} is not a valid item identifier at resouce {}", data.get("result").getAsString(), id.toString());
                    continue;
                }
                if (data.get("time").getAsInt() < 0) {
                    LOGGER.info("{} is not a valid time at resouce {}", data.get("time").getAsInt(), id.toString());
                    continue;
                }

                RecipeInit.RACK_ITEM_LIST.add((Item) Registry.ITEM.get(new Identifier(data.get("item").getAsString())));
                RecipeInit.RACK_RESULT_ITEM_LIST.add((Item) Registry.ITEM.get(new Identifier(data.get("result").getAsString())));
                RecipeInit.RACK_RESULT_TIME_LIST.add(data.get("time").getAsInt());

            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        }

        manager.findResources("rack_items", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                if (Registry.ITEM.get(new Identifier(data.get("item").getAsString())).toString().equals("air")) {
                    LOGGER.info("{} is not a valid item identifier at resouce {}", data.get("item").getAsString(), id.toString());
                    return;
                }
                if (Registry.ITEM.get(new Identifier(data.get("result").getAsString())).toString().equals("air")) {
                    LOGGER.info("{} is not a valid item identifier at resouce {}", data.get("result").getAsString(), id.toString());
                    return;
                }
                if (data.get("time").getAsInt() < 0) {
                    LOGGER.info("{} is not a valid time at resouce {}", data.get("time").getAsInt(), id.toString());
                    return;
                }

                RecipeInit.RACK_ITEM_LIST.add((Item) Registry.ITEM.get(new Identifier(data.get("item").getAsString())));
                RecipeInit.RACK_RESULT_ITEM_LIST.add((Item) Registry.ITEM.get(new Identifier(data.get("result").getAsString())));
                RecipeInit.RACK_RESULT_TIME_LIST.add(data.get("time").getAsInt());

            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });
    }

}