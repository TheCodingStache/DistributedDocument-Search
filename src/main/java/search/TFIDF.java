package search;

import model.DocumentData;

import java.util.*;

public class TFIDF {
    public static double calculateTermFreq(List<String> words, String term) {
        long count = 0;
        for (String word : words) {
            if (term.equalsIgnoreCase(word)) {
                count++;
            }
        }
        double termFrequency = (double) count / words.size();
        return termFrequency;
    }

    public static DocumentData createDocumentData(List<String> words, List<String> terms) {
        DocumentData documentData = new DocumentData();
        for (String term : terms) {
            double termFreq = calculateTermFreq(words, term.toLowerCase());
            documentData.putTermFreq(term, termFreq);
        }
        return documentData;
    }

    private static double getInverseDocumentFrequency(String term, Map<String, DocumentData> documentResults) {
        double numberOfDocuments = 0;
        for (String document : documentResults.keySet()) {
            DocumentData documentData = documentResults.get(document);
            double termFreq = documentData.getFrequency(term);
            if (termFreq > 0.0) {
                numberOfDocuments++;
            }
        }
        return numberOfDocuments == 0 ? 0 : Math.log10(documentResults.size() / numberOfDocuments);
    }

    private static Map<String, Double> getTermToInverseDocumentFreqMap(List<String> terms,
                                                                       Map<String, DocumentData> documentResults) {
        Map<String, Double> termToIDF = new HashMap<>();
        for (String term : terms) {
            double idf = getInverseDocumentFrequency(term, documentResults);
            termToIDF.put(term, idf);
        }
        return termToIDF;
    }

    private static double calculateDocumentScore(List<String> terms,
                                                 DocumentData documentData,
                                                 Map<String, Double> termToInverseDocumentFrequency) {
        double score = 0;
        for (String term : terms) {
            double termFreq = documentData.getFrequency(term);
            double inverseTermFreq = termToInverseDocumentFrequency.get(term);
            score += termFreq * inverseTermFreq;
        }
        return score;
    }

    public static Map<Double, List<String>> getDocumentsSortedByScore(List<String> terms, Map<String, DocumentData> documentResults) {
        TreeMap<Double, List<String>> documentsScore = new TreeMap<>();
        Map<String, Double> termToInverseDocumentFreq = getTermToInverseDocumentFreqMap(terms, documentResults);
        for (String document : documentResults.keySet()) {
            DocumentData documentData = documentResults.get(document);
            double score = calculateDocumentScore(terms, documentData, termToInverseDocumentFreq);
            addDocumentScoreToTreeMap(documentsScore, score, document);
        }
        return documentsScore.descendingMap();
    }

    private static void addDocumentScoreToTreeMap(TreeMap<Double, List<String>> scoreToDocuments, double score, String document) {
        List<String> documentWithCurrentScore = scoreToDocuments.get(score);
        if (documentWithCurrentScore == null) {
            documentWithCurrentScore = new ArrayList<>();
        }
        documentWithCurrentScore.add(document);
        scoreToDocuments.put(score, documentWithCurrentScore);
    }

    public static List<String> getWordsFromLine(String line) {
        return Arrays.asList(line.split("(\\.)+|(,)+|( )+|(-)+|(\\?)+|(!)+|(;)+|(:)+|(/d)+|(/n)+"));
    }

    public static List<String> getWordsFromLines(List<String> lines) {
        List<String> words = new ArrayList<>();
        for (String line : lines) {
            words.addAll(getWordsFromLine(line));
        }
        return words;
    }
}
