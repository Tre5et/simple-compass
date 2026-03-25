package net.treset.compass;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.treset.compass.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class CompassClient implements ClientModInitializer {

    public static ConfigScreen getConfigScreen() {
        return new ConfigScreen(Config.MAIN_PAGE, Minecraft.getInstance().screen);
    }

    @Override
    public void onInitializeClient() {
        Config.init();

        FabricLoader.getInstance().getModContainer(CompassMod.MOD_ID).ifPresent(modContainer -> {
            ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "minimal"),  modContainer, PackActivationType.NORMAL);
            ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "dark"),  modContainer, PackActivationType.NORMAL);
            ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "dark_minimal"),  modContainer, PackActivationType.NORMAL);
        });
    }
}
