package net.treset.compass.tools;

import net.minecraft.client.MinecraftClient;
import net.treset.compass.config.Config;
import net.treset.compass.hud.HudCompass;
import net.treset.vanillaconfig.config.IntegerConfig;
import net.treset.vanillaconfig.config.base.BaseConfig;
import net.treset.vanillaconfig.config.config_type.ConfigType;

public class WaypointTools {

    private static final boolean[] prevWpShow = new boolean[] {
            false, false, false, false
    };

   public static void onChangeWaypointActive(boolean prevBoolean, String name) {
        BaseConfig[] options = getAllOptions(name);

        for (BaseConfig e : options) {
            if (e != null) {
                e.setDisplayed(!prevBoolean);
            }
        }

       HudCompass.forceUpdateNextFrame = true;
    }

    public static void onSetWaypointToPlayer(String name) {
        MinecraftClient cli = MinecraftClient.getInstance();
        if(cli.player != null) {
            double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();
            double[] pos = PlayerTools.getPos();
            IntegerConfig[] configs = getCoordinateOptions(name);

            try {
                configs[0].setInteger((int) Math.floor(pos[0] * coordScale));
                configs[1].setInteger((int) Math.floor(pos[1] * coordScale));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onChangeWaypoint(int prevInt, String name) {
       HudCompass.forceUpdateNextFrame = true;
    }

    public static BaseConfig[] getWaypointOptions(String name, boolean x, boolean z, boolean setToPlayer) {
        int maxAmount = (x? 1 : 0) + (z? 1 : 0) + (setToPlayer? 1 : 0);

        BaseConfig[] configs = new BaseConfig[maxAmount];

        for (BaseConfig e : Config.Lists.WP_SUB_OPTIONS) {
            if(e.getKey().split("\\.")[3].equals(name.split("\\.")[3])) {
                if(e.getKey().split("\\.")[4].equals("x") && x) {
                    configs[0] = e;
                } else if(e.getKey().split("\\.")[4].equals("z") && z) {
                    configs[x? 1 : 0] = e;
                } else if(e.getKey().split("\\.")[4].equals("to_player") && setToPlayer) {
                    configs[maxAmount - 1] = e;
                }
            }
        }

        return configs;
    }

    public static IntegerConfig[] getCoordinateOptions(String name) {
        BaseConfig[] configs = getWaypointOptions(name, true, true, false);
        if(configs.length != 2) return new IntegerConfig[2];

        IntegerConfig[] integerConfigs = new IntegerConfig[2];
        for (int i = 0; i < 2; i++) {
            if(configs[i].getType() != ConfigType.INTEGER) return new IntegerConfig[]{null, null};
            integerConfigs[i] = (IntegerConfig)configs[i];
        }
        return integerConfigs;
    }

    public static BaseConfig[] getAllOptions(String name) {
        BaseConfig[] configs = getWaypointOptions(name, true, true, true);
        if(configs.length != 3) return new BaseConfig[3];
        return configs;
    }
}
