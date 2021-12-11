package net.treset.compass.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.Locale;

public class PlayerTools {

    public static boolean doesPlayerExist() {
        if(MinecraftClient.getInstance().player != null) return true;
        return false;
    }

    public static double[] getPos() {
        double[] pos = new double[2];

        Entity cam = MinecraftClient.getInstance().getCameraEntity(); //camera position (also accurate when e.g. in freecam)
        assert cam != null;
        pos[0] = cam.getX();
        pos[1] = cam.getZ();

        return pos;
    }

    public static String getWorldId() {
        MinecraftClient cli = MinecraftClient.getInstance();

        if (cli.isIntegratedServerRunning()) { //is player in singleplayer?
            IntegratedServer server = cli.getServer();

            if (server != null) {
                String name = server.getSaveProperties().getLevelName(); //get world name
                return name.toLowerCase(Locale.US).replaceAll("\\W", "_"); //lowercase and replace space with _
            }
        } else {
            if (cli.isConnectedToRealms()) { //is player in realms?
                ClientPlayNetworkHandler handler = cli.getNetworkHandler();
                ClientConnection connection = handler != null ? handler.getConnection() : null;

                if (connection != null) {
                    String str = "realms_" + connection.getAddress().toString(); //get realms connection adress

                    if (str.contains("/")) { //split string after /
                        str = str.substring(str.indexOf('/') + 1);
                    }

                    return str.replace(':', '_'); //replace : with _
                }
            }

            ServerInfo server = cli.getCurrentServerEntry();

            if (server != null) {
                return server.address.replace(':', '_'); //get server adress; replace : with _
            }
        }
        return null;
    }
}
