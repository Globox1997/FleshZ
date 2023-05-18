package net.fleshz.compat;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fleshz.recipe.RecipeInit;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class FleshEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public FleshEmiRecipe(Item item) {
        this.id = new Identifier("fleshz", "rack_" + Registries.ITEM.getId(item).getPath());
        int index = RecipeInit.RACK_ITEM_LIST.indexOf(item);

        this.input = List.of(EmiIngredient.of(Ingredient.ofItems(item)));
        this.output = List.of(EmiStack.of(RecipeInit.RACK_RESULT_ITEM_LIST.get(index)));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FleshEmiPlugin.RACK_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 76;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(input.get(0), 0, 0);
        widgets.addSlot(output.get(0), 58, 0).recipeContext(this);
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }
}
