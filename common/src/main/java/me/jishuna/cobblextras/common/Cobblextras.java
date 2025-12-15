package me.jishuna.cobblextras.common;

import net.minecraft.resources.ResourceLocation;

public final class Cobblextras {
    public static final String MOD_ID = "cobblextras";

    public static ResourceLocation key(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
