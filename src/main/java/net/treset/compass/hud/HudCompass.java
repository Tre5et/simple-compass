package net.treset.compass.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.config.Config;
import net.treset.compass.tools.PlayerTools;
import net.treset.vanillaconfig.config.BooleanConfig;
import net.treset.vanillaconfig.config.IntegerConfig;

import java.util.Arrays;
import java.util.List;

public class HudCompass {

    private static final Identifier COMPASS_NORTH = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_north.png");
    private static final Identifier COMPASS_WEST = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_west.png");
    private static final Identifier COMPASS_SOUTH = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_south.png");
    private static final Identifier COMPASS_EAST = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_east.png");
    private static final List<Identifier> COMPASS_DIRECTIONS = Arrays.asList(
            COMPASS_NORTH,
            COMPASS_WEST,
            COMPASS_SOUTH,
            COMPASS_EAST
    );
    private static final Identifier COMPASS_WAYPOINT_A = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_waypoint_a.png");
    private static final Identifier COMPASS_WAYPOINT_B = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_waypoint_b.png");
    private static final Identifier COMPASS_WAYPOINT_C = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_waypoint_c.png");
    private static final Identifier COMPASS_WAYPOINT_D = new Identifier(CompassMod.MOD_ID, "textures/gui/sprites/hud/compass_waypoint_d.png");
    private static final List<Identifier> COMPASS_WAYPOINTS = Arrays.asList(
            COMPASS_WAYPOINT_A,
            COMPASS_WAYPOINT_B,
            COMPASS_WAYPOINT_C,
            COMPASS_WAYPOINT_D
    );

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

    public static void handleCompass(DrawContext ctx) {

        cam = cli.getCameraEntity(); //set camera / cancel if no camera is present

        if (!shouldDrawDirections() && !shouldDrawWaypoints()) return;

        int windowWidth = cli.getWindow().getScaledWidth(); //check if window was resized
        if (windowWidth != prevWindowWidth) {
            forceUpdateNextFrame = true;
            prevWindowWidth = windowWidth;
        }

        updatePositions();

        renderCompass(ctx);
    }

    public static double getYaw() {
        double yaw;

        double headYaw = cam.getYaw();
        headYaw %= 360; //rotation {0..360}
        if (headYaw < 0) headYaw += 360; //correct for negative values

        yaw = headYaw / 360; //rotation {0..1}

        return yaw;
    }

    public static double getPositionByAngle(double angle) {
        double camYaw = getYaw();

        double imgPos;
        double compassScale = Config.COMPASS_SCALE.getDouble();

        double compassOffset = (float) (0.5 * (compassScale - 1)); //offset to account for compass scale

        double windowX = cli.getWindow().getScaledWidth();

        camYaw = 1 - camYaw; //invert to line up with rotation direction
        camYaw -= angle; //rotate position to account for angle
        if (camYaw < 0) camYaw = 1 + camYaw; //loop negative values back to the other side

        imgPos = windowX * ((camYaw) * compassScale - compassOffset); //calculate position on screen

        return imgPos;
    }

    public static double getAngleOfPoint(double targetX, double targetZ) {
        double angle = 0;

        double[] pos = PlayerTools.getPos();

        assert cli.player != null;
        double coordScale = cli.player.clientWorld.getDimension().coordinateScale();

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
        if(cam == null) return false;
        switch(Config.DIR_DISPLAY_MODE.getOption()) {
            case "config.compass.display_mode.list.always" -> { return true; }
            case "config.compass.display_mode.list.hand" -> { return PlayerTools.isHoldingCompass(); }
            case "config.compass.display_mode.list.hotbar" -> { return PlayerTools.hasCompassInHotbar(); }
            case "config.compass.display_mode.list.inventory" -> { return PlayerTools.hasCompassInInventory(); }
            case "config.compass.display_mode.list.never" -> { return false; }
            default -> { return false; }
        }
    }

