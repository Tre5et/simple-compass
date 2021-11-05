package net.treset.compass.config;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.treset.compass.CompassMod;

public class CompassConfigScreen {
    public static Screen configScreen;
    public static void buildScreen() {
        Screen parentScreen = MinecraftClient.getInstance().currentScreen;

        ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder(() -> ConfigBuilder.create().transparentBackground());
        configScreen = screenBuilder.build(parentScreen, CompassMod.config);
    }
}
