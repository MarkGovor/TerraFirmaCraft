/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.capabilities.forge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import net.dries007.tfc.common.items.VesselItem;

public class ForgingHandler implements IForging
{
    private final LazyOptional<IForging> capability;
    private final ItemStack stack;

    private final ForgeSteps steps;

    private int work;
    @Nullable private ResourceLocation recipe;

    private boolean initialized;

    public ForgingHandler(ItemStack stack)
    {
        this.capability = LazyOptional.of(() -> this);
        this.stack = stack;

        this.work = 0;
        this.recipe = null;
        this.steps = new ForgeSteps();
    }

    public ItemStack getContainer()
    {
        return stack;
    }

    @Override
    public int getWork()
    {
        return work;
    }

    @Override
    public void setWork(int work)
    {
        this.work = work;
        save();
    }

    @Nullable
    @Override
    public ResourceLocation getRecipeName()
    {
        return recipe;
    }

    @Override
    public void setRecipe(@Nullable ResourceLocation recipe)
    {
        this.recipe = recipe;
        save();
    }

    @Nullable
    @Override
    public ForgeStep getStep(int step)
    {
        return steps.getStep(step);
    }

    @Override
    public boolean matches(ForgeRule rule)
    {
        return rule.matches(steps);
    }

    @Override
    public void addStep(@Nullable ForgeStep step)
    {
        steps.addStep(step);
        save();
    }

    @Override
    public void reset()
    {
        save();
    }

    /**
     * @see VesselItem.VesselCapability#load()
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (cap == ForgingCapability.CAPABILITY)
        {
            load();
            return capability.cast();
        }
        return LazyOptional.empty();
    }

    private void load()
    {
        if (!initialized)
        {
            initialized = true;

            final CompoundTag tag = stack.getTagElement("tfc:forging");
            if (tag != null)
            {
                work = tag.getInt("work");
                steps.read(tag);
                recipe = tag.contains("recipe") ? new ResourceLocation(tag.getString("recipe")) : null;
            }
        }
    }

    private void save()
    {
        if (work == 0 && !steps.any() && recipe == null)
        {
            // No defining data, so don't save anything
            stack.removeTagKey("tfc:forging");
        }
        else
        {
            final CompoundTag tag = stack.getOrCreateTagElement("tfc:forging");
            tag.putInt("work", work);
            steps.write(tag);
            if (recipe != null)
            {
                tag.putString("recipe", recipe.toString());
            }
        }
    }
}