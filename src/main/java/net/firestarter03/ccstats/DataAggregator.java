package net.firestarter03.ccstats;

import java.util.HashMap;
import java.util.Map;

public class DataAggregator {
    private final Map<String, Double> values = new HashMap<>();

    public void addValue(String label, double value) {
        values.put(label, values.getOrDefault(label, 0.0) + value);
    }

    public Map<String, Double> getValues() {
        return values;
    }

    public void clear() {
        values.clear();
    }
}
