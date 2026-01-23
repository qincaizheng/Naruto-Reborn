package com.shadow_ninja.narutoreborn.ninjutsu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

/**
 * Holds one ninjutsu instance ID plus its player-specific state.
 */
public class NinjutsuContainer {
    private static final String KEY_ID = "Id";
    private static final String KEY_UNLOCKED = "Unlocked";
    private static final String KEY_ENABLED = "Enabled";
    private static final String KEY_POWER = "Power";
    private static final String KEY_MIN_POWER = "MinPower";
    private static final String KEY_MAX_POWER = "MaxPower";
    private static final String KEY_CHAKRA_COST = "ChakraCost";
    private static final String KEY_COOLDOWN_UNTIL = "CooldownUntil";
    private static final String KEY_DURATION_REMAINING = "DurationRemaining";
    private static final String KEY_OWNER_UUID = "OwnerUUID";
    private static final String KEY_OWNER_ENTITY_ID = "OwnerEntityId";
    private static final String KEY_CLIENT_CACHE = "ClientCache";

    private ResourceLocation id;
    private boolean unlocked;
    private boolean enabled = true;
    private double power = Double.NaN;
    private double minPower = Double.NaN;
    private double maxPower = Double.NaN;
    private double chakraCost = 0.0d;
    private long cooldownUntilTick = 0L;
    private int durationRemainingTicks = 0;
    private UUID ownerUuid;
    private int ownerEntityId = -1;
    private CompoundTag clientCache = new CompoundTag();

    public NinjutsuContainer(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation id() {
        return id;
    }

    public boolean unlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double power() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double minPower() {
        return minPower;
    }

    public void setMinPower(double minPower) {
        this.minPower = minPower;
    }

    public double maxPower() {
        return maxPower;
    }

    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    public double chakraCost() {
        return chakraCost;
    }

    public void setChakraCost(double chakraCost) {
        this.chakraCost = Math.max(0.0d, chakraCost);
    }

    public long cooldownUntilTick() {
        return cooldownUntilTick;
    }

    public void startCooldown(long nowTick, long cooldownTicks) {
        long target = nowTick + Math.max(0L, cooldownTicks);
        cooldownUntilTick = Math.max(cooldownUntilTick, target);
    }

    public boolean isCoolingDown(long nowTick) {
        return nowTick < cooldownUntilTick;
    }

    public double remainingCooldownSeconds(long nowTick) {
        if (!isCoolingDown(nowTick)) {
            return 0.0d;
        }
        return (cooldownUntilTick - nowTick) / 20.0d;
    }

    public int durationRemainingTicks() {
        return durationRemainingTicks;
    }

    public void setDurationRemainingTicks(int ticks) {
        durationRemainingTicks = Math.max(0, ticks);
    }

    public void tickDuration() {
        if (durationRemainingTicks > 0) {
            durationRemainingTicks--;
        }
    }

    public Optional<UUID> ownerUuid() {
        return Optional.ofNullable(ownerUuid);
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public int ownerEntityId() {
        return ownerEntityId;
    }

    public void setOwnerEntityId(int ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    public CompoundTag clientCache() {
        return clientCache;
    }

    public void setClientCache(CompoundTag clientCache) {
        this.clientCache = clientCache == null ? new CompoundTag() : clientCache;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(KEY_ID, id.toString());
        tag.putBoolean(KEY_UNLOCKED, unlocked);
        tag.putBoolean(KEY_ENABLED, enabled);
        if (!Double.isNaN(power)) {
            tag.putDouble(KEY_POWER, power);
        }
        if (!Double.isNaN(minPower)) {
            tag.putDouble(KEY_MIN_POWER, minPower);
        }
        if (!Double.isNaN(maxPower)) {
            tag.putDouble(KEY_MAX_POWER, maxPower);
        }
        tag.putDouble(KEY_CHAKRA_COST, chakraCost);
        tag.putLong(KEY_COOLDOWN_UNTIL, cooldownUntilTick);
        tag.putInt(KEY_DURATION_REMAINING, durationRemainingTicks);
        if (ownerUuid != null) {
            tag.putUUID(KEY_OWNER_UUID, ownerUuid);
        }
        if (ownerEntityId >= 0) {
            tag.putInt(KEY_OWNER_ENTITY_ID, ownerEntityId);
        }
        tag.put(KEY_CLIENT_CACHE, clientCache.copy());
        return tag;
    }

    public static NinjutsuContainer load(CompoundTag tag) {
        ResourceLocation id = ResourceLocation.parse(tag.getString(KEY_ID));
        NinjutsuContainer container = new NinjutsuContainer(id);
        container.unlocked = tag.getBoolean(KEY_UNLOCKED);
        container.enabled = tag.getBoolean(KEY_ENABLED);
        if (tag.contains(KEY_POWER)) {
            container.power = tag.getDouble(KEY_POWER);
        }
        if (tag.contains(KEY_MIN_POWER)) {
            container.minPower = tag.getDouble(KEY_MIN_POWER);
        }
        if (tag.contains(KEY_MAX_POWER)) {
            container.maxPower = tag.getDouble(KEY_MAX_POWER);
        }
        container.chakraCost = tag.getDouble(KEY_CHAKRA_COST);
        container.cooldownUntilTick = tag.getLong(KEY_COOLDOWN_UNTIL);
        container.durationRemainingTicks = tag.getInt(KEY_DURATION_REMAINING);
        if (tag.hasUUID(KEY_OWNER_UUID)) {
            container.ownerUuid = tag.getUUID(KEY_OWNER_UUID);
        }
        if (tag.contains(KEY_OWNER_ENTITY_ID)) {
            container.ownerEntityId = tag.getInt(KEY_OWNER_ENTITY_ID);
        }
        if (tag.contains(KEY_CLIENT_CACHE)) {
            container.clientCache = tag.getCompound(KEY_CLIENT_CACHE);
        }
        return container;
    }
}
