package net.treset.compass;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.treset.compass.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class CompassClient implements ClientModInitializer {

    public static ConfigScreen CONFIG_SCREEN;

    @Override
    public void onInitializeClient() {
        Config.init();

        FabricLoader.getInstance().getModContainer(CompassMod.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(CompassMod.MOD_ID, "minimal"),  modContainer, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(CompassMod.MOD_ID, "dark"),  modContainer, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(CompassMod.MOD_ID, "dark_minimal"),  modContainer, ResourcePackActivationType.NORMAL);
        });
    }
}
