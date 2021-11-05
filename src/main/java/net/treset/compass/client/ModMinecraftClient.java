package net.treset.compass.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.player.PlayerEntity;

public class ModMinecraftClient extends MinecraftClient {
    public ModMinecraftClient(RunArgs args) {
        super(args);
    }

    public static float[] getRotation() {
        float[] rotation;

        PlayerEntity player = MinecraftClient.getInstance().player;

        assert MinecraftClient.getInstance().player != null;
        float headYaw = player.getHeadYaw();
        float headPitch = player.getPitch();

        headYaw = headYaw % 360;

        if(headYaw < 0) headYaw += 360;

        rotation = new float[] {headYaw, headPitch};

        return rotation;
    }

    public static float[] textPosition() {
        float[] textPos = new float[] {0, 0};
        int textLengthX = 32;
        int textHeightY = 32;
        int compassScale = 3;

        float windowX = getInstance().getWindow().getScaledWidth();
        float windowY = getInstance().getWindow().getScaledHeight();

        windowX -= textLengthX;
        windowY -= textHeightY;

        float[] headRotation = getRotation();

        windowX = (float)(windowX * ((1 - (headRotation[0] / 360)) * compassScale - 0.5 * (compassScale - 1)));
        windowY *= 1 - ((headRotation[1] + 90) / 180);

        textPos[0] = windowX;
        textPos[1] = windowY;

        return textPos;
    }
}
