package com.shadow_ninja.narutoreborn.ninjutsu;

import com.shadow_ninja.narutoreborn.data.NinjaData;
import com.shadow_ninja.narutoreborn.ninjutsu.katon.FireballJutsu;

/**
 * Default starting values for new/first-time players.
 */
public final class NinjaDefaults {
    public static void applyIfEmpty(NinjaData data) {
        // If it's a fresh profile, initialize basic stats and jutsu.
        if (data.getLevel() <= 0 && data.getChakraCurrent() <= 0 && data.getNinjutsuContainers().isEmpty() && data.getQuickbarIds().isEmpty()) {
            data.setLevel(1);
            data.setExperience(0);
            data.setChakraMax(100.0d);
            data.setChakraCurrent(100.0d);
            data.setChakraRegenRate(1.0d);

            NinjutsuContainer starter = new NinjutsuContainer(FireballJutsu.ID);
            starter.setUnlocked(true);
            starter.setEnabled(true);
            data.addNinjutsuContainer(starter);
            data.getQuickbarIds().add(FireballJutsu.ID);
        }
    }
}
