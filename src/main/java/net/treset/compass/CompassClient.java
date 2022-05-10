package net.treset.compass;

import net.fabricmc.api.ClientModInitializer;
import net.treset.compass.config.Config;
import net.treset.compass.config.gui.ConfigGui;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class CompassClient implements ClientModInitializer {

    public static ConfigGui configScreen;
    public static ConfigScreen CONFIG_SCREEN;

    @Override
    public void onInitializeClient() {

        /*KeybindTools.registerKeybind( //setup keybinds
                "key.compass.config_gui", GLFW.GLFW_KEY_H);
        ClientTickEvents.END_CLIENT_TICK.register(client -> { KeybindTools.resolveKeybinds(); }); //resolve keybinds

        ConfigManager.getInstance().registerConfigHandler(CompassMod.MOD_ID, new Config_o()); //register config

        if(!FileTools.loadOrCreateConfigDir()) return; //setup config directory
        WorldSpecificWaypoints.resetGlobalWaypoints(); //set displayed waypoints to default*/

        Config.init();

        CompassMod.LOGGER.info("Client initialized!");
    }
}
