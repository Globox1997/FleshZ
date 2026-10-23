package net.fleshz.init;

import net.fleshz.FleshMain;
import net.fleshz.recipe.RackRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeInit {

    public static final RecipeType<RackRecipe> RACK_DRYING = Registry.register(Registries.RECIPE_TYPE, FleshMain.identifierOf("rack_drying"), new RecipeType<RackRecipe>() {
        @Override
        public String toString() {
            return "fleshz:rack_drying";
        }
    });
    public static final RecipeSerializer<RackRecipe> RACK_DRYING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, FleshMain.identifierOf("rack_drying"), new RackRecipe.Serializer());

    public static void init() {
    }
}
