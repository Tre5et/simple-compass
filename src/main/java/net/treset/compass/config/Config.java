package net.treset.compass.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.*;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.util.FileUtils;
import net.treset.compass.CompassMod;
import net.treset.compass.config.lists.DisplayMode;
import net.treset.compass.tools.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config implements IConfigHandler {

    public static final int CONFIG_VERSION = 0;

    public static class General { //general options
        public static final ConfigOptionList DIR_DISPLAY_MODE = new ConfigOptionList("Direction Display Mode", DisplayMode.ALWAYS,"When the Directions should be displayed");
        public static final ConfigOptionList WP_DISPLAY_MODE = new ConfigOptionList("Waypoint Display Mode", DisplayMode.ALWAYS, "When the Waypoints should be displayed");
        public static final ConfigBoolean MINIMALIST_MODE = new ConfigBoolean("Minimalist Mode", false, "Hide all the fancy colory bits");
        public static final ConfigDouble COMPASS_SCALE = new ConfigDouble("Compass Scale", 2, 1, 15, "Scale (width) of the Compass");
        public static final ConfigDouble DIR_SCALE = new ConfigDouble("Direction Size", 1, 0.1, 10, "Size of the Direction indicators");
        public static final ConfigDouble WP_SCALE = new ConfigDouble("Waypoint Size", 1, 0.1, 10, "Size of the Waypoint indicators");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                DIR_DISPLAY_MODE,
                WP_DISPLAY_MODE,
                MINIMALIST_MODE,
                COMPASS_SCALE,
                DIR_SCALE,
                WP_SCALE
        );
    }

    public static class Waypoints { //waypoint options
        public static final ConfigBoolean WAYPOINT_A = new ConfigBoolean("Show Waypoint A              ", false, "Show or hide Waypoint A");
        public static final ConfigInteger WAYPOINT_A_X = new ConfigInteger(" -  Waypoint A X-Coordinate", 0, "The X (east / west) value of Waypoint A");
        public static final ConfigInteger WAYPOINT_A_Z = new ConfigInteger(" -  Waypoint A Z-Coordinate", 0, "The Z (north / south) value of Waypoint A");
        public static final ConfigBoolean WAYPOINT_A_SET_TO_PLAYER =new ConfigBoolean(" -  Set Waypoint A to Player", false, "Set Waypoint A to player");
        public static final ConfigBoolean WAYPOINT_B = new ConfigBoolean("Show Waypoint B", false, "Show or hide Waypoint B");
        public static final ConfigInteger WAYPOINT_B_X = new ConfigInteger(" -  Waypoint B X-Coordinate", 0, "The X (east / west) value of Waypoint B");
        public static final ConfigInteger WAYPOINT_B_Z = new ConfigInteger(" -  Waypoint B Z-Coordinate", 0, "The Z (north / south) value of Waypoint B");
        public static final ConfigBoolean WAYPOINT_B_SET_TO_PLAYER =new ConfigBoolean(" -  Set Waypoint B to Player", false, "Set Waypoint B to player");
        public static final ConfigBoolean WAYPOINT_C = new ConfigBoolean("Show Waypoint C", false, "Show or hide Waypoint C");
        public static final ConfigInteger WAYPOINT_C_X = new ConfigInteger(" -  Waypoint C X-Coordinate", 0, "The X (east / west) value of Waypoint C");
        public static final ConfigInteger WAYPOINT_C_Z = new ConfigInteger(" -  Waypoint C Z-Coordinate", 0, "The Z (north / south) value of Waypoint C");
        public static final ConfigBoolean WAYPOINT_C_SET_TO_PLAYER =new ConfigBoolean(" -  Set Waypoint C to Player", false, "Set Waypoint C to player");
        public static final ConfigBoolean WAYPOINT_D = new ConfigBoolean("Show Waypoint D", false, "Show or hide Waypoint D");
        public static final ConfigInteger WAYPOINT_D_X = new ConfigInteger(" -  Waypoint D X-Coordinate", 0, "The X (east / west) value of Waypoint D");
        public static final ConfigInteger WAYPOINT_D_Z = new ConfigInteger(" -  Waypoint D Z-Coordinate", 0, "The Z (north / south) value of Waypoint D");
        public static final ConfigBoolean WAYPOINT_D_SET_TO_PLAYER =new ConfigBoolean(" -  Set Waypoint D to Player", false, "Set Waypoint D to player");

        public static final ConfigBoolean[] SHOW_OPTIONS = new ConfigBoolean[]{ //show waypoint options
            WAYPOINT_A, WAYPOINT_B, WAYPOINT_C, WAYPOINT_D
        };
        public static final ConfigInteger[] COORDS = new ConfigInteger[] { //coordinate options
            WAYPOINT_A_X, WAYPOINT_A_Z,
            WAYPOINT_B_X, WAYPOINT_B_Z,
            WAYPOINT_C_X, WAYPOINT_C_Z,
            WAYPOINT_C_X, WAYPOINT_C_Z
        };
        public static final ConfigBoolean[] SET_PLAYER_OPTIONS = new ConfigBoolean[]{ //set to player options
          WAYPOINT_A_SET_TO_PLAYER, WAYPOINT_B_SET_TO_PLAYER, WAYPOINT_C_SET_TO_PLAYER, WAYPOINT_D_SET_TO_PLAYER
        };

        public static List<IConfigBase> OPTIONS = new ArrayList<>(List.of( //displayed options; are updated
                WAYPOINT_A,
                WAYPOINT_B,
                WAYPOINT_C,
                WAYPOINT_D
        ));

        public static final ImmutableList<IConfigBase> ALL_OPTIONS = ImmutableList.of( //all options
                WAYPOINT_A,
                WAYPOINT_A_X,
                WAYPOINT_A_Z,
                WAYPOINT_A_SET_TO_PLAYER,
                WAYPOINT_B,
                WAYPOINT_B_X,
                WAYPOINT_B_Z,
                WAYPOINT_B_SET_TO_PLAYER,
                WAYPOINT_C,
                WAYPOINT_C_X,
                WAYPOINT_C_Z,
                WAYPOINT_C_SET_TO_PLAYER,
                WAYPOINT_D,
                WAYPOINT_D_X,
                WAYPOINT_D_Z,
                WAYPOINT_D_SET_TO_PLAYER
        );
    }
    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CompassMod.CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) { //does file exist and is readable

            JsonObject obj;
            if ((obj = FileTools.readJsonFile(configFile)) != null) { //read file
                ConfigUtils.readConfigBase(obj, "General", Config.General.OPTIONS); //read category general options
                ConfigUtils.readConfigBase(obj, "Waypoints", Config.Waypoints.ALL_OPTIONS); //read category waypoint options
            }
        }
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) { //does config dir exist or create it

            JsonObject obj = new JsonObject();

            ConfigUtils.writeConfigBase(obj, "General", Config.General.OPTIONS); //add category general options
            ConfigUtils.writeConfigBase(obj, "Waypoints", Config.Waypoints.ALL_OPTIONS); //add category waypoint options
            obj.add("config_version", new JsonPrimitive(CONFIG_VERSION)); //add config file version option

            FileTools.writeJsonToFile(obj, new File(dir, CompassMod.CONFIG_FILE_NAME)); //write file
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
