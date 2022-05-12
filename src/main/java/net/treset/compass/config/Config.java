package net.treset.compass.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.treset.compass.CompassClient;
import net.treset.compass.tools.KeybindTools;
import net.treset.compass.tools.WaypointTools;
import net.treset.vanillaconfig.config.*;
import net.treset.vanillaconfig.config.base.BaseConfig;
import net.treset.vanillaconfig.config.base.SlideableConfig;
import net.treset.vanillaconfig.config.managers.SaveLoadManager;
import net.treset.vanillaconfig.config.version.ConfigVersion;
import net.treset.vanillaconfig.screen.ConfigScreen;
import net.treset.vanillaconfig.tools.FileTools;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final PageConfig MAIN_PAGE = new PageConfig("config.compass.main.page");

    public static final ListConfig DIR_DISPLAY_MODE = new ListConfig(ConfigLists.displayMode, 0, "config.compass.dir_display_mode.list", ConfigLists.dirDisplayModeComments);
    public static final ListConfig WP_DISPLAY_MODE = new ListConfig(ConfigLists.displayMode, 0, "config.compass.wp_display_mode.list", ConfigLists.wpDisplayModeComments);
    public static final BooleanConfig MINIMALIST_MODE = new BooleanConfig(false, "config.compass.minimalist.toggle", "config.compass.minimalist.toggle.comment");
    public static final DoubleConfig COMPASS_SCALE = new DoubleConfig(2, 1, 15, "config.compass.scale.double", "config.compass.scale.double.comment");
    public static final DoubleConfig DIR_SCALE = new DoubleConfig(1, 0.1, 10, "config.compass.dir_scale.double", "config.compass.dir_scale.double.comment");
    public static final DoubleConfig WP_SCALE = new DoubleConfig(1, 0.1, 10, "config.compass.wp_scale.double", "config.compass.wp_scale.double.comment");

    public static final PageConfig WAYPOINTS_PAGE = new PageConfig("config.compass.waypoints.page");

    public static final KeybindConfig OPEN_CONFIG = new KeybindConfig(new int[]{0x23} /*H*/, 0, 5, "config.compass.open_config.keybind", "config.compass.open_config.keybind.comment");

    public static final BooleanConfig WP_A_SHOW = new BooleanConfig(false, "config.compass.waypoints.a.toggle", "config.compass.waypoints.a.toggle.comment");
    public static final IntegerConfig WP_A_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.a.x", "config.compass.waypoints.a.x.comment");
    public static final IntegerConfig WP_A_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.a.z", "config.compass.waypoints.a.z.comment");
    public static final ButtonConfig WP_A_SET_TO_PLAYER = new ButtonConfig("config.compass.waypoints.a.to_player", "config.compass.waypoints.a.to_player.comment");
    public static final BooleanConfig WP_B_SHOW = new BooleanConfig(false, "config.compass.waypoints.b.toggle", "config.compass.waypoints.b.toggle.comment");
    public static final IntegerConfig WP_B_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.b.x", "config.compass.waypoints.b.x.comment");
    public static final IntegerConfig WP_B_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.b.z", "config.compass.waypoints.b.z.comment");
    public static final ButtonConfig WP_B_SET_TO_PLAYER = new ButtonConfig("config.compass.waypoints.b.to_player", "config.compass.waypoints.b.to_player.comment");
    public static final BooleanConfig WP_C_SHOW = new BooleanConfig(false, "config.compass.waypoints.c.toggle", "config.compass.waypoints.c.toggle.comment");
    public static final IntegerConfig WP_C_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.c.x", "config.compass.waypoints.c.x.comment");
    public static final IntegerConfig WP_C_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.c.z", "config.compass.waypoints.c.z.comment");
    public static final ButtonConfig WP_C_SET_TO_PLAYER = new ButtonConfig( "config.compass.waypoints.c.to_player", "config.compass.waypoints.c.to_player.comment");
    public static final BooleanConfig WP_D_SHOW = new BooleanConfig(false, "config.compass.waypoints.d.toggle", "config.compass.waypoints.d.toggle.comment");
    public static final IntegerConfig WP_D_X = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.d.x", "config.compass.waypoints.d.x.comment");
    public static final IntegerConfig WP_D_Z = new IntegerConfig(0, -1000000000, 100000000, "config.compass.waypoints.d.z", "config.compass.waypoints.d.z.comment");
    public static final ButtonConfig WP_D_SET_TO_PLAYER = new ButtonConfig("config.compass.waypoints.d.to_player", "config.compass.waypoints.d.to_player.comment");

    public static class Lists {
        public static final BaseConfig[] MAIN_PAGE_CONFIGS = new BaseConfig[] {
                DIR_DISPLAY_MODE,
                WP_DISPLAY_MODE,
                MINIMALIST_MODE,
                COMPASS_SCALE,
                DIR_SCALE,
                WP_SCALE,
                WAYPOINTS_PAGE,
                OPEN_CONFIG
        };

        public static final BaseConfig[] WAYPOINTS_PAGE_CONFIGS = new BaseConfig[] {
                WP_A_SHOW,
                WP_A_X,
                WP_A_Z,
                WP_A_SET_TO_PLAYER,
                WP_B_SHOW,
                WP_B_X,
                WP_B_Z,
                WP_B_SET_TO_PLAYER,
                WP_C_SHOW,
                WP_C_X,
                WP_C_Z,
                WP_C_SET_TO_PLAYER,
                WP_D_SHOW,
                WP_D_X,
                WP_D_Z,
                WP_D_SET_TO_PLAYER
        };

         public static final SlideableConfig[] SLIDER_CONFIGS = new SlideableConfig[] {
                 COMPASS_SCALE,
                 DIR_SCALE,
                 WP_SCALE
         };

         public static final BaseConfig[] WP_SUB_OPTIONS = new BaseConfig[] {
                 WP_A_X,
                 WP_A_Z,
                 WP_A_SET_TO_PLAYER,
                 WP_B_X,
                 WP_B_Z,
                 WP_B_SET_TO_PLAYER,
                 WP_C_X,
                 WP_C_Z,
                 WP_C_SET_TO_PLAYER,
                 WP_D_X,
                 WP_D_Z,
                 WP_D_SET_TO_PLAYER
         };

         public static final BooleanConfig[] WP_SHOW_OPTIONS = new BooleanConfig[] {
                 WP_A_SHOW,
                 WP_B_SHOW,
                 WP_C_SHOW,
                 WP_D_SHOW
         };

         public static final IntegerConfig[] WP_COORD_OPTIONS = new IntegerConfig[] {
                 WP_A_X, WP_A_Z,
                 WP_B_X, WP_B_Z,
                 WP_C_X, WP_C_Z,
                 WP_D_X, WP_D_Z
         };

         public static final ButtonConfig[] WP_PLAYER_OPTIONS = new ButtonConfig[] {
                 WP_A_SET_TO_PLAYER,
                 WP_B_SET_TO_PLAYER,
                 WP_C_SET_TO_PLAYER,
                 WP_D_SET_TO_PLAYER
         };
    }

    public static void init() {
        MAIN_PAGE.setOptions(Lists.MAIN_PAGE_CONFIGS);
        MAIN_PAGE.setSaveName("compass");
        MAIN_PAGE.setPath("compass");

        WAYPOINTS_PAGE.setOptions(Lists.WAYPOINTS_PAGE_CONFIGS);
        WAYPOINTS_PAGE.setSaveName("waypoints");
        WAYPOINTS_PAGE.setPath("compass");

        for(SlideableConfig e : Lists.SLIDER_CONFIGS) {
            e.setSlider(true);
        }

        for(BaseConfig e : Lists.WP_SUB_OPTIONS) {
            e.setDisplayed(false);
            e.setCustomWidth(290, 140);
        }

        for(BooleanConfig e : Lists.WP_SHOW_OPTIONS) {
            e.onChange(WaypointTools::onChangeWaypointActive);
        }

        for(IntegerConfig e : Lists.WP_COORD_OPTIONS) {
            e.setFullWidth(false);
            e.onChange(WaypointTools::onChangeWaypoint);
        }

        for(ButtonConfig e : Lists.WP_PLAYER_OPTIONS) {
            e.onClickL(WaypointTools::onSetWaypointToPlayer);
            e.onClickR(WaypointTools::onSetWaypointToPlayer);
        }

        OPEN_CONFIG.onPressed(KeybindTools::openConfig);

        MAIN_PAGE.loadVersion();
        if(!MAIN_PAGE.hasVersion()) {
            migrateFromMalilib();
        }
        checkMigrateWorldConfig();
        MAIN_PAGE.setVersion(new ConfigVersion("1.0.0"));

        SaveLoadManager.globalSaveConfig(MAIN_PAGE);
        SaveLoadManager.worldSaveConfig(WAYPOINTS_PAGE);

        CompassClient.CONFIG_SCREEN = new ConfigScreen(MAIN_PAGE, MinecraftClient.getInstance().currentScreen);
    }

    public static void migrateFromMalilib() {
        if(!FileTools.fileExists(new File("./config/compass.json"))) return;

        JsonObject obj;
        if((obj = FileTools.readJsonFile(new File("./config/compass.json"))) == null || !obj.isJsonObject()) return;

        JsonObject genPage;
        if((genPage = obj.getAsJsonObject("General")) == null || !genPage.isJsonObject()) return;

        JsonPrimitive dirDisplayMode;
        if((dirDisplayMode = genPage.getAsJsonPrimitive("Direction Display Mode")) == null || !dirDisplayMode.isJsonPrimitive() || !dirDisplayMode.isString()) return;
        String newString;
        if((newString = getNewDisplayMode(dirDisplayMode.getAsString())) == null) return;
        DIR_DISPLAY_MODE.setOption(newString);

        JsonPrimitive wpDisplayMode;
        if((wpDisplayMode = genPage.getAsJsonPrimitive("Waypoint Display Mode")) == null || !wpDisplayMode.isJsonPrimitive() || !wpDisplayMode.isString()) return;
        if((newString = getNewDisplayMode(wpDisplayMode.getAsString())) == null) return;
        WP_DISPLAY_MODE.setOption(newString);

        MINIMALIST_MODE.migrateFrom("General/Minimalist Mode");
        COMPASS_SCALE.migrateFrom("General/Compass Scale");
        DIR_SCALE.migrateFrom("General/Direction Size");
        WP_SCALE.migrateFrom("General/Waypoint Size");

        MAIN_PAGE.migrateFileFrom("compass.json");

        loadOpenHotkey();


        WAYPOINTS_PAGE.migrateFileFrom("compass.json");
    }

    public static String getNewDisplayMode(String oldString) {
        switch (oldString) {
            case "always" -> { return "config.compass.display_mode.list.always"; }
            case "when_holding_compass" -> { return "config.compass.display_mode.list.compass"; }
            case "never" -> { return "config.compass.display_mode.list.never"; }
            default -> { return null; }
        }
    }

    public static void loadOpenHotkey() {
        if(!GLFW.glfwInit()) return;

        File optionsFile = new File("./options.txt"); //migrate keybinding
        if(optionsFile.exists() && optionsFile.isFile() && optionsFile.canRead()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(optionsFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.startsWith("key_key.compass.config_gui:")) {
                        String keyName;
                        try {
                            keyName = line.substring(27);
                        } catch (IndexOutOfBoundsException e) {
                            return;
                        }

                        if(keyName.isEmpty()) return;
                        InputUtil.Key key;
                        try {
                            key = InputUtil.fromTranslationKey(keyName);
                        } catch(IllegalArgumentException e) {
                            return;
                        }
                        if(key == null) return;

                        int keyCode = key.getCode();
                        int scanCode = -1;
                        try {
                            scanCode = GLFW.glfwGetKeyScancode(keyCode);
                        } catch (IllegalStateException e) {
                            return;
                        }

                        if(scanCode <= 0) return;

                        OPEN_CONFIG.setKeys(new int[]{scanCode});

                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkMigrateWorldConfig() {
        File dir = new File("./config/compass");
        if(!dir.isDirectory()) return;
        List<String> list = new ArrayList<>(List.of(dir.list()));
        list.remove("waypoints");
        list.remove("waypoints.json");
        list.remove("compass.json");
        if(list.size() == 0) return;

        for(BooleanConfig e : Lists.WP_SHOW_OPTIONS) {
            e.migrateFrom("/Show Waypoint " + e.getKey().split("\\.")[3].toUpperCase()
                    + (e.getKey().split("\\.")[3].equals("a")? "              " : ""));
        }
        for(IntegerConfig e : Lists.WP_COORD_OPTIONS) {
            e.migrateFrom("/ -  Waypoint " + e.getKey().split("\\.")[3].toUpperCase() + " " + e.getKey().split("\\.")[4].toUpperCase() + "-Coordinate");
        }
        WAYPOINTS_PAGE.migrateFileFrom("compass.json");
    }

}
