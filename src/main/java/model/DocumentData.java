package model;

import java.util.HashMap;
import java.util.Map;

public class DocumentData {
    private Map<String, Double> termToFrequency = new HashMap<String, Double>();

    public void putTermFreq(String term, double freq) {
        termToFrequency.put(term, freq);
    }

    public void getFrequency(String term) {
        termToFrequency.get(term);
    }
}
