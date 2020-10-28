package model;

import java.util.HashMap;
import java.util.Map;

public class DocumentData {
    private final Map<String, Double> termToFrequency = new HashMap<>();

    public void putTermFreq(String term, double freq) {
        termToFrequency.put(term, freq);
    }

    public double getFrequency(String term) {
        return termToFrequency.get(term);
    }
}
