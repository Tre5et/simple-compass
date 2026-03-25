package net.treset.compass.tools;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PlayerTools {
    public static boolean isHoldingCompass() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(Minecraft.getInstance().player == null) return false;

        return player.getMainHandItem().is(Items.COMPASS) ||
                player.getOffhandItem().is(Items.COMPASS);
    }

    public static boolean hasCompassInHotbar() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(Minecraft.getInstance().player == null) return false;

        for(int i = 0; i < 9; i++) {
            if(player.getInventory().getItem(i).is(Items.COMPASS)) {
                return true;
            }
        }

        return player.getOffhandItem().is(Items.COMPASS);
    }

    public static boolean hasCompassInInventory() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(Minecraft.getInstance().player == null) return false;

        return player.getInventory().contains(new ItemStack(Items.COMPASS))
                || player.getOffhandItem().is(Items.COMPASS);
    }
}
