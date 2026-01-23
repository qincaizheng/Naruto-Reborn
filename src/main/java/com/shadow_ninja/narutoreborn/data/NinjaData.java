package com.shadow_ninja.narutoreborn.data;

import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NinjaData implements INBTSerializable<CompoundTag> {
    private static final String KEY_LEVEL = "Level";
    private static final String KEY_EXPERIENCE = "Experience";
    private static final String KEY_CHAKRA_CURRENT = "ChakraCurrent";
    private static final String KEY_CHAKRA_MAX = "ChakraMax";
    private static final String KEY_CHAKRA_REGEN = "ChakraRegen";
    private static final String KEY_CONTAINERS = "NinjutsuContainers";
    private static final String KEY_QUICKBAR = "Quickbar";

    public static final AttachmentSyncHandler<NinjaData> SYNC_HANDLER = new AttachmentSyncHandler<>() {
        @Override
        public void write(RegistryFriendlyByteBuf buf, NinjaData attachment, boolean initialSync) {
            buf.writeNbt(attachment.save());
        }

        @Override
        public NinjaData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable NinjaData previousValue) {
            CompoundTag tag = buf.readNbt();
            NinjaData data = new NinjaData();
            if (tag != null) {
                data.load(tag);
            }
            return data;
        }
    };

    private int level;
    private int experience;
    private double chakraCurrent;
    private double chakraMax = 100.0d;
    private double chakraRegenRate = 1.0d;

    private final List<NinjutsuContainer> ninjutsuContainers = new ArrayList<>();
    private final List<ResourceLocation> quickbarIds = new ArrayList<>();

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(0, level);
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = Math.max(0, experience);
    }

    public double getChakraCurrent() {
        return chakraCurrent;
    }

    public void setChakraCurrent(double chakraCurrent) {
        this.chakraCurrent = Math.max(0.0d, Math.min(chakraCurrent, chakraMax));
    }

    public double getChakraMax() {
        return chakraMax;
    }

    public void setChakraMax(double chakraMax) {
        this.chakraMax = Math.max(0.0d, chakraMax);
        if (chakraCurrent > this.chakraMax) {
            chakraCurrent = this.chakraMax;
        }
    }

    public double getChakraRegenRate() {
        return chakraRegenRate;
    }

    public void setChakraRegenRate(double chakraRegenRate) {
        this.chakraRegenRate = Math.max(0.0d, chakraRegenRate);
    }

    public List<NinjutsuContainer> getNinjutsuContainers() {
        return ninjutsuContainers;
    }

    public Optional<NinjutsuContainer> findContainer(ResourceLocation id) {
        return ninjutsuContainers.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    public boolean ownsNinjutsu(ResourceLocation id) {
        return findContainer(id).isPresent();
    }

    public void addNinjutsuContainer(NinjutsuContainer container) {
        ninjutsuContainers.add(container);
    }

    public List<ResourceLocation> getQuickbarIds() {
        return quickbarIds;
    }

    public void setQuickbarIds(List<ResourceLocation> ids) {
        quickbarIds.clear();
        quickbarIds.addAll(ids);
    }

    private static ListTag saveIdList(List<ResourceLocation> ids) {
        ListTag list = new ListTag();
        for (ResourceLocation id : ids) {
            list.add(StringTag.valueOf(id.toString()));
        }
        return list;
    }

    private static void loadIdList(List<ResourceLocation> out, CompoundTag tag, String key) {
        out.clear();
        ListTag list = tag.getList(key, 8); // 8 = StringTag
        for (int i = 0; i < list.size(); i++) {
            try {
                out.add(ResourceLocation.parse(list.getString(i)));
            } catch (Exception ignored) {
            }
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(KEY_LEVEL, level);
        tag.putInt(KEY_EXPERIENCE, experience);
        tag.putDouble(KEY_CHAKRA_CURRENT, chakraCurrent);
        tag.putDouble(KEY_CHAKRA_MAX, chakraMax);
        tag.putDouble(KEY_CHAKRA_REGEN, chakraRegenRate);

        ListTag containerList = new ListTag();
        for (NinjutsuContainer c : ninjutsuContainers) {
            containerList.add(c.save());
        }
        tag.put(KEY_CONTAINERS, containerList);

        tag.put(KEY_QUICKBAR, saveIdList(quickbarIds));
        return tag;
    }

    public void load(CompoundTag tag) {
        level = tag.getInt(KEY_LEVEL);
        experience = tag.getInt(KEY_EXPERIENCE);
        chakraCurrent = tag.getDouble(KEY_CHAKRA_CURRENT);
        chakraMax = tag.getDouble(KEY_CHAKRA_MAX);
        chakraRegenRate = tag.getDouble(KEY_CHAKRA_REGEN);

        ninjutsuContainers.clear();
        ListTag containersTag = tag.getList(KEY_CONTAINERS, Tag.TAG_COMPOUND);
        for (int i = 0; i < containersTag.size(); i++) {
            ninjutsuContainers.add(NinjutsuContainer.load(containersTag.getCompound(i)));
        }

        loadIdList(quickbarIds, tag, KEY_QUICKBAR);
        setChakraMax(chakraMax);
        setChakraCurrent(chakraCurrent);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return save();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        load(compoundTag);
    }
}
