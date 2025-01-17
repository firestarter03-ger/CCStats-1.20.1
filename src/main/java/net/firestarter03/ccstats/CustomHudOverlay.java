package net.firestarter03.ccstats;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomHudOverlay implements HudRenderCallback {

    private static final Identifier IMAGE_LEFT = new Identifier("ccstats", "textures/ui/left_overlay.png");
    private static final Identifier IMAGE_RIGHT = new Identifier("ccstats", "textures/ui/right_overlay.png");

    // Regex für allgemeine Attribute: "+Zahl% Name" oder "+Zahl Name"
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\+([0-9]+(?:\\.[0-9]+)?)\\s*(%?)\\s*(.+)");
    // Regex für Rüstung: "Rüstung Zahl"
    private static final Pattern ARMOR_PATTERN = Pattern.compile("Rüstung\\s+([0-9]+(?:\\.[0-9]+)?)");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Screen currentScreen = client.currentScreen;

        if (currentScreen instanceof GenericContainerScreen) {
            Text title = currentScreen.getTitle();

            if (title.getString().contains("㚝")) {
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // Bildschirmgröße
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Koordinaten für Overlays
                int xLeft = (int) (screenWidth * 0.115);  // Links
                int xRight = (int) (screenWidth * 0.65); // Rechts
                int yOverlay = (int) (screenHeight * 0.19); // Gleiche Höhe für linkes und rechtes Overlay
                int overlayWidth = 150;
                int overlayHeight = 210;

                // Koordinaten für Rüstungswert
                int xArmor = (screenWidth / 2) - 50; // Zentriert
                int armorYOffset = (int) (screenHeight * 0.15); // Höhe für Rüstungswert
                int armorTextColor = 0x00e5ff; // Cyan Text für Rüstung

                // Bilder für Overlays
                context.drawTexture(IMAGE_LEFT, xLeft, yOverlay, 0, 0, overlayWidth, overlayHeight, overlayWidth, overlayHeight);
                context.drawTexture(IMAGE_RIGHT, xRight, yOverlay, 0, 0, overlayWidth, overlayHeight, overlayWidth, overlayHeight);

                // Verarbeitung der Slots
                Map<String, Double> percentages = new HashMap<>();
                Map<String, Double> absolutes = new HashMap<>();
                double totalArmor = 0.0;

                List<Slot> slots = ((GenericContainerScreen) currentScreen).getScreenHandler().slots;

                for (int i = 0; i < 54; i++) {
                    Slot slot = slots.get(i);
                    if (!slot.getStack().isEmpty()) {
                        TooltipContext tooltipContext = client.options.advancedItemTooltips
                                ? TooltipContext.Default.ADVANCED
                                : TooltipContext.Default.BASIC;

                        List<Text> lore = slot.getStack().getTooltip(client.player, tooltipContext);

                        for (int j = 1; j < lore.size(); j++) {
                            String lineText = lore.get(j).getString();

                            Matcher attributeMatcher = ATTRIBUTE_PATTERN.matcher(lineText);
                            if (attributeMatcher.find()) {
                                double value = Double.parseDouble(attributeMatcher.group(1));
                                boolean isPercentage = !attributeMatcher.group(2).isEmpty();
                                String attributeName = attributeMatcher.group(3).trim();

                                if (isPercentage) {
                                    percentages.put(attributeName, percentages.getOrDefault(attributeName, 0.0) + value);
                                } else {
                                    absolutes.put(attributeName, absolutes.getOrDefault(attributeName, 0.0) + value);
                                }
                            }

                            Matcher armorMatcher = ARMOR_PATTERN.matcher(lineText);
                            if (armorMatcher.find()) {
                                totalArmor += Double.parseDouble(armorMatcher.group(1));
                            }
                        }
                    }
                }

                int maxFontSize = 12; // Maximale Schriftgröße
                int minFontSize = 2;  // Minimale Schriftgröße
                int availableHeight = overlayHeight - 22; // Höhe des Overlays abzüglich der Oberkante
                int overlayPadding = 9; //Abstand vom Rand des Overlays
                int maxOverlayTextWidth = overlayWidth - (2 * overlayPadding); // Maximale Breite des Textes im Overlay


                // Sortierte Listen für die Anzeige
                List<Map.Entry<String, Double>> sortedPercentages = new ArrayList<>(percentages.entrySet());
                sortedPercentages.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

                List<Map.Entry<String, Double>> sortedAbsolutes = new ArrayList<>(absolutes.entrySet());
                sortedAbsolutes.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

                int fontSizePercent = maxFontSize;
                int totalLinesPercent = sortedPercentages.size();

                // Berechnung für linkes Overlay (Prozentwerte)
                for (Map.Entry<String, Double> entry : sortedPercentages) {
                    String statText = String.format("%.2f%% %s", entry.getValue(), entry.getKey());

                    // Schriftgröße basierend auf vertikaler Höhe
                    while ((totalLinesPercent * (fontSizePercent + 3)) > availableHeight && fontSizePercent > minFontSize) {
                        fontSizePercent--;
                    }

                    // Schriftgröße basierend auf horizontaler Breite
                    while (client.textRenderer.getWidth(statText) * (fontSizePercent / 12.0f) > maxOverlayTextWidth && fontSizePercent > minFontSize) {
                        fontSizePercent--;
                    }
                }


                int fontSizeAbsolute = maxFontSize;
                int totalLinesAbsolute = sortedAbsolutes.size();

                // Berechnung für rechtes Overlay (Absolutwerte)
                for (Map.Entry<String, Double> entry : sortedAbsolutes) {
                    String statText = String.format("+%.0f %s", entry.getValue(), entry.getKey());

                    // Schriftgröße basierend auf vertikaler Höhe
                    while ((totalLinesAbsolute * (fontSizeAbsolute + 3)) > availableHeight && fontSizeAbsolute > minFontSize) {
                        fontSizeAbsolute--;
                    }

                    // Schriftgröße basierend auf horizontaler Breite
                    while (client.textRenderer.getWidth(statText) * (fontSizeAbsolute / 12.0f) > maxOverlayTextWidth && fontSizeAbsolute > minFontSize) {
                        fontSizeAbsolute--;
                    }
                }




                // Linkes Overlay: Prozentwerte
                int yOffsetLeft = yOverlay + 22;
                for (Map.Entry<String, Double> entry : sortedPercentages) {
                    String statText = String.format("%.2f%% %s", entry.getValue(), entry.getKey());

                    // Zeichne den Text
                    context.getMatrices().push();
                    context.getMatrices().scale(fontSizePercent / 12.0f, fontSizePercent / 12.0f, 1.0f);
                    context.drawText(client.textRenderer, statText, (int) ((xLeft + overlayPadding) / (fontSizePercent / 12.0)), (int) (yOffsetLeft / (fontSizePercent / 12.0)), 0xFFFFFF, false);
                    context.getMatrices().pop();

                    // Erhöhe die vertikale Position basierend auf der aktuellen Schriftgröße
                    yOffsetLeft += fontSizePercent + 3;

                    // Abbrechen, falls der Text über das Overlay hinausgeht
                    if (yOffsetLeft > (yOverlay + overlayHeight)) {
                        break;
                    }
                }



                // Rechtes Overlay: Absolute Werte
                int yOffsetRight = yOverlay + 22;
                for (Map.Entry<String, Double> entry : sortedAbsolutes) {
                    String statText = String.format("+%.0f %s", entry.getValue(), entry.getKey());

                    // Zeichne den Text
                    context.getMatrices().push();
                    context.getMatrices().scale(fontSizeAbsolute / 12.0f, fontSizeAbsolute / 12.0f, 1.0f);
                    context.drawText(client.textRenderer, statText, (int) ((xRight + overlayPadding) / (fontSizeAbsolute / 12.0)), (int) (yOffsetRight / (fontSizeAbsolute / 12.0)), 0xFFFFFF, false);
                    context.getMatrices().pop();

                    // Erhöhe die vertikale Position basierend auf der aktuellen Schriftgröße
                    yOffsetRight += fontSizeAbsolute + 3;

                    // Abbrechen, falls der Text über das Overlay hinausgeht
                    if (yOffsetRight > (yOverlay + overlayHeight)) {
                        break;
                    }
                }





                // Einzelner Rüstungswert anzeigen
                String armorText = String.format("Rüstung: %.2f", totalArmor);
                context.drawText(client.textRenderer, armorText, xArmor, armorYOffset, armorTextColor, false);

                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
            }
        }
    }
}
