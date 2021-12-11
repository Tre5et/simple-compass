package net.treset.compass.mixin;


import net.minecraft.client.MinecraftClient;
import net.treset.compass.CompassClient;
import net.treset.compass.config.WorldSpecificWaypoints;
import net.treset.compass.tools.WaypointTools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void onClientRender(CallbackInfo info) {
        if(MinecraftClient.getInstance().currentScreen == CompassClient.configScreen) { //only run if player is on config screen
            WaypointTools.setWaypointsOptions(); //check if waypoints should be set to player
            WaypointTools.setWaypointsToPlayer(); //check if options should be displayed or not
        }

        WorldSpecificWaypoints.readWriteWaypoints();
    }
}
