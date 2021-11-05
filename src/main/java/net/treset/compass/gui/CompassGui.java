package net.treset.compass.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.client.ModMinecraftClient;
import net.treset.compass.config.CompassConfig;
import net.treset.compass.config.waypoints.CompassConfigWaypoint0;
import net.treset.compass.config.waypoints.CompassConfigWaypoint1;
import net.treset.compass.config.waypoints.CompassConfigWaypoint2;
import net.treset.compass.config.waypoints.CompassConfigWaypoint3;

import java.util.Objects;

public class CompassGui {

    private static final Identifier NORTH_TEX = new Identifier(CompassMod.MOD_ID, "textures/gui/compass.png");

    private static final MinecraftClient cli = MinecraftClient.getInstance();
    private static final PlayerEntity player = cli.player;

    private static float prevRotX = 0;
    private static int changedSince = 0;

    public static void drawCompass(MatrixStack matrices) {
        if(!CompassConfig.enabled) return;
        MinecraftClient cli = ModMinecraftClient.getInstance();
        RenderSystem.setShaderTexture(0, NORTH_TEX); //set spritesheet as texture to draw
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0), 5, 0, 0, 32, 32, 131, 65); //draw N-W-S-E sprites
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.25f), 5, 33, 0, 32, 32, 131, 65);
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.5f), 5, 0, 33, 32, 32, 131, 65);
        DrawableHelper.drawTexture(matrices, (int)positionImage(32, 0.75f), 5, 33, 33, 32, 32, 131, 65);
        if(CompassConfigWaypoint0.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint0.waypointX,CompassConfigWaypoint0.waypointZ)), 5, 66, 0, 32, 32, 131, 65); //draw A-B-C-D sprites
        if(CompassConfigWaypoint1.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint1.waypointX,CompassConfigWaypoint1.waypointZ)), 5, 99, 0, 32, 32, 131, 65);
        if(CompassConfigWaypoint2.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint2.waypointX,CompassConfigWaypoint2.waypointZ)), 5, 66, 33, 32, 32, 131, 65);
        if(CompassConfigWaypoint3.displayed) DrawableHelper.drawTexture(matrices, (int)positionImage(32, getAngleOfPoint(CompassConfigWaypoint3.waypointX,CompassConfigWaypoint3.waypointZ)), 5, 99, 33, 32, 32, 131, 65);
    }

    public static float getYaw() {
        float yaw;

        float headYaw = Objects.requireNonNull(cli.getCameraEntity()).getYaw();
        headYaw %= 360; //rotation {0..360}
        if(headYaw < 0) headYaw += 360; //correct for negative values

        yaw = headYaw / 360; //rotation {0..1}

        return yaw;
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

        double cameraPosX = Objects.requireNonNull(cli.getCameraEntity()).lastRenderX;
        double cameraPosZ = Objects.requireNonNull(cli.getCameraEntity()).lastRenderZ;

        double distX = Math.abs(targetX - cameraPosX);
        double distZ = Math.abs(targetZ - cameraPosZ);

        double relAngle = Math.atan2(distX, distZ) / (0.5 * Math.PI) / 4; //angle in one quadrant

        if (targetX < cameraPosX && targetZ < cameraPosZ) angle = (float)(relAngle + 0); //quadrant N - W
        else if (targetX < cameraPosX && targetZ > cameraPosZ) angle = (float)(0.25 - relAngle + 0.25f); //quadrant W - S
        else if (targetX > cameraPosX && targetZ > cameraPosZ) angle = (float)(relAngle + 0.5f); //quadrant S - E
        else if (targetX > cameraPosX && targetZ < cameraPosZ) angle = (float)(0.25 - relAngle + 0.75f); //quadrant E - N

        return angle;
    }
}
