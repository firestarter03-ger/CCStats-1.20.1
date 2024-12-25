package net.firestarter03.ccstats;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class CustomHudOverlay implements HudRenderCallback {

    private static final Identifier IMAGE = new Identifier("ccstats", "textures/ui/inv.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        //Pos wo Bild gerendert wird
        int x = 10;
        int y = 10;

        //Größe des Bildes
        int width = 125;
        int height = 200;

        //Bild rendern
        context.drawTexture(IMAGE, x, y, 0, 0, width, height, width, height);

        //Optional: Text rendern
        String text1 = "xx.xx% Münz Chance";
        context.drawText(client.textRenderer, text1, x + 5, y + height - 180, 0xFFFFFF, false);

        String text2 = "xxx.xx% Münz Menge";
        context.drawText(client.textRenderer, text2, x + 5, y + height - 160, 0xFFFFFF, false);

        String text3 = "xxx.xx% Dropchance";
        context.drawText(client.textRenderer, text3, x + 5, y + height - 140, 0xFFFFFF, false);

        String text4 = "xx.xx% Laufgeschwindigkeit";
        context.drawText(client.textRenderer, text4, x + 5, y + height - 120, 0xFFFFFF, false);

    }
}