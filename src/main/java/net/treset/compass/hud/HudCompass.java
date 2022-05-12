package net.treset.compass.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.config.Config;
import net.treset.compass.tools.PlayerTools;
import net.treset.vanillaconfig.config.BooleanConfig;
import net.treset.vanillaconfig.config.IntegerConfig;

import java.util.Arrays;

public class HudCompass {

    private static final Identifier SPRITESHEET = new Identifier(CompassMod.MOD_ID, "textures/gui/compass.png");

    private static final MinecraftClient cli = MinecraftClient.getInstance();
    private static Entity cam;

    private static double prevYaw = 0;
    private static double[] prevPos = new double[]{0, 0};
    private static int[] prevImgPos = new int[]{
            0, 0, 0, 0,
            0, 0, 0, 0
    };
    private static int[] imgPos = prevImgPos;

    private static int prevWindowWidth = 0;

    public static boolean forceUpdateNextFrame = false;

    public static void handleCompass(MatrixStack matrices) {

        cam = cli.getCameraEntity(); //set camera / cancel if no camera is present

        if (!shouldDrawDirections() && !shouldDrawWaypoints()) return;

        int tileSize = 32;
        int tWidth = 128;
        int tHeight = 128;

        int windowWidth = cli.getWindow().getScaledWidth(); //check if window was resized
        if (windowWidth != prevWindowWidth) {
            forceUpdateNextFrame = true;
            prevWindowWidth = windowWidth;
        }

        updatePositions(tileSize);

        renderCompass(matrices, tileSize, tWidth, tHeight);
    }

    public static double getYaw() {
        double yaw;

        double headYaw = cam.getYaw();
        headYaw %= 360; //rotation {0..360}
        if (headYaw < 0) headYaw += 360; //correct for negative values

        yaw = headYaw / 360; //rotation {0..1}

        return yaw;
    }

    public static double getPositionByAngle(int width, double angle) {
        double camYaw = getYaw();

        double imgPos = 0;
        double compassScale = Config.COMPASS_SCALE.getDouble();

        double imageOffset = (float) (0.5 * width); //offset to account for image with
        double compassOffset = (float) (0.5 * (compassScale - 1)); //offset to account for compass scale

        double windowX = cli.getWindow().getScaledWidth();

        camYaw = 1 - camYaw; //invert to line up with rotation direction
        camYaw -= angle; //rotate position to account for angle
        if (camYaw < 0) camYaw = 1 + camYaw; //loop negative values back to the other side

        imgPos = windowX * ((camYaw) * compassScale - compassOffset) - imageOffset; //calculate position on screen

        return imgPos;
    }

    public static double getAngleOfPoint(double targetX, double targetZ) {
        double angle = 0;

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

        if (targetX < posX && targetZ < posZ) angle = (relAngle + 0); //quadrant N - W
        else if (targetX < posX && targetZ > posZ) angle = (0.25 - relAngle + 0.25f); //quadrant W - S
        else if (targetX > posX && targetZ > posZ) angle = (relAngle + 0.5f); //quadrant S - E
        else if (targetX > posX && targetZ < posZ) angle = (0.25 - relAngle + 0.75f); //quadrant E - N

        return angle;
    }

    private static boolean shouldDrawDirections() {
        return !Config.DIR_DISPLAY_MODE.getOption().equals("config.compass.display_mode.list.never") &&
                cam != null &&
                !(Config.DIR_DISPLAY_MODE.getOption().equals("config.compass.display_mode.list.compass") && !PlayerTools.isHoldingCompass());
    }

    private static boolean shouldDrawWaypoints() {
        return !Config.WP_DISPLAY_MODE.getOption().equals("config.compass.display_mode.list.never") &&
                cam != null &&
                !(Config.WP_DISPLAY_MODE.getOption().equals("config.compass.display_mode.list.compass") && !PlayerTools.isHoldingCompass());
    }

    private static void updatePositions(int tileSize) {
        float dirScale = (float) Config.DIR_SCALE.getDouble(); //handle direction scale
        float wpScale = (float) Config.WP_SCALE.getDouble(); //handle waypoint scale

        int sizeDir = (int) (tileSize * dirScale);
        int sizeWp = (int) (tileSize * wpScale);

        boolean sameYaw = prevYaw == getYaw();
        boolean samePos = Arrays.equals(PlayerTools.getPos(), prevPos);

        BooleanConfig[] wpShow = Config.Lists.WP_SHOW_OPTIONS;
        IntegerConfig[] wpCoords = Config.Lists.WP_COORD_OPTIONS;

        if((!sameYaw && shouldDrawDirections() && shouldDrawWaypoints()) || forceUpdateNextFrame) { //update everything
            imgPos = prevImgPos; //copy all previous values

            for (int i = 0; i < 4; i++) {
                imgPos[i] = (int) getPositionByAngle(sizeDir, (float) i / 4); //override direction values
                if (wpShow[i].getBoolean())
                    imgPos[i + 4] = (int) getPositionByAngle(sizeWp, getAngleOfPoint(wpCoords[i * 2].getInteger(), wpCoords[i * 2 + 1].getInteger())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
            prevYaw = getYaw();

            forceUpdateNextFrame = false;
        } else if(!sameYaw && shouldDrawDirections()) { //update only directions
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                imgPos[i] = (int) getPositionByAngle(sizeDir, (float) i / 4); //override direction values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
            prevYaw = getYaw();
        } else if((!sameYaw && shouldDrawWaypoints()) || (!samePos && shouldDrawWaypoints())) { //update only waypoints
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                if(wpShow[i].getBoolean()) imgPos[i + 4] = (int)getPositionByAngle(sizeWp, getAngleOfPoint(wpCoords[i*2].getInteger(), wpCoords[i * 2 + 1].getInteger())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
        } else { //update nothing
            imgPos = prevImgPos; //copy all previous values
        }
    }

    private static void renderCompass(MatrixStack matrices, int tileSize, int tWidth, int tHeight) {
        double dirScale = Config.DIR_SCALE.getDouble(); //handle direction scale
        double wpScale = Config.WP_SCALE.getDouble(); //handle waypoint scale
        int yOffset = (Config.MINIMALIST_MODE.getBoolean()) ? tileSize * 2 : 0; //handle minimalist mode

        int tWidthDir = (int) (tWidth * dirScale);
        int tHeightDir = (int) (tHeight * dirScale);
        int tWidthWp = (int) (tWidth * wpScale);
        int tHeightWp = (int) (tHeight * wpScale);

        int sizeDir = (int) (tileSize * dirScale);
        int sizeWp = (int) (tileSize * wpScale);
        int vDir = (int) (yOffset * dirScale);
        int vWp = (int) ((yOffset + tileSize) * wpScale);

        BooleanConfig[] wpShow = Config.Lists.WP_SHOW_OPTIONS;

        RenderSystem.setShaderTexture(0, SPRITESHEET); //set spritesheet as texture to draw
        if(shouldDrawDirections()) {
            for (int i = 0; i < 4; i++) { //two loops so waypoints render over directions
                DrawableHelper.drawTexture(matrices, imgPos[i], 5, i * sizeDir, vDir, sizeDir, sizeDir, tWidthDir, tHeightDir); //draw direction sprites
            }
        }
        if(shouldDrawWaypoints()) {
            for (int i = 3; i >= 0; i--) { //render waypoints a last for top layer
                if (wpShow[i].getBoolean())
                    DrawableHelper.drawTexture(matrices, imgPos[i + 4], 5, i * sizeWp, vWp, sizeWp, sizeWp, tWidthWp, tHeightWp); //draw waypoint sprites
            }
        }
    }
}

