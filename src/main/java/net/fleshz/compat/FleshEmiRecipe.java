package net.fleshz.compat;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fleshz.recipe.RackRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FleshEmiRecipe implements EmiRecipe {
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#.#");

    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final int time;

    public FleshEmiRecipe(RecipeEntry<RackRecipe> entry) {
        this.id = entry.id();
        this.input = List.of(EmiIngredient.of(entry.value().getIngredient()));
        this.output = List.of(EmiStack.of(entry.value().getResult(MinecraftClient.getInstance().world.getRegistryManager())));
        this.time = entry.value().getTime();
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
        return 82;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(26, 1, Math.max(this.time * 50, 50)).tooltip((mouseX, mouseY) -> Stream.of("faster", "slower", "paused")
                .map(key -> TooltipComponent.of(Text.translatable("emi.fleshz.rack." + key).asOrderedText())).toList());
        widgets.addSlot(input.get(0), 0, 0);
        widgets.addSlot(output.get(0), 58, 0).recipeContext(this);
        widgets.addText(getTimeText(this.time), 0, 21, -1, true);
    }

    @Override
    public int getDisplayHeight() {
        return 30;
    }

    private static Text getTimeText(int ticks) {
        float seconds = ticks / 20.0F;
        if (seconds >= 60.0F) {
            return Text.translatable("emi.fleshz.rack.time.minutes", TIME_FORMAT.format(seconds / 60.0F));
        }
        return Text.translatable("emi.fleshz.rack.time.seconds", TIME_FORMAT.format(seconds));
    }
}
