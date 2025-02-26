/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.items;

import net.minecraft.world.food.FoodProperties;

public enum Food
{
    // Berries
    BLACKBERRY(false, true),
    BLUEBERRY(false, true),
    BUNCHBERRY(false, true),
    CLOUDBERRY(false, true),
    CRANBERRY(false, true),
    ELDERBERRY(false, true),
    GOOSEBERRY(false, true),
    RASPBERRY(false, true),
    SNOWBERRY(false, true),
    STRAWBERRY(false, true),
    WINTERGREEN_BERRY(false, true),
    // Fruit
    BANANA,
    CHERRY,
    GREEN_APPLE,
    LEMON,
    OLIVE,
    ORANGE,
    PEACH,
    PLUM,
    RED_APPLE,
    // Grains
    BARLEY,
    BARLEY_GRAIN,
    BARLEY_DOUGH,
    BARLEY_FLOUR,
    BARLEY_BREAD,
    MAIZE,
    MAIZE_GRAIN,
    MAIZE_DOUGH,
    MAIZE_FLOUR,
    MAIZE_BREAD,
    OAT,
    OAT_GRAIN,
    OAT_DOUGH,
    OAT_FLOUR,
    OAT_BREAD,
    RYE,
    RYE_GRAIN,
    RYE_DOUGH,
    RYE_FLOUR,
    RYE_BREAD,
    RICE,
    RICE_GRAIN,
    RICE_DOUGH,
    RICE_FLOUR, // todo: remove rice flour, dough, and bread. Add cooked rice. Add a pot boiling recipe for rice grain -> cooked rice. Eventually, add a rice addon to soups maybe?
    RICE_BREAD,
    WHEAT,
    WHEAT_GRAIN,
    WHEAT_DOUGH,
    WHEAT_FLOUR,
    WHEAT_BREAD,
    // Vegetables
    BEET,
    CABBAGE,
    CARROT,
    GARLIC,
    GREEN_BEAN,
    GREEN_BELL_PEPPER,
    ONION,
    POTATO,
    RED_BELL_PEPPER,
    SOYBEAN,
    SUGARCANE,
    SQUASH,
    TOMATO,
    YELLOW_BELL_PEPPER,
    CHEESE,
    COOKED_EGG,
    DRIED_SEAWEED,
    CATTAIL_ROOT,
    DRIED_KELP,
    // Meats
    BEEF(true, false),
    PORK(true, false),
    CHICKEN(true, false),
    MUTTON(true, false),
    BEAR(true, false),
    HORSE_MEAT(true, false),
    PHEASANT(true, false),
    VENISON(true, false),
    WOLF(true, false),
    RABBIT(true, false),
    HYENA(true, false),
    DUCK(true, false),
    CHEVON(true, false),
    GRAN_FELINE(true, false),
    CAMELIDAE(true, false),
    COD(true, false),
    SALMON(true, false),
    BLUEGILL(true, false),
    TROPICAL_FISH(true, false),
    // Cooked Meats
    COOKED_BEEF(true, false),
    COOKED_PORK(true, false),
    COOKED_CHICKEN(true, false),
    COOKED_MUTTON(true, false),
    COOKED_BEAR(true, false),
    COOKED_HORSE_MEAT(true, false),
    COOKED_PHEASANT(true, false),
    COOKED_VENISON(true, false),
    COOKED_WOLF(true, false),
    COOKED_RABBIT(true, false),
    COOKED_HYENA(true, false),
    COOKED_DUCK(true, false),
    COOKED_CHEVON(true, false),
    COOKED_CAMELIDAE(true, false),
    COOKED_GRAN_FELINE(true, false),
    COOKED_COD(true, false),
    COOKED_SALMON(true, false),
    COOKED_BLUEGILL(true, false),
    COOKED_TROPICAL_FISH(true, false);

    private final boolean meat, fast;

    Food()
    {
        this(false, false);
    }

    Food(boolean meat, boolean fast)
    {
        this.meat = meat;
        this.fast = fast;
    }

    public FoodProperties getFoodProperties()
    {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (meat) builder.meat();
        if (fast) builder.fast();
        return builder.nutrition(4).saturationMod(0.3f).build();
    }
}
