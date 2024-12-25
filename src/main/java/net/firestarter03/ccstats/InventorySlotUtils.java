package net.firestarter03.ccstats;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;

public class InventorySlotUtils {
    public static ItemStack getItemSlot(int slot) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.currentScreenHandler instanceof GenericContainerScreenHandler handler) {
            return handler.getSlot(slot).getStack();
        }
        return ItemStack.EMPTY;
    }
}
