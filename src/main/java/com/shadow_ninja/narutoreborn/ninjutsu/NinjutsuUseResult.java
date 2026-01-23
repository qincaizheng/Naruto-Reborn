package com.shadow_ninja.narutoreborn.ninjutsu;

import net.minecraft.network.chat.Component;

public record NinjutsuUseResult(boolean success, Component message) {
    public static NinjutsuUseResult success(Component message) {
        return new NinjutsuUseResult(true, message);
    }

    public static NinjutsuUseResult fail(Component message) {
        return new NinjutsuUseResult(false, message);
    }
}
