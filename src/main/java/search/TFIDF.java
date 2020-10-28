package search;

import java.util.List;

public class TFIDF {
    public static double calculateTermFreq(List<String> words, String term) {
        long count = 0;
        for (String word : words) {
            if (term.equalsIgnoreCase(word)) {
                count++;
            }
        }
        return (double) count / words.size();
    }
}
