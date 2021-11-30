package net.treset.compass.helpers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class PlayerHelper {

    public static double[] getPos() {
        double[] pos = new double[2];

        Entity cam = MinecraftClient.getInstance().getCameraEntity();
        assert cam != null;
        pos[0] = cam.getX();
        pos[1] = cam.getZ();

        return pos;
    }
}
