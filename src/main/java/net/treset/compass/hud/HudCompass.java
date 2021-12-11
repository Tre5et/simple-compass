package net.treset.compass.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.config.Config;
import net.treset.compass.tools.PlayerTools;

import java.util.Arrays;

public class HudCompass {

    private static final Identifier SPRITESHEET = new Identifier(CompassMod.MOD_ID, "textures/gui/compass.png");

    private static final MinecraftClient cli = MinecraftClient.getInstance();
    private static Entity cam;

    private static float prevYaw = 0;
    private static double[] prevPos = new double[] {0, 0};
    private static int[] prevImgPos = new int[] {
            0, 0, 0, 0,
            0, 0, 0, 0
    };

    private static int prevWindowWidth = 0;

    public static boolean forceUpdateNextFrame = false;

    public static void drawCompass(MatrixStack matrices) {

        cam = cli.getCameraEntity(); //set camera / cancel if no camera is present

        if(!Config.General.ENABLED.getBooleanValue() || cam == null) return; //check if compass is enabled

        int tileSize = 32;
        int tWidth = 128;
        int tHeight = 128;

        int yOffset = (Config.General.MINIMALIST_MODE.getBooleanValue()) ? tileSize * 2 : 0; //handle minimalist mode
        float dirScale =  (float) Config.General.DIR_SCALE.getDoubleValue(); //handle direction scale
        float wpScale = (float) Config.General.WP_SCALE.getDoubleValue(); //handle waypoint scale

        ConfigBoolean[] wpShow = Config.Waypoints.SHOW_OPTIONS;
        ConfigInteger[] wpCoords = Config.Waypoints.COORDS;

        RenderSystem.setShaderTexture(0, SPRITESHEET); //set spritesheet as texture to draw

        int tWidthDir = (int)(tWidth * dirScale);
        int tHeightDir = (int)(tHeight * dirScale);
        int tWidthWp = (int)(tWidth * wpScale);
        int tHeightWp = (int)(tHeight * wpScale);

        int sizeDir = (int)(tileSize * dirScale);
        int sizeWp = (int)(tileSize * wpScale);
        int vDir = (int)(yOffset * dirScale);
        int vWp = (int)((yOffset + tileSize) * wpScale);

        int[] imgPos = new int[8];
        boolean sameYaw = prevYaw == getYaw();
        boolean samePos = Arrays.equals(PlayerTools.getPos(), prevPos);

        int windowWidth = cli.getWindow().getScaledWidth(); //check if window was resized
        if(windowWidth != prevWindowWidth) {
            forceUpdateNextFrame = true;
            prevWindowWidth = windowWidth;
        }

        if(!sameYaw || forceUpdateNextFrame) { //update everything
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                imgPos[i] = (int)getPositionByAngle(sizeDir, (float)i / 4); //override direction values
                if(wpShow[i].getBooleanValue()) imgPos[i + 4] = (int)getPositionByAngle(sizeWp, getAngleOfPoint(wpCoords[i*2].getIntegerValue(), wpCoords[i * 2 + 1].getIntegerValue())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
            prevYaw = getYaw();

            forceUpdateNextFrame = false;
        } else if(!samePos) { //update only waypoints
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                if(wpShow[i].getBooleanValue()) imgPos[i + 4] = (int)getPositionByAngle(sizeWp, getAngleOfPoint(wpCoords[i*2].getIntegerValue(), wpCoords[i * 2 + 1].getIntegerValue())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
        } else { //update nothing
            imgPos = prevImgPos; //copy all previous values
        }

        for(int i = 0; i < 4; i ++) { //two loops so waypoints render over directions
            DrawableHelper.drawTexture(matrices, imgPos[i], 5, i * sizeDir, vDir, sizeDir, sizeDir, tWidthDir, tHeightDir); //draw direction sprites
        }
        for(int i = 3; i >= 0; i --) { //render waypoints a last for top layer
            if(wpShow[i].getBooleanValue()) DrawableHelper.drawTexture(matrices, imgPos[i + 4], 5, i * sizeWp, vWp, sizeWp, sizeWp, tWidthWp, tHeightWp); //draw waypoint sprites
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

    public static float getPositionByAngle(int width, float angle) {
        float camYaw = getYaw();

        float imgPos = 0;
        float compassScale = (float) Config.General.COMPASS_SCALE.getDoubleValue();

        float imageOffset = (float)(0.5 * width); //offset to account for image with
        float compassOffset = (float)(0.5 * (compassScale - 1)); //offset to account for compass scale

        float windowX = cli.getWindow().getScaledWidth();

        camYaw = 1 - camYaw; //invert to line up with rotation direction
        camYaw -= angle; //rotate position to account for angle
        if(camYaw < 0) camYaw = 1 + camYaw; //loop negative values back to the other side

        imgPos = windowX * ((camYaw) * compassScale - compassOffset) - imageOffset; //calculate position on screen

        return imgPos;
    }

    public static float getAngleOfPoint(float targetX, float targetZ) {
        float angle = 0;

        double[] pos = PlayerTools.getPos();

        assert cli.player != null;
        double coordScale = cli.player.clientWorld.getDimension().getCoordinateScale();

        double posX = pos[0] * coordScale;
        double posZ = pos[1] * coordScale;

        targetX += 0.5;
        targetZ += 0.5;

        double distX = Math.abs(targetX - posX); //correct for center of block
        double distZ = Math.abs(targetZ - posZ);

        double relAngle = Math.atan2(distX, distZ) / (0.5 * Math.PI) / 4; //angle in one quadrant

        if (targetX < posX && targetZ < posZ) angle = (float)(relAngle + 0); //quadrant N - W
        else if (targetX < posX && targetZ > posZ) angle = (float)(0.25 - relAngle + 0.25f); //quadrant W - S
        else if (targetX > posX && targetZ > posZ) angle = (float)(relAngle + 0.5f); //quadrant S - E
        else if (targetX > posX && targetZ < posZ) angle = (float)(0.25 - relAngle + 0.75f); //quadrant E - N

        return angle;
    }
}
