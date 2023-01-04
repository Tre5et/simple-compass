package net.treset.compass.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PlayerTools {

    public static double[] getPos() {
        double[] pos = new double[2];

        Entity cam = MinecraftClient.getInstance().getCameraEntity(); //camera position (also accurate when e.g. in freecam)
        assert cam != null;
        pos[0] = cam.getX();
        pos[1] = cam.getZ();

        return pos;
    }

    public static boolean isHoldingCompass() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(MinecraftClient.getInstance().player == null) return false;

        return player.getMainHandStack().isItemEqual(new ItemStack(Items.COMPASS)) ||
                player.getOffHandStack().isItemEqual(new ItemStack(Items.COMPASS));
    }

    public static boolean hasCompassInHotbar() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(MinecraftClient.getInstance().player == null) return false;

        for(int i = 0; i < 9; i++) {
            if(player.getInventory().getStack(i).isItemEqual(new ItemStack(Items.COMPASS))) {
                return true;
            }
        }

        return player.getOffHandStack().isItemEqual(new ItemStack(Items.COMPASS));
    }

    public static boolean hasCompassInInventory() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(MinecraftClient.getInstance().player == null) return false;

        return player.getInventory().getSlotWithStack(new ItemStack(Items.COMPASS)) >= 0
                || player.getOffHandStack().isItemEqual(new ItemStack(Items.COMPASS));
    }
}
