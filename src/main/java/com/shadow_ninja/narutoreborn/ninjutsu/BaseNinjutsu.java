package com.shadow_ninja.narutoreborn.ninjutsu;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Simple data-backed ninjutsu implementation.
 */
public abstract class BaseNinjutsu implements Ininjutsu {
    private double power;
    private final double maxPower;
    private final double minPower;
    private final int requiredLevel;
    private final String category;
    private final ResourceLocation icon;
    private final Component displayName;
    private final Component tooltip;
    private double range;
    private double cost;
    private double cooldown;
    private int duration;
    private boolean unlocked;
    private boolean enabled = true;

    protected BaseNinjutsu(Builder builder) {
        this.power = builder.power;
        this.maxPower = builder.maxPower;
        this.minPower = builder.minPower;
        this.requiredLevel = builder.requiredLevel;
        this.category = builder.category;
        this.icon = builder.icon;
        this.displayName = builder.displayName;
        this.tooltip = builder.tooltip;
        this.range = builder.range;
        this.cost = builder.cost;
        this.cooldown = builder.cooldown;
        this.duration = builder.duration;
        this.unlocked = builder.unlocked;
        this.enabled = builder.enabled;
    }

    @Override
    public void use(Player player, Level level) {
        NarutoReborn.LOGGER.debug("Using ninjutsu {} for player {}", displayName.getString(), player.getScoreboardName());
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = Math.max(minPower, Math.min(maxPower, power));
    }

    @Override
    public double getRange() {
        return range;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public double getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public Component getTooltip() {
        return tooltip;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public double getMaxPower() {
        return maxPower;
    }

    @Override
    public double getMinPower() {
        return minPower;
    }

    @Override
    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public boolean getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static class Builder {
        private double power;
        private double maxPower = 1.0d;
        private double minPower = 0.1d;
        private int requiredLevel = 0;
        private String category = "misc";
        private ResourceLocation icon = NarutoReborn.createRL("textures/gui/ninjutsu/missing.png");
        private Component displayName = Component.translatable("ninjutsu.narutoreborn.unnamed");
        private Component tooltip = Component.empty();
        private double range = 1.0d;
        private double cost = 0;
        private double cooldown = 0;
        private int duration = 0;
        private boolean unlocked = false;
        private boolean enabled = true;

        public Builder power(double power) {
            this.power = power;
            return this;
        }

        public Builder maxPower(double maxPower) {
            this.maxPower = maxPower;
            return this;
        }

        public Builder minPower(double minPower) {
            this.minPower = minPower;
            return this;
        }

        public Builder requiredLevel(int requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder range(double range) {
            this.range = range;
            return this;
        }

        public Builder cost(double cost) {
            this.cost = cost;
            return this;
        }

        public Builder cooldown(double cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder unlocked(boolean unlocked) {
            this.unlocked = unlocked;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
    }
}
