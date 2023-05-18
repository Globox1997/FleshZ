package net.fleshz.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.fleshz.FleshMain;
import net.fleshz.recipe.RecipeInit;
import net.minecraft.util.Identifier;

public class FleshEmiPlugin implements EmiPlugin {

    public static final Identifier MY_SPRITE_SHEET = new Identifier("fleshz", "textures/item/hide.png");
    public static final EmiStack RACK = EmiStack.of(FleshMain.WOOD_RACK);
    public static final EmiRecipeCategory RACK_CATEGORY = new EmiRecipeCategory(new Identifier("fleshz", "rack"), RACK, new EmiTexture(MY_SPRITE_SHEET, 0, 0, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(RACK_CATEGORY);
        registry.addWorkstation(RACK_CATEGORY, RACK);

        for (int i = 0; i < RecipeInit.RACK_ITEM_LIST.size(); i++) {
            registry.addRecipe(new FleshEmiRecipe(RecipeInit.RACK_ITEM_LIST.get(i)));
        }
    }

}