    private static boolean shouldDrawWaypoints() {
        if(cam == null) return false;
        switch(Config.WP_DISPLAY_MODE.getOption()) {
            case "config.compass.display_mode.list.always" -> { return true; }
            case "config.compass.display_mode.list.hand" -> { return PlayerTools.isHoldingCompass(); }
            case "config.compass.display_mode.list.hotbar" -> { return PlayerTools.hasCompassInHotbar(); }
            case "config.compass.display_mode.list.inventory" -> { return PlayerTools.hasCompassInInventory(); }
            case "config.compass.display_mode.list.never" -> { return false; }
            default -> { return false; }
        }
    }

    private static void updatePositions() {
        boolean sameYaw = prevYaw == getYaw();
        boolean samePos = Arrays.equals(PlayerTools.getPos(), prevPos);

        BooleanConfig[] wpShow = Config.Lists.WP_SHOW_OPTIONS;
        IntegerConfig[] wpCoords = Config.Lists.WP_COORD_OPTIONS;

        if((!sameYaw && shouldDrawDirections() && shouldDrawWaypoints()) || forceUpdateNextFrame) { //update everything
            imgPos = prevImgPos; //copy all previous values

            for (int i = 0; i < 4; i++) {
                imgPos[i] = (int) getPositionByAngle((float) i / 4); //override direction values
                if (wpShow[i].getBoolean())
                    imgPos[i + 4] = (int) getPositionByAngle(getAngleOfPoint(wpCoords[i * 2].getInteger(), wpCoords[i * 2 + 1].getInteger())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
            prevYaw = getYaw();

            forceUpdateNextFrame = false;
        } else if(!sameYaw && shouldDrawDirections()) { //update only directions
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                imgPos[i] = (int) getPositionByAngle((float) i / 4); //override direction values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
            prevYaw = getYaw();
        } else if((!sameYaw && shouldDrawWaypoints()) || (!samePos && shouldDrawWaypoints())) { //update only waypoints
            imgPos = prevImgPos; //copy all previous values

            for(int i = 0; i < 4; i++) {
                if(wpShow[i].getBoolean()) imgPos[i + 4] = (int)getPositionByAngle(getAngleOfPoint(wpCoords[i*2].getInteger(), wpCoords[i * 2 + 1].getInteger())); //override waypoint values
            }

            prevImgPos = imgPos; //copy all values into previous values
            prevPos = PlayerTools.getPos();
        } else { //update nothing
            imgPos = prevImgPos; //copy all previous values
        }
    }

    private static void renderCompass(DrawContext ctx) {
        double dirScale = Config.DIR_SIZE.getDouble(); //handle direction scale
        double wpScale = Config.WP_SIZE.getDouble(); //handle waypoint scale

        /*int tWidthDir = (int) (tWidth * dirScale);
        int tHeightDir = (int) (tHeight * dirScale);
        int tWidthWp = (int) (tWidth * wpScale);
        int tHeightWp = (int) (tHeight * wpScale);*/

        int sizeDir = (int) (32 * dirScale);
        int sizeWp = (int) (32 * wpScale);

        int dirOffset = (int)Math.rint(sizeDir / 2f);
        int wpOffset = (int)Math.rint(sizeWp / 2f);

        BooleanConfig[] wpShow = Config.Lists.WP_SHOW_OPTIONS;

        if(shouldDrawDirections()) {
            for (int i = 0; i < 4; i++) { //two loops so waypoints render over directions
                ctx.drawTexture(COMPASS_DIRECTIONS.get(i), imgPos[i] - dirOffset, 5, 0, 0, sizeDir, sizeDir, sizeDir, sizeDir); //draw direction sprites
            }
        }
        if(shouldDrawWaypoints()) {
            for (int i = 3; i >= 0; i--) { //render waypoints a last for top layer
                if (wpShow[i].getBoolean())
                    ctx.drawTexture(COMPASS_WAYPOINTS.get(i), imgPos[i + 4] - wpOffset, 5, 0, 0, sizeWp, sizeWp, sizeWp, sizeWp); //draw waypoint sprites
            }
        }
    }
}

