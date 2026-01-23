package com.shadow_ninja.narutoreborn.data;

import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NinjaDataHandler implements INinjaDataHandler {
    public static final INinjaDataHandler INSTANCE = new NinjaDataHandler();

    private NinjaDataHandler() {
    }

    public static INinjaDataHandler instance() {
        return INSTANCE;
    }

    @Override
    public NinjaData get(Player player) {
        return player.getData(AttachmentInit.NINJA_DATA.get());
    }

    @Override
    public void save(Player player, NinjaData data) {
        player.setData(AttachmentInit.NINJA_DATA.get(), data);
    }

    @Override
    public int getLevel(Player player) {
        return get(player).getLevel();
    }

    @Override
    public void setLevel(Player player, int level) {
        NinjaData data = get(player);
        data.setLevel(level);
        save(player, data);
    }

    @Override
    public int getExperience(Player player) {
        return get(player).getExperience();
    }

    @Override
    public void setExperience(Player player, int experience) {
        NinjaData data = get(player);
        data.setExperience(experience);
        save(player, data);
    }

    @Override
    public double getChakraCurrent(Player player) {
        return get(player).getChakraCurrent();
    }

    @Override
    public void setChakraCurrent(Player player, double current) {
        NinjaData data = get(player);
        data.setChakraCurrent(current);
        save(player, data);
    }

    @Override
    public double getChakraMax(Player player) {
        return get(player).getChakraMax();
    }

    @Override
    public void setChakraMax(Player player, double max) {
        NinjaData data = get(player);
        data.setChakraMax(max);
        save(player, data);
    }

    @Override
    public double getChakraRegen(Player player) {
        return get(player).getChakraRegenRate();
    }

    @Override
    public void setChakraRegen(Player player, double regen) {
        NinjaData data = get(player);
        data.setChakraRegenRate(regen);
        save(player, data);
    }

    @Override
    public List<ResourceLocation> getQuickbar(Player player) {
        return new ArrayList<>(get(player).getQuickbarIds());
    }

    @Override
    public void setQuickbar(Player player, List<ResourceLocation> ids) {
        NinjaData data = get(player);
        data.setQuickbarIds(ids);
        save(player, data);
    }

    @Override
    public List<NinjutsuContainer> getContainers(Player player) {
        return new ArrayList<>(get(player).getNinjutsuContainers());
    }

    @Override
    public Optional<NinjutsuContainer> findContainer(Player player, ResourceLocation id) {
        return get(player).findContainer(id);
    }

    @Override
    public boolean learnNinjutsu(Player player, ResourceLocation id) {
        NinjaData data = get(player);
        if (data.ownsNinjutsu(id)) {
            return false;
        }
        NinjutsuContainer container = new NinjutsuContainer(id);
        container.setUnlocked(true);
        container.setEnabled(true);
        data.addNinjutsuContainer(container);
        save(player, data);
        return true;
    }
}
