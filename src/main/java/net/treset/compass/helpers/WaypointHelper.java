package net.treset.compass.helpers;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.minecraft.client.MinecraftClient;
import net.treset.compass.CompassModClient;
import net.treset.compass.config.CompassConfig;

import java.util.Arrays;
import java.util.List;

public class WaypointHelper {

    private static final boolean[] prevWpShow = new boolean[] {
            false, false, false, false
    };

    public static void setWaypointsOptions() {
        ConfigBoolean[] wpShow = CompassConfig.Waypoints.SHOW_OPTIONS;

        for(int i = 0; i <= 3; i ++) {
            if(wpShow[i].getBooleanValue() != prevWpShow[i]) {
                if(prevWpShow[i]) removeWaypointsOption(i);
                else addWaypointsOption(i);
            }
            prevWpShow[i] = wpShow[i].getBooleanValue();
        }
    }

    public static void addWaypointsOption(int waypointIndex) {
        IConfigBase[] allOpt = CompassConfig.Waypoints.ALL_OPTIONS.toArray(new IConfigBase[CompassConfig.Waypoints.ALL_OPTIONS.size()]);

        List<IConfigBase> relevantOpt = Arrays.asList(
                allOpt[waypointIndex*4+1], allOpt[waypointIndex*4+2], allOpt[waypointIndex*4+3]
        );

        CompassConfig.Waypoints.OPTIONS.addAll(CompassConfig.Waypoints.OPTIONS.indexOf(allOpt[waypointIndex*4]) + 1, relevantOpt);

        if(CompassModClient.configScreen != null) CompassModClient.configScreen.reloadEntries();
    }

    public static void removeWaypointsOption(int waypointIndex) {
        IConfigBase[] allOpt = CompassConfig.Waypoints.ALL_OPTIONS.toArray(new IConfigBase[CompassConfig.Waypoints.ALL_OPTIONS.size()]);

        List<IConfigBase> relevantOpt = Arrays.asList(
                allOpt[waypointIndex*4+1], allOpt[waypointIndex*4+2], allOpt[waypointIndex*4+3]
        );

        CompassConfig.Waypoints.OPTIONS.removeAll(relevantOpt);

        if(CompassModClient.configScreen != null) CompassModClient.configScreen.reloadEntries();
    }

    public static void setWaypointsToPlayer() {
        ConfigBoolean[] wpToPlayer = CompassConfig.Waypoints.SET_PLAYER_OPTIONS;
        ConfigInteger[] wpCoords = CompassConfig.Waypoints.COORDS;

        for(int i = 0; i <= 3; i++){
            if(wpToPlayer[i].getBooleanValue()) {
                if(MinecraftClient.getInstance().getCameraEntity() != null) {
                    double[] pos = PlayerHelper.getPos();
                    wpCoords[i*2].setIntegerValue((Math.round((float)pos[0])));
                    wpCoords[i*2+1].setIntegerValue((Math.round((float)pos[1])));
                }
                wpToPlayer[i].setBooleanValue(false);
                CompassModClient.configScreen.reloadEntries();
            }
        }
    }
}
