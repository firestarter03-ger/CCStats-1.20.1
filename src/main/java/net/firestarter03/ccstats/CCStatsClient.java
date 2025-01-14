package net.firestarter03.ccstats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CCStatsClient implements ClientModInitializer {
    private static LoreProcessor processor;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new CustomHudOverlay());
        // Registriere den ClientTickEvent, um regelmäßig den aktiven Bildschirm zu überprüfen
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;

            if (currentScreen instanceof GenericContainerScreen) {
                Text title = currentScreen.getTitle();

                // Prüfe, ob der Titel des Inventars "㚝" ist
                if ("㚝".equals(title.getString())) {
                    if (processor == null) {
                        processor = new LoreProcessor();
                        // Definiere hier deine spezifischen Slots, die du auslesen möchtest
                        int[] slotsToCheck = {2, 6, 11, 15, 20, 24, 29, 33, 38, 42, 47, 48, 50, 51};
                        processor.processSlots(slotsToCheck);  // Übergibt die Slots zur Verarbeitung
                    }
                } else {
                    // Falls der Titel nicht "㚝" ist, das Rechnen stoppen
                    if (processor != null) {
                        processor.displayResults();
                        processor = null; // Setze den Prozessor auf null, wenn der Bildschirm geschlossen wird
                    }
                }
            }
        });
    }
}




