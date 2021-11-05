package net.treset.compass;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.treset.compass.config.CompassConfigScreen;
import org.lwjgl.glfw.GLFW;

public class CompassModClient implements ClientModInitializer {

    public static KeyBinding configGuiKeybind;

    @Override
    public void onInitializeClient() {

        configGuiKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compass.config_gui", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_H, // The keycode of the key
                "category.compass.compass" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configGuiKeybind.isPressed()) {
                CompassConfigScreen.buildScreen();
                MinecraftClient.getInstance().setScreen(CompassConfigScreen.configScreen);
                CompassMod.LOGGER.info("H pressed");
            }
        });

        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            ConfigScreenBuilder.setMain(CompassMod.MOD_ID, new ClothConfigScreenBuilder());
        }

        CompassMod.LOGGER.info("Client initialized!");
    }
}
