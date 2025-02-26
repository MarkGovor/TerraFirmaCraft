/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.items;

import net.minecraft.world.item.Tier;

import net.dries007.tfc.common.TFCTags;

public class ChiselItem extends ToolItem
{
    public ChiselItem(Tier tier, float attackDamage, float attackSpeed, Properties properties)
    {
        super(tier, attackDamage, attackSpeed, TFCTags.Blocks.MINEABLE_WITH_CHISEL, properties);
    }

    // todo implement stuff
}