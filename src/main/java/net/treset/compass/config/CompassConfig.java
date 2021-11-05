package net.treset.compass.config;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import net.treset.compass.config.waypoints.CompassConfigWaypoint0;
import net.treset.compass.config.waypoints.CompassConfigWaypoint1;
import net.treset.compass.config.waypoints.CompassConfigWaypoint2;
import net.treset.compass.config.waypoints.CompassConfigWaypoint3;

public class CompassConfig implements ConfigGroup {
    @ConfigEntry
    public static float compassScale = 1;
    @ConfigEntry
    public static boolean enabled = true;

    @Transitive
    CompassConfigWaypoint0 waypoint0 = new CompassConfigWaypoint0();
    @Transitive
    CompassConfigWaypoint1 waypoint1 = new CompassConfigWaypoint1();
    @Transitive
    CompassConfigWaypoint2 waypoint2 = new CompassConfigWaypoint2();
    @Transitive
    CompassConfigWaypoint3 waypoint3 = new CompassConfigWaypoint3();
}
