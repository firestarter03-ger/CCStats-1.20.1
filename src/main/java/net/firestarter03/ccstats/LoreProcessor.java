package net.firestarter03.ccstats;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.Map;

public class LoreProcessor {
    private final DataAggregator aggregator = new DataAggregator();

    // Verarbeite die Items in den angegebenen Slots
    public void processSlots(int[] slots) {
        aggregator.clear();
        MinecraftClient client = MinecraftClient.getInstance();
        Screen screen = client.currentScreen;

        // Sicherstellen, dass der Bildschirm ein GenericContainerScreen ist
        if (screen instanceof GenericContainerScreen) {
            ScreenHandler handler = ((GenericContainerScreen) screen).getScreenHandler();

            // Iteriere durch die Slots und verarbeite die Items
            for (int slot : slots) {
                ItemStack stack = handler.getSlot(slot).getStack(); // Hole das Item aus dem Slot
                if (!stack.isEmpty()) {
                    List<String> lore = LoreUtils.getLore(stack);  // Lore des Items extrahieren
                    LoreParser.parseLore(lore, aggregator);  // Die Lore-Daten verarbeiten
                }
            }
        }
    }

    public void displayResults() {
        // Gib die berechneten Ergebnisse aus
        aggregator.getValues().forEach((label, value) -> {
            System.out.println(label + ": " + value);
        });
    }
}


