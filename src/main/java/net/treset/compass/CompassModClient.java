package net.treset.compass;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.treset.compass.config.CompassConfig;
import net.treset.compass.config.gui.CompassConfigGui;
import net.treset.compass.helpers.KeybindHandler;
import org.lwjgl.glfw.GLFW;

public class CompassModClient implements ClientModInitializer {

    public static CompassConfigGui configScreen;

    @Override
    public void onInitializeClient() {

        KeybindHandler.registerKeybind(
                "key.compass.config_gui", GLFW.GLFW_KEY_H);

        ClientTickEvents.END_CLIENT_TICK.register(client -> { KeybindHandler.resolveKeybinds(); });

        ConfigManager.getInstance().registerConfigHandler(CompassMod.MOD_ID, new CompassConfig());

        CompassMod.LOGGER.info("Client initialized!");
    }
}
