package net.treset.compass.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PlayerTools {

    public static boolean doesPlayerExist() {
        return MinecraftClient.getInstance().player != null;
    }

    public static double[] getPos() {
        double[] pos = new double[2];

        Entity cam = MinecraftClient.getInstance().getCameraEntity(); //camera position (also accurate when e.g. in freecam)
        assert cam != null;
        pos[0] = cam.getX();
        pos[1] = cam.getZ();

        return pos;
    }

    public static boolean isHoldingCompass() {
        if(!doesPlayerExist()) return false;
        PlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack handStack = player.getMainHandStack();
        ItemStack offHandStack = player.getOffHandStack();

        return ItemStack.areItemsEqualIgnoreDamage(new ItemStack(Items.COMPASS), handStack) ||
                ItemStack.areItemsEqualIgnoreDamage(new ItemStack(Items.COMPASS), offHandStack);
    }
}
