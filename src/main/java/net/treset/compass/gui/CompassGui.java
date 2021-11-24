package net.treset.compass.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.config.CompassConfig;
import net.treset.compass.config.waypoints.CompassConfigWaypoint0;
import net.treset.compass.config.waypoints.CompassConfigWaypoint1;
import net.treset.compass.config.waypoints.CompassConfigWaypoint2;
import net.treset.compass.config.waypoints.CompassConfigWaypoint3;

import java.util.Objects;

public class CompassGui {

    private static final Identifier SPRITESHEET = new Identifier(CompassMod.MOD_ID, "textures/gui/compass.png");

    private static final MinecraftClient cli = MinecraftClient.getInstance();
    private static Entity cam;

    public static void drawCompass(MatrixStack matrices) {
        cam = Objects.requireNonNull(cli.getCameraEntity());

        if(!CompassConfig.enabled) return;
        int directionSpriteHeight = 32;
        if(!CompassConfig.showArrows) directionSpriteHeight = 15;

        setWaypointsToPlayer();

        RenderSystem.setShaderTexture(0, SPRITESHEET); //set spritesheet as texture to draw
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0), 5, 0, 0, 32, directionSpriteHeight, 131, 65); //draw N-W-S-E sprites
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.25f), 5, 33, 0, 32, directionSpriteHeight, 131, 65);
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.5f), 5, 0, 33, 32, directionSpriteHeight, 131, 65);
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.75f), 5, 33, 33, 32, directionSpriteHeight, 131, 65);
        if(CompassConfigWaypoint0.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint0.waypointX,CompassConfigWaypoint0.waypointZ)), 5, 66, 0, 32, 32, 131, 65); //draw A-B-C-D sprites
        if(CompassConfigWaypoint1.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint1.waypointX,CompassConfigWaypoint1.waypointZ)), 5, 99, 0, 32, 32, 131, 65);
        if(CompassConfigWaypoint2.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint2.waypointX,CompassConfigWaypoint2.waypointZ)), 5, 66, 33, 32, 32, 131, 65);
        if(CompassConfigWaypoint3.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint3.waypointX,CompassConfigWaypoint3.waypointZ)), 5, 99, 33, 32, 32, 131, 65);
    }

    public static float getYaw() {
        float yaw;

        float headYaw = cam.getYaw();
        headYaw %= 360; //rotation {0..360}
        if(headYaw < 0) headYaw += 360; //correct for negative values

        yaw = headYaw / 360; //rotation {0..1}

        return yaw;
    }

    public static double[] getPos() {
        double[] pos = new double[2];

        pos[0] = cam.getX();
        pos[1] = cam.getZ();

        return pos;
    }

    public static float positionImage(int width, float angle) {
        float imagePos = 0;
        float compassScale = CompassConfig.compassScale;

        float imageOffset = (float)(0.5 * width); //offset to account for image with
        float compassOffset = (float)(0.5 * (compassScale - 1)); //offset to account for compass scale

        float windowX = cli.getWindow().getScaledWidth();

        float cameraRotation = getYaw();
        cameraRotation = 1 - cameraRotation; //invert to line up with rotation direction
        cameraRotation -= angle; //rotate position to account for angle
        if(cameraRotation < 0) cameraRotation = 1 + cameraRotation; //loop negative values back to the other side

        imagePos = windowX * ((cameraRotation) * compassScale - compassOffset) - imageOffset; //calculate position on screen

        //CompassMod.LOGGER.info(imagePos);

        return imagePos;
    }

    public static float getAngleOfPoint(float targetX, float targetZ) {
        float angle = 0;

        double[] pos = getPos();

        double posX = pos[0];
        double posZ = pos[1];

        double distX = Math.abs(targetX - posX);
        double distZ = Math.abs(targetZ - posZ);

        double relAngle = Math.atan2(distX, distZ) / (0.5 * Math.PI) / 4; //angle in one quadrant

        if (targetX < posX && targetZ < posZ) angle = (float)(relAngle + 0); //quadrant N - W
        else if (targetX < posX && targetZ > posZ) angle = (float)(0.25 - relAngle + 0.25f); //quadrant W - S
        else if (targetX > posX && targetZ > posZ) angle = (float)(relAngle + 0.5f); //quadrant S - E
        else if (targetX > posX && targetZ < posZ) angle = (float)(0.25 - relAngle + 0.75f); //quadrant E - N

        return angle;
    }

    public static void setWaypointsToPlayer() {
        if(CompassConfigWaypoint0.setToPlayer) {
            CompassConfigWaypoint0.setToPlayer = false;
            double[] pos = getPos();
            CompassConfigWaypoint0.waypointX = (int)pos[0];
            CompassConfigWaypoint0.waypointZ = (int)pos[1];
        }
        if(CompassConfigWaypoint1.setToPlayer) {
            CompassConfigWaypoint1.setToPlayer = false;
            double[] pos = getPos();
            CompassConfigWaypoint1.waypointX = (int)pos[0];
            CompassConfigWaypoint1.waypointZ = (int)pos[1];
        }
        if(CompassConfigWaypoint2.setToPlayer) {
            CompassConfigWaypoint2.setToPlayer = false;
            double[] pos = getPos();
            CompassConfigWaypoint2.waypointX = (int)pos[0];
            CompassConfigWaypoint2.waypointZ = (int)pos[1];
        }
        if(CompassConfigWaypoint3.setToPlayer) {
            CompassConfigWaypoint3.setToPlayer = false;
            double[] pos = getPos();
            CompassConfigWaypoint3.waypointX = (int)pos[0];
            CompassConfigWaypoint3.waypointZ = (int)pos[1];
        }
    }
}
