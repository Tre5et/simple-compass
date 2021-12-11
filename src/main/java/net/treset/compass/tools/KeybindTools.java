package net.treset.compass.tools;

import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.treset.compass.CompassClient;
import net.treset.compass.config.gui.ConfigGui;

import java.util.ArrayList;
import java.util.List;

public class KeybindTools {
    private static final List<KeyBinding> keys = new ArrayList<>();

    public static void registerKeybind(String key, int keyCode) {
        KeyBinding keybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
           key, InputUtil.Type.KEYSYM, keyCode, "category.compass.compass")); //specify keybind

        keys.add(keybinding);
    }

    public static void resolveKeybinds() {
        while(keys.get(0).isPressed()) { //listen for keypress
            CompassClient.configScreen = new ConfigGui(); //open config screen
            GuiBase.openGui(CompassClient.configScreen);
        }
    }
}
