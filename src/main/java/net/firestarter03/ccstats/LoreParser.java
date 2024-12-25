package net.firestarter03.ccstats;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoreParser {
    private static final Pattern VALUE_PATTERN = Pattern.compile("([+-]?\\d+(\\.\\d+)?)(%?\\s?\\w+)");

    public static void parseLore(List<String> lore, DataAggregator aggregator) {
        for (String line : lore) {
            Matcher matcher = VALUE_PATTERN.matcher(line);
            while (matcher.find()) {
                double value = Double.parseDouble(matcher.group(1));
                String label = matcher.group(3).trim();
                aggregator.addValue(label, value);
            }
        }
    }
}
