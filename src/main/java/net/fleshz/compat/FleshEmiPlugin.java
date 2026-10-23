package net.fleshz.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.fleshz.init.BlockInit;
import net.fleshz.init.RecipeInit;
import net.fleshz.recipe.RackRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;

public class FleshEmiPlugin implements EmiPlugin {

    public static final Identifier MY_SPRITE_SHEET = Identifier.of("fleshz", "textures/item/hide.png");
    public static final EmiStack RACK = EmiStack.of(BlockInit.OAK_WOOD_RACK);
    public static final EmiRecipeCategory RACK_CATEGORY = new EmiRecipeCategory(Identifier.of("fleshz", "rack"), RACK, new EmiTexture(MY_SPRITE_SHEET, 0, 0, 16, 16, 16, 16, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(RACK_CATEGORY);
        registry.addWorkstation(RACK_CATEGORY, RACK);

        for (RecipeEntry<RackRecipe> entry : registry.getRecipeManager().listAllOfType(RecipeInit.RACK_DRYING)) {
            registry.addRecipe(new FleshEmiRecipe(entry));
        }
    }

}
