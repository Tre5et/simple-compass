package net.treset.compass.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.treset.compass.CompassMod;
import net.treset.compass.config.Config;
import net.treset.compass.tools.PlayerTools;

import java.util.Arrays;
import java.util.List;

public class HudCompass {

    private static final Identifier COMPASS_NORTH = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_north");
    private static final Identifier COMPASS_WEST = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_west");
    private static final Identifier COMPASS_SOUTH = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_south");
    private static final Identifier COMPASS_EAST = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_east");
    private static final List<Identifier> COMPASS_DIRECTIONS = Arrays.asList(
            COMPASS_NORTH,
            COMPASS_WEST,
            COMPASS_SOUTH,
            COMPASS_EAST
    );
    private static final Identifier COMPASS_WAYPOINT_A = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_waypoint_a");
    private static final Identifier COMPASS_WAYPOINT_B = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_waypoint_b");
    private static final Identifier COMPASS_WAYPOINT_C = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_waypoint_c");
    private static final Identifier COMPASS_WAYPOINT_D = Identifier.fromNamespaceAndPath(CompassMod.MOD_ID, "hud/compass_waypoint_d");
    private static final List<Identifier> COMPASS_WAYPOINTS = Arrays.asList(
            COMPASS_WAYPOINT_A,
            COMPASS_WAYPOINT_B,
            COMPASS_WAYPOINT_C,
            COMPASS_WAYPOINT_D
    );

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void handleCompass(GuiGraphicsExtractor ctx, DeltaTracker tracker) {
        if(minecraft.options.hideGui || (!shouldDrawDirections() && !shouldDrawWaypoints())) return;
        Entity camera = minecraft.getCameraEntity();
        if(camera == null) return;

        float yaw = ((camera.getYRot(tracker.getGameTimeDeltaPartialTick(true)) % 360 + 360) % 360 / 360);
        Vec3 position = camera.getPosition(tracker.getGameTimeDeltaPartialTick(true));
        int windowWidth = minecraft.getWindow().getGuiScaledWidth();

        int directionSize = (int) (32 * Config.DIR_SIZE.getDouble());
        int waypointSize = (int) (32 * Config.WP_SIZE.getDouble());

        if(shouldDrawDirections()) {
            for (int i = 0; i < 4; i++) { //two loops so waypoints render over directions
                renderCardinal(ctx, i, yaw, windowWidth, directionSize);
            }
        }
        if(shouldDrawWaypoints()) {
            for (int i = 3; i >= 0; i--) { //render waypoints a last for top layer
                if (Config.Lists.WP_SHOW_OPTIONS[i].getBoolean()) {
                    renderWaypoint(ctx, i, position, yaw, windowWidth, waypointSize);
                }
            }
        }
    }

    public static double getPositionByAngle(double angle, float yaw, double windowWidth) {
        double compassScale = Config.COMPASS_SCALE.getDouble();

        double compassOffset = (float) (0.5 * (compassScale - 1)); //offset to account for compass scale

        double camYaw = 1 - yaw; //invert to line up with rotation direction
        camYaw -= angle; //rotate position to account for angle
        if (camYaw < 0) camYaw = 1 + camYaw; //loop negative values back to the other side

        return windowWidth * ((camYaw) * compassScale - compassOffset); //calculate position on screen
    }

    public static void renderAtAngle(GuiGraphicsExtractor ctx, Identifier sprite, double angle, float yaw, double windowWidth, int size) {
        double offset = size / 2D;
        double xPos = getPositionByAngle(angle, yaw, windowWidth);
        int x = Math.toIntExact(Math.round(xPos - offset));
        ctx.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, 5, size, size);
    }

    public static void renderCardinal(GuiGraphicsExtractor ctx, int index, float yaw, double windowWidth, int size) {
        renderAtAngle(ctx, COMPASS_DIRECTIONS.get(index), index / 4D, yaw, windowWidth, size);
    }

    public static double getAngleOfPoint(double targetX, double targetZ, Vec3 position) {
        double angle = 0;

        assert minecraft.player != null;
        double coordScale = minecraft.player.level().dimensionType().coordinateScale();

        double posX = position.x * coordScale;
        double posZ = position.z * coordScale;

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

    public static void renderWaypoint(GuiGraphicsExtractor ctx, int index, Vec3 position, float yaw, double windowWidth, int size) {
        double angle = getAngleOfPoint(Config.Lists.WP_COORD_OPTIONS[index*2].getInteger(), Config.Lists.WP_COORD_OPTIONS[index*2+1].getInteger(), position);
        renderAtAngle(ctx, COMPASS_WAYPOINTS.get(index), angle, yaw, windowWidth, size);
    }

    private static boolean shouldDrawDirections() {
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
        switch(Config.WP_DISPLAY_MODE.getOption()) {
            case "config.compass.display_mode.list.always" -> { return true; }
            case "config.compass.display_mode.list.hand" -> { return PlayerTools.isHoldingCompass(); }
            case "config.compass.display_mode.list.hotbar" -> { return PlayerTools.hasCompassInHotbar(); }
            case "config.compass.display_mode.list.inventory" -> { return PlayerTools.hasCompassInInventory(); }
            case "config.compass.display_mode.list.never" -> { return false; }
            default -> { return false; }
        }
    }
}

