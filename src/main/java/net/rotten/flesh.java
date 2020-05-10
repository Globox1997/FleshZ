package net.rotten;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.FoodComponent;

public class flesh extends Item {
    public boolean isonrack = false;
    public static final FoodComponent FOOD_COMPONENT = (new FoodComponent.Builder()).hunger(5).saturationModifier(0.8F)
            .meat().build();

    public flesh() {
        super(new Item.Settings().food(FOOD_COMPONENT).group(ItemGroup.FOOD));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        ItemStack item = player.getMainHandStack();
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        if (!world.isClient && state.getBlock() == leth.WOODRECK) {

            item.decrement(1);
            isonrack = true;
            return ActionResult.SUCCESS;

        } else
            return ActionResult.PASS;

    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
        Random random = new Random();
        int randomNumber = random.nextInt() % 2;
        if (randomNumber < 0) {
            randomNumber = randomNumber * (-1);
        }
        if (!world.isClient && randomNumber == 0) {
            StatusEffectInstance hunger = new StatusEffectInstance(StatusEffect.byRawId(17), 800, 1, true, false);
            entity.addStatusEffect(hunger);
        }

        return super.finishUsing(stack, world, entity);
    }
}
