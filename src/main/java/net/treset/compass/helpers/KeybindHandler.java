package net.treset.compass.helpers;

import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.treset.compass.CompassModClient;
import net.treset.compass.config.gui.CompassConfigGui;

import java.util.ArrayList;
import java.util.List;

public class KeybindHandler {
    private static final List<KeyBinding> keys = new ArrayList<>();

    public static void registerKeybind(String key, int keyCode) {
        KeyBinding keybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
           key, InputUtil.Type.KEYSYM, keyCode, "category.compass.compass"));

        keys.add(keybinding);
    }

    public static void resolveKeybinds() {
        while(keys.get(0).isPressed()) {
            CompassModClient.configScreen = new CompassConfigGui();
            GuiBase.openGui(CompassModClient.configScreen);
        }
    }
}
