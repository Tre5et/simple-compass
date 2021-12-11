package net.treset.compass.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.treset.compass.CompassClient;
import net.treset.compass.CompassMod;
import net.treset.compass.config.gui.ConfigGui;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() { //set config screen as modmenu options
        CompassMod.LOGGER.info("set modmenu screen");
        return (screen) -> {
            CompassClient.configScreen = new ConfigGui();
            CompassClient.configScreen.setParent(screen);
            return CompassClient.configScreen;
        };
    }
}
