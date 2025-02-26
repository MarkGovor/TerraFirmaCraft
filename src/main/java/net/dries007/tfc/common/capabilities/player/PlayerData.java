/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.capabilities.player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.network.PacketHandler;
import net.dries007.tfc.network.PlayerDataUpdatePacket;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;

public class PlayerData implements ICapabilitySerializable<CompoundTag>
{
    public static final long MAX_INTOXICATED_TICKS = 36 * ICalendar.TICKS_IN_HOUR; // A day and a half. Each drink gives you 4 hours of time

    private final Player player;
    private final LazyOptional<PlayerData> capability;
    @Nullable private CompoundTag delayedFoodNbt;

    private long lastDrinkTick;
    private long intoxicationTick;

    public PlayerData(Player player)
    {
        this.player = player;
        this.capability = LazyOptional.of(() -> this);
    }

    /**
     * @return The number of remaining ticks the player is intoxicated for
     */
    public long getIntoxicatedTicks()
    {
        return Math.max(0, Calendars.SERVER.getTicks() - intoxicationTick);
    }

    /**
     * Intoxicates the player for at least the next {@code ticks}.
     */
    public void addIntoxicatedTicks(long ticks)
    {
        long currentTick = Calendars.SERVER.getTicks();
        if (intoxicationTick < currentTick)
        {
            intoxicationTick = currentTick;
        }
        intoxicationTick += ticks;
        if (intoxicationTick > currentTick + MAX_INTOXICATED_TICKS)
        {
            intoxicationTick = currentTick + MAX_INTOXICATED_TICKS;
        }
        sync();
    }

    public long getLastDrinkTick()
    {
        return lastDrinkTick;
    }

    public void setLastDrinkTick(long lastDrinkTick)
    {
        this.lastDrinkTick = lastDrinkTick;
        sync();
    }

    public void sync()
    {
        if (player instanceof final ServerPlayer serverPlayer)
        {
            PacketHandler.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerDataUpdatePacket(lastDrinkTick, intoxicationTick));
        }
    }

    public void updateFromPacket(long lastDrinkTick, long intoxicationTick)
    {
        this.lastDrinkTick = lastDrinkTick;
        this.intoxicationTick = intoxicationTick;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return cap == PlayerDataCapability.CAPABILITY ? capability.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        final CompoundTag nbt = new CompoundTag();
        if (player.getFoodData() instanceof TFCFoodData)
        {
            nbt.put("food", ((TFCFoodData) player.getFoodData()).serializeToPlayerData());
        }
        nbt.putLong("lastDrinkTick", lastDrinkTick);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        delayedFoodNbt = nbt.contains("food", Tag.TAG_COMPOUND) ? nbt.getCompound("food") : null;
        if (player.getFoodData() instanceof TFCFoodData)
        {
            writeTo((TFCFoodData) player.getFoodData());
        }
        lastDrinkTick = nbt.getLong("lastDrinkTick");
    }

    public void writeTo(TFCFoodData stats)
    {
        if (delayedFoodNbt != null)
        {
            stats.deserializeFromPlayerData(delayedFoodNbt);
            delayedFoodNbt = null;
        }
    }
}
