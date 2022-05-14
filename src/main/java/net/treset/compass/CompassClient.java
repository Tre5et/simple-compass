package net.treset.compass;

import net.fabricmc.api.ClientModInitializer;
import net.treset.compass.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class CompassClient implements ClientModInitializer {

    public static ConfigScreen CONFIG_SCREEN;

    @Override
    public void onInitializeClient() {
        Config.init();

        CompassMod.LOGGER.info("Client initialized!");
    }
}
