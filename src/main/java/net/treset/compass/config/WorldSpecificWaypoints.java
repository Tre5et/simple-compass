package net.treset.compass.config;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.treset.compass.CompassMod;
import net.treset.compass.hud.HudCompass;
import net.treset.compass.tools.FileTools;
import net.treset.compass.tools.PlayerTools;

import java.io.File;
import java.util.List;

public class WorldSpecificWaypoints {
    private static boolean prevPlayerExists = false;
    private static String prevWorldId = "";

    public static void readWriteWaypoints() {
        boolean playerExists = PlayerTools.doesPlayerExist();
        if(playerExists != prevPlayerExists) {
            prevPlayerExists = playerExists;
            if(!playerExists) { //player left world
                if(!writeWorldWaypoints(Config_o.Waypoints.ALL_OPTIONS, prevWorldId)) CompassMod.LOGGER.warn("Couldn't save per world waypoints.");
                resetGlobalWaypoints();
            } else { //player joined world
                if(!readWorldWaypoints(Config_o.Waypoints.ALL_OPTIONS, prevWorldId)) CompassMod.LOGGER.warn("Couldn't load per world waypoints.");
                HudCompass.forceUpdateNextFrame = true; //force waypoints to display immediately
            }
        }
        if(PlayerTools.getWorldId() != null) prevWorldId = PlayerTools.getWorldId();
    }

    public static boolean writeWorldWaypoints(List<? extends IConfigBase> options, String worldId) {
        File dir = CompassMod.CONFIG_DIR;

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject obj = new JsonObject();

            for (IConfigBase option : options) { //add all provided options
                obj.add(option.getName(), option.getAsJsonElement());
            }

            if(worldId != null) { //save to [worldName].json
                return(FileTools.writeJsonToFile(obj, new File(dir, worldId + ".json")));
            }
        }
        return false;
    }

    public static boolean readWorldWaypoints(List<? extends IConfigBase> options, String worldId) {
        File dir = CompassMod.CONFIG_DIR;

        JsonObject obj;
        if(worldId != null) {
            if((obj = FileTools.readJsonFile(new File(dir, worldId + ".json"))) != null) { //read from [worldName].json
                for (IConfigBase option : options) { //set provided elements to loaded data
                    if (obj.has(option.getName())) {
                        option.setValueFromJsonElement(obj.get(option.getName()));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void resetGlobalWaypoints() { //set the displayed waypoints to defaults
        Config_o.loadFromFile();

        Config_o.Waypoints.WAYPOINT_A.setBooleanValue(false);
        Config_o.Waypoints.WAYPOINT_B.setBooleanValue(false);
        Config_o.Waypoints.WAYPOINT_C.setBooleanValue(false);
        Config_o.Waypoints.WAYPOINT_D.setBooleanValue(false);

        ConfigInteger[] coords = Config_o.Waypoints.COORDS;
        for (ConfigInteger e : coords) {
            e.setIntegerValue(0);
        }

        Config_o.saveToFile();
    }
}
