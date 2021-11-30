package net.treset.compass.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.config.CompassConfig;
import net.treset.compass.helpers.PlayerHelper;

import java.util.Objects;

public class CompassGui {

    private static final Identifier SPRITESHEET = new Identifier(CompassMod.MOD_ID, "textures/gui/compass.png");

    private static final MinecraftClient cli = MinecraftClient.getInstance();
    private static Entity cam;

    public static void drawCompass(MatrixStack matrices) {

        cam = Objects.requireNonNull(cli.getCameraEntity()); //set camera / cancel if no camera is present

        if(!CompassConfig.General.ENABLED.getBooleanValue()) return; //check if compass is enabled
        int yOffset;
        if(CompassConfig.General.MINIMALIST_MODE.getBooleanValue()) yOffset = 66; else yOffset = 0; //handle minimalist mode

        ConfigBoolean[] wpShow = CompassConfig.Waypoints.SHOW_OPTIONS;
        ConfigInteger[] wpCoords = CompassConfig.Waypoints.COORDS;

        RenderSystem.setShaderTexture(0, SPRITESHEET); //set spritesheet as texture to draw
        for(int i = 0; i <= 3; i ++) {
            DrawableHelper.drawTexture(matrices, (int) drawImage(32, (float)i / 4), 5, i * 33, 0 + yOffset, 32, 32, 131, 131); //draw N-W-S-E sprites
            if(wpShow[i].getBooleanValue()) DrawableHelper.drawTexture(matrices, (int) drawImage(32, getAngleOfPoint(wpCoords[i*2].getIntegerValue(), wpCoords[i * 2 + 1].getIntegerValue())), 5, i * 33, 33 + yOffset, 32, 32, 131, 131); //draw A-B-C-D sprites
        }
    }

    public static float getYaw() {
        float yaw;

        float headYaw = cam.getYaw();
        headYaw %= 360; //rotation {0..360}
        if(headYaw < 0) headYaw += 360; //correct for negative values

        yaw = headYaw / 360; //rotation {0..1}

        return yaw;
    }

    public static float drawImage(int width, float angle) {
        float imagePos = 0;
        float compassScale = (float)CompassConfig.General.COMPASS_SCALE.getDoubleValue();

        float imageOffset = (float)(0.5 * width); //offset to account for image with
        float compassOffset = (float)(0.5 * (compassScale - 1)); //offset to account for compass scale

        float windowX = cli.getWindow().getScaledWidth();

        float cameraRotation = getYaw();
        cameraRotation = 1 - cameraRotation; //invert to line up with rotation direction
        cameraRotation -= angle; //rotate position to account for angle
        if(cameraRotation < 0) cameraRotation = 1 + cameraRotation; //loop negative values back to the other side

        imagePos = windowX * ((cameraRotation) * compassScale - compassOffset) - imageOffset; //calculate position on screen

        return imagePos;
    }

    public static float getAngleOfPoint(float targetX, float targetZ) {
        float angle = 0;

        double[] pos = PlayerHelper.getPos();

        assert cli.player != null;
        double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();

        double posX = pos[0] / coordScale;
        double posZ = pos[1] / coordScale;

        double distX = Math.abs(targetX - posX);
        double distZ = Math.abs(targetZ - posZ);

        double relAngle = Math.atan2(distX, distZ) / (0.5 * Math.PI) / 4; //angle in one quadrant

        if (targetX < posX && targetZ < posZ) angle = (float)(relAngle + 0); //quadrant N - W
        else if (targetX < posX && targetZ > posZ) angle = (float)(0.25 - relAngle + 0.25f); //quadrant W - S
        else if (targetX > posX && targetZ > posZ) angle = (float)(relAngle + 0.5f); //quadrant S - E
        else if (targetX > posX && targetZ < posZ) angle = (float)(0.25 - relAngle + 0.75f); //quadrant E - N

        return angle;
    }
}
