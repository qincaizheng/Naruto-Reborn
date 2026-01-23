package com.shadow_ninja.narutoreborn.ninjutsu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Core ninjutsu contract aligned with README requirements.
 */
public interface Ininjutsu {
    void use(Player player, Level level);

    double getPower();

    void setPower(double power);

    double getRange();

    double getCost();

    void setCost(double cost);

    double getCooldown();

    void setCooldown(double cooldown);

    Component getDisplayName();

    ResourceLocation getIcon();

    Component getTooltip();

    String getCategory();

    double getMaxPower();

    double getMinPower();

    int getRequiredLevel();

    boolean getUnlocked();

    boolean getEnabled();

    int getDuration();
}
