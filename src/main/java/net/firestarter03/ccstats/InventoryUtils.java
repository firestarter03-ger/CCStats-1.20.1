package net.firestarter03.ccstats;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.GenericContainerScreenHandler;

public class InventoryUtils {
    public static boolean isTargetInventoryOpen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof GenericContainerScreen screen) {
            return "㚝".equals(screen.getTitle().getString());
        }
        return false;
    }

}
