package net.treset.compass;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompassMod implements ModInitializer {

	public static final String MOD_ID = "compass";
	public static final String CONFIG_FILE_NAME = MOD_ID + ".json";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Mod initialized!");
	}
}
