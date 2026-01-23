package com.shadow_ninja.narutoreborn.ninjutsu;

import net.minecraft.resources.ResourceLocation;
import com.shadow_ninja.narutoreborn.NarutoReborn;
import com.shadow_ninja.narutoreborn.ninjutsu.katon.FireballJutsu;

import java.util.*;

public final class NinjutsuRegistry {
    private static final Map<ResourceLocation, Ininjutsu> NINJUTSU = new LinkedHashMap<>();
    private static boolean initialized;

    private NinjutsuRegistry() {
    }

    public static void bootstrap() {
        if (initialized) {
            return;
        }
        initialized = true;
        register(FireballJutsu.ID, new FireballJutsu());
        NarutoReborn.LOGGER.info("Registered {} ninjutsu entries", NINJUTSU.size());
    }

    public static void register(ResourceLocation id, Ininjutsu ninjutsu) {
        if (NINJUTSU.containsKey(id)) {
            throw new IllegalStateException("Duplicate ninjutsu id: " + id);
        }
        NINJUTSU.put(id, ninjutsu);
    }

    public static Optional<Ininjutsu> get(ResourceLocation id) {
        return Optional.ofNullable(NINJUTSU.get(id));
    }

    public static Collection<Ininjutsu> values() {
        return Collections.unmodifiableCollection(NINJUTSU.values());
    }

    public static Collection<ResourceLocation> ids() {
        return Collections.unmodifiableSet(NINJUTSU.keySet());
    }
}
