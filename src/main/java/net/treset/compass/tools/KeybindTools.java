package net.treset.compass.tools;

import net.minecraft.client.MinecraftClient;
import net.treset.compass.CompassClient;

public class KeybindTools {
    public static void openConfig(String name) {
        if(MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().setScreen(CompassClient.CONFIG_SCREEN);
    }
}
