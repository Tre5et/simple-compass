package net.treset.compass.config;

import net.minecraft.client.MinecraftClient;
import net.treset.compass.CompassClient;
import net.treset.vanillaconfig.config.*;
import net.treset.vanillaconfig.config.base.BaseConfig;
import net.treset.vanillaconfig.config.base.SlideableConfig;
import net.treset.vanillaconfig.config.managers.SaveLoadManager;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class Config {
    public static final PageConfig MAIN_PAGE = new PageConfig("config.compass.main.page");

    public static final ListConfig DIR_DISPLAY_MODE = new ListConfig(ConfigLists.displayMode, 0, "config.compass.dir_display_mode.list", ConfigLists.dirDisplayModeComments);
    public static final ListConfig WP_DISPLAY_MODE = new ListConfig(ConfigLists.displayMode, 0, "config.compass.wp_display_mode.list", ConfigLists.wpDisplayModeComments);
    public static final BooleanConfig MINIMALIST_MODE = new BooleanConfig(false, "config.compass.minimalist.toggle", "config.compass.minimalist.toggle.comment");
    public static final DoubleConfig COMPASS_SCALE = new DoubleConfig(2, 1, 15, "config.compass.scale.double", "config.compass.scale.double.comment");
    public static final DoubleConfig DIR_SCALE = new DoubleConfig(1, 0.1, 10, "config.compass.dir_scale.double", "config.compass.dir_scale.double");
    public static final DoubleConfig WP_SCALE = new DoubleConfig(1, 0.1, 10, "config.compass.wp_scale.double", "config.compass.wp_scale.double");

    public static final PageConfig WAYPOINTS_PAGE = new PageConfig("config.compass.waypoints.page");

    public static final BooleanConfig WAYPOINT_A = new BooleanConfig(false, "config.compass.waypoints.a.toggle", "config.compass.waypoints.a.toggle.comment");
    public static final IntegerConfig WAYPOINT_A_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.a.x", "config.compass.waypoints.a.x.comment");
    public static final IntegerConfig WAYPOINT_A_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.a.z", "config.compass.waypoints.a.z.comment");
    public static final BooleanConfig WAYPOINT_A_SET_TO_PLAYER = new BooleanConfig(false, "config.compass.waypoints.a.to_player", "config.compass.waypoints.a.to_player.comment");
    public static final BooleanConfig WAYPOINT_B = new BooleanConfig(false, "config.compass.waypoints.b.toggle", "config.compass.waypoints.b.toggle.comment");
    public static final IntegerConfig WAYPOINT_B_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.b.x", "config.compass.waypoints.b.x.comment");
    public static final IntegerConfig WAYPOINT_B_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.b.z", "config.compass.waypoints.b.z.comment");
    public static final BooleanConfig WAYPOINT_B_SET_TO_PLAYER = new BooleanConfig(false, "config.compass.waypoints.b.to_player", "config.compass.waypoints.b.to_player.comment");
    public static final BooleanConfig WAYPOINT_C = new BooleanConfig(false, "config.compass.waypoints.c.toggle", "config.compass.waypoints.c.toggle.comment");
    public static final IntegerConfig WAYPOINT_C_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.c.x", "config.compass.waypoints.c.x.comment");
    public static final IntegerConfig WAYPOINT_C_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.c.z", "config.compass.waypoints.c.z.comment");
    public static final BooleanConfig WAYPOINT_C_SET_TO_PLAYER = new BooleanConfig(false, "config.compass.waypoints.c.to_player", "config.compass.waypoints.c.to_player.comment");
    public static final BooleanConfig WAYPOINT_D = new BooleanConfig(false, "config.compass.waypoints.d.toggle", "config.compass.waypoints.d.toggle.comment");
    public static final IntegerConfig WAYPOINT_D_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.d.x", "config.compass.waypoints.d.x.comment");
    public static final IntegerConfig WAYPOINT_D_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.d.z", "config.compass.waypoints.d.z.comment");
    public static final BooleanConfig WAYPOINT_D_SET_TO_PLAYER = new BooleanConfig(false, "config.compass.waypoints.d.to_player", "config.compass.waypoints.d.to_player.comment");

    static class Lists {
        public static final BaseConfig[] MAIN_PAGE_CONFIGS = new BaseConfig[] {
                DIR_DISPLAY_MODE,
                WP_DISPLAY_MODE,
                MINIMALIST_MODE,
                COMPASS_SCALE,
                DIR_SCALE,
                WP_SCALE,
                WAYPOINTS_PAGE
        };

        public static final BaseConfig[] WAYPOINTS_PAGE_CONFIGS = new BaseConfig[] {
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
        };

         public static final SlideableConfig[] SLIDER_CONFIGS = new SlideableConfig[] {
                 COMPASS_SCALE,
                 DIR_SCALE,
                 WP_SCALE
         };
    }

    public static void init() {
        MAIN_PAGE.setOptions(Lists.MAIN_PAGE_CONFIGS);
        MAIN_PAGE.setSaveName("compass");
        MAIN_PAGE.setPath("compass");

        WAYPOINTS_PAGE.setOptions(Lists.WAYPOINTS_PAGE_CONFIGS);
        WAYPOINTS_PAGE.setSaveName("waypoints");
        WAYPOINTS_PAGE.setPath("compass");

        for (SlideableConfig e : Lists.SLIDER_CONFIGS) {
            e.setSlider(true);
        }

        SaveLoadManager.globalSaveConfig(MAIN_PAGE);
        SaveLoadManager.worldSaveConfig(WAYPOINTS_PAGE);

        CompassClient.CONFIG_SCREEN = new ConfigScreen(MAIN_PAGE, MinecraftClient.getInstance().currentScreen);
    }
}
