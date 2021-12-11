package net.treset.compass;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.treset.compass.config.Config;
import net.treset.compass.config.WorldSpecificWaypoints;
import net.treset.compass.config.gui.ConfigGui;
import net.treset.compass.tools.FileTools;
import net.treset.compass.tools.KeybindTools;
import org.lwjgl.glfw.GLFW;

public class CompassClient implements ClientModInitializer {

    public static ConfigGui configScreen;

    @Override
    public void onInitializeClient() {

        KeybindTools.registerKeybind( //setup keybinds
                "key.compass.config_gui", GLFW.GLFW_KEY_H);
        ClientTickEvents.END_CLIENT_TICK.register(client -> { KeybindTools.resolveKeybinds(); }); //resolve keybinds

        ConfigManager.getInstance().registerConfigHandler(CompassMod.MOD_ID, new Config()); //register config

        if(!FileTools.loadOrCreateConfigDir()) return; //setup config directory
        WorldSpecificWaypoints.resetGlobalWaypoints(); //set displayed waypoints to default

        CompassMod.LOGGER.info("Client initialized!");
    }
}
