package net.treset.compass.tools;

import fi.dy.masa.malilib.config.IConfigBase;
import net.minecraft.client.MinecraftClient;
import net.treset.compass.CompassClient;
import net.treset.compass.config.Config;
import net.treset.compass.config.Config_o;
import net.treset.compass.hud.HudCompass;
import net.treset.vanillaconfig.config.BooleanConfig;
import net.treset.vanillaconfig.config.IntegerConfig;

import java.util.Arrays;
import java.util.List;

public class WaypointTools {

    private static final boolean[] prevWpShow = new boolean[] {
            false, false, false, false
    };

    public static void setWaypointsOptions() {
        HudCompass.forceUpdateNextFrame = true; //all settings apply instantly

        BooleanConfig[] wpShow = Config.Lists.WP_SHOW_OPTIONS;

        for(int i = 0; i <= 3; i ++) {
            if(wpShow[i].getBoolean() != prevWpShow[i]) { //waypoint option has changed
                if(prevWpShow[i]) removeWaypointsOption(i); //waypoint toggled on
                else addWaypointsOption(i); //waypoint toggled off
                prevWpShow[i] = wpShow[i].getBoolean();
            }
        }
    }

    public static void addWaypointsOption(int waypointIndex) {
        IConfigBase[] allOpt = Config_o.Waypoints.ALL_OPTIONS.toArray(new IConfigBase[Config_o.Waypoints.ALL_OPTIONS.size()]);

        List<IConfigBase> relevantOpt = Arrays.asList(
                allOpt[waypointIndex*4+1], allOpt[waypointIndex*4+2], allOpt[waypointIndex*4+3] //select options to add
        );

        Config_o.Waypoints.OPTIONS.addAll(Config_o.Waypoints.OPTIONS.indexOf(allOpt[waypointIndex*4]) + 1, relevantOpt); //add options at correct position

        if(CompassClient.configScreen != null) CompassClient.configScreen.reloadEntries(); //refresh to display new options
    }

    public static void removeWaypointsOption(int waypointIndex) {
        IConfigBase[] allOpt = Config_o.Waypoints.ALL_OPTIONS.toArray(new IConfigBase[Config_o.Waypoints.ALL_OPTIONS.size()]);

        List<IConfigBase> relevantOpt = Arrays.asList(
                allOpt[waypointIndex*4+1], allOpt[waypointIndex*4+2], allOpt[waypointIndex*4+3] //select options to remove
        );

        Config_o.Waypoints.OPTIONS.removeAll(relevantOpt); //remove options by finding them

        if(CompassClient.configScreen != null) CompassClient.configScreen.reloadEntries(); //refresh to hide hidden options
    }

    public static void setWaypointsToPlayer_o() {
        MinecraftClient cli = MinecraftClient.getInstance();

        BooleanConfig[] wpToPlayer = Config.Lists.WP_PLAYER_OPTIONS;
        IntegerConfig[] wpCoords = Config.Lists.WP_COORD_OPTIONS;

        for(int i = 0; i <= 3; i++){
            if(wpToPlayer[i].getBoolean()) { //is waypoint supposed to be set to player?
                if(cli.player != null) {
                    double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();
                    double[] pos = PlayerTools.getPos();
                    wpCoords[i*2].setInteger((int)Math.floor(pos[0] * coordScale)); //set x-value rounded down to match ingame logic
                    wpCoords[i*2+1].setInteger((int)Math.floor(pos[1] * coordScale)); //set y-value rounded down to match ingame logic
                }
                wpToPlayer[i].setBoolean(false); //reset set to player option (so that it always appears false)
                CompassClient.configScreen.reloadEntries(); //refresh to display new coordinates
            }
        }
    }

    public static void onChangeWaypointActive(boolean prevBoolean, String name) {

    }

    public static void onSetWaypoitToPlayer(boolean prevBoolean, String name) {
        if(!prevBoolean) {
            MinecraftClient cli = MinecraftClient.getInstance();
            if(cli.player != null) {
                double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();
                double[] pos = PlayerTools.getPos();


            }
        }
    }
}
