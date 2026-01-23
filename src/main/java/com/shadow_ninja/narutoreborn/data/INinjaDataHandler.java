package com.shadow_ninja.narutoreborn.data;

import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public interface INinjaDataHandler {
    NinjaData get(Player player);

    void save(Player player, NinjaData data);

    int getLevel(Player player);

    void setLevel(Player player, int level);

    int getExperience(Player player);

    void setExperience(Player player, int experience);

    double getChakraCurrent(Player player);

    void setChakraCurrent(Player player, double current);

    double getChakraMax(Player player);

    void setChakraMax(Player player, double max);

    double getChakraRegen(Player player);

    void setChakraRegen(Player player, double regen);

    List<ResourceLocation> getQuickbar(Player player);

    void setQuickbar(Player player, List<ResourceLocation> ids);

    List<NinjutsuContainer> getContainers(Player player);

    Optional<NinjutsuContainer> findContainer(Player player, ResourceLocation id);

    boolean learnNinjutsu(Player player, ResourceLocation id);
}
