package net.treset.compass.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.treset.compass.CompassMod;
import net.treset.compass.client.ModMinecraftClient;
import net.treset.compass.gui.CompassGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    /*@Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        CompassMod.LOGGER.info("Initialized InGameHud mixin!");
    }*/

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void onRender (MatrixStack matrices, float tickDelta, CallbackInfo info) {

        CompassGui.drawCompass(matrices);
        //ModMinecraftClient.getInstance().textRenderer.draw(matrices, "N^N", ModMinecraftClient.textPosition()[0], 5, -1);
    }
}