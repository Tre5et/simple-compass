package net.treset.compass.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.treset.compass.CompassClient;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() { //set config screen as modmenu options
        return screen -> CompassClient.CONFIG_SCREEN;
    }
}
