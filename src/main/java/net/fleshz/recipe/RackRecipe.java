package net.fleshz.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fleshz.init.BlockInit;
import net.fleshz.init.RecipeInit;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class RackRecipe implements Recipe<SingleStackRecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int time;

    public RackRecipe(Ingredient ingredient, ItemStack result, int time) {
        this.ingredient = ingredient;
        this.result = result;
        this.time = time;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockInit.OAK_WOOD_RACK);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.RACK_DRYING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RACK_DRYING;
    }

    public static class Serializer implements RecipeSerializer<RackRecipe> {

        private static final MapCodec<RackRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                Codecs.NONNEGATIVE_INT.fieldOf("time").forGetter(recipe -> recipe.time)
        ).apply(instance, RackRecipe::new));

        private static final PacketCodec<RegistryByteBuf, RackRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, recipe -> recipe.ingredient,
                ItemStack.PACKET_CODEC, recipe -> recipe.result,
                PacketCodecs.VAR_INT, recipe -> recipe.time,
                RackRecipe::new);

        @Override
        public MapCodec<RackRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, RackRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
