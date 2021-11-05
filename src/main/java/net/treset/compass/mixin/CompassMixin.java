package net.treset.compass.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.treset.compass.CompassMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class CompassMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		CompassMod.LOGGER.info("Initialized TitleScreen mixin!");
	}
}
