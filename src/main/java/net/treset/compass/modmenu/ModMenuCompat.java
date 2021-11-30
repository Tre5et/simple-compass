package net.treset.compass.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.treset.compass.CompassMod;
import net.treset.compass.config.gui.CompassConfigGui;

import static net.treset.compass.CompassModClient.configScreen;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        CompassMod.LOGGER.info("set mod screen");
        return (screen) -> {
            configScreen = new CompassConfigGui();
            configScreen.setParent(screen);
            return configScreen;
        };
    }
}
