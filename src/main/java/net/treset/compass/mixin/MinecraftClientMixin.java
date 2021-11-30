package net.treset.compass.mixin;


import net.minecraft.client.MinecraftClient;
import net.treset.compass.helpers.WaypointHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void onClientRender(CallbackInfo info) {
        WaypointHelper.setWaypointsToPlayer();

        WaypointHelper.setWaypointsOptions();
    }
}
