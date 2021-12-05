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
        if(MinecraftClient.getInstance().currentScreen != CompassModClient.configScreen) return;

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
        if(MinecraftClient.getInstance().currentScreen != CompassModClient.configScreen) return;
        MinecraftClient cli = MinecraftClient.getInstance();

        ConfigBoolean[] wpToPlayer = CompassConfig.Waypoints.SET_PLAYER_OPTIONS;
        ConfigInteger[] wpCoords = CompassConfig.Waypoints.COORDS;

        for(int i = 0; i <= 3; i++){
            if(wpToPlayer[i].getBooleanValue()) {
                if(cli.player != null) {
                    double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();
                    double[] pos = PlayerHelper.getPos();
                    wpCoords[i*2].setIntegerValue((Math.round((float)(pos[0] * coordScale))));
                    wpCoords[i*2+1].setIntegerValue((Math.round((float)(pos[1] * coordScale))));
                }
                wpToPlayer[i].setBooleanValue(false);
                CompassModClient.configScreen.reloadEntries();
            }
        }
    }
}
