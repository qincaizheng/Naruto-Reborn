package com.shadow_ninja.narutoreborn.network;

import com.shadow_ninja.narutoreborn.NarutoReborn;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Client -> Server: request to use a quickbar slot.
 */
public record UseQuickbarNinjutsuC2SPayload(int slot) implements CustomPacketPayload {
    public static final Type<UseQuickbarNinjutsuC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(NarutoReborn.MODID, "use_quickbar_ninjutsu"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UseQuickbarNinjutsuC2SPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeVarInt(payload.slot),
                    buf -> new UseQuickbarNinjutsuC2SPayload(buf.readVarInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
