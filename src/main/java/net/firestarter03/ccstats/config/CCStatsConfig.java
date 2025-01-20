package net.firestarter03.ccstats.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.Color;

public class CCStatsConfig {
    public static final ConfigClassHandler<CCStatsConfig> CONFIG = ConfigClassHandler.createBuilder(CCStatsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("ccstats.json"))
                    .build())
            .build();

    @SerialEntry public Color textColor = Color.WHITE;
    @SerialEntry public int maxPercentageLines = 0; // 0 = Alle Prozentwerte anzeigen
    @SerialEntry public int maxAbsoluteLines = 0;  // 0 = Alle Absolutwerte anzeigen


    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("CCStats Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("CCLive ArmorStats Settings"))
                        .option(Option.<Color>createBuilder()
                                .name(Text.literal("Text Color"))
                                .description(OptionDescription.of(Text.literal("Set the text color for stats.")))
                                .binding(defaults.textColor, () -> config.textColor, newVal -> config.textColor = newVal)
                                .controller(ColorControllerBuilder::create) // Farbcontroller ohne Alpha
                                .build())
                        .option(Option.<Integer>createBuilder() // Option für maximale Prozentwerte
                                .name(Text.literal("Max. Percentage Lines"))
                                .description(OptionDescription.of(Text.literal(
                                        "Set the maximum number of percentage lines to display.\n" +
                                                "0 = All\nX = Your chosen limit")))
                                .binding(
                                        defaults.maxPercentageLines, // Standardwert
                                        () -> config.maxPercentageLines, // Aktueller Wert
                                        newVal -> config.maxPercentageLines = newVal) // Neue Werte setzen
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 12)
                                        .step(1)
                                        .formatValue(val -> Text.literal(val + " Lines")))
                                .build())
                        .option(Option.<Integer>createBuilder() // Option für maximale Absolutwerte
                                .name(Text.literal("Max. Absolute Lines"))
                                .description(OptionDescription.of(Text.literal(
                                        "Set the maximum number of absolute lines to display.\n" +
                                                "0 = All\nX = Your chosen limit")))
                                .binding(
                                        defaults.maxAbsoluteLines, // Standardwert
                                        () -> config.maxAbsoluteLines, // Aktueller Wert
                                        newVal -> config.maxAbsoluteLines = newVal) // Neue Werte setzen
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 12)
                                        .step(1)
                                        .formatValue(val -> Text.literal(val + " Lines")))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}