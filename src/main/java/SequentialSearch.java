import model.DocumentData;
import search.TFIDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SequentialSearch {
    public static final String BOOKS_DIRECTORY = "./resources/books";
    public static final String SEARCH_QUERY_1 = "scene where katniss eats on the train first time bread butter";
    public static final String SEARCH_QUERY_2 = "katniss story about father";
    public static final String SEARCH_QUERY_3 = "";

    public static void main(String[] args) throws FileNotFoundException {
        File documentsDirectory = new File(BOOKS_DIRECTORY);
        List<String> documents = Arrays.stream(Objects.requireNonNull(documentsDirectory.list()))
                .map(documentName -> BOOKS_DIRECTORY + "/" + documentName)
                .collect(Collectors.toList());
        List<String> terms = TFIDF.getWordsFromLine(SEARCH_QUERY_2);
        findMostRelevantDocuments(documents, terms);

    }

    private static void findMostRelevantDocuments(List<String> documents, List<String> terms) throws FileNotFoundException {
        Map<String, DocumentData> documentsResult = new HashMap<>();
        for (String document : documents) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(document));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            List<String> words = TFIDF.getWordsFromLines(lines);
            DocumentData documentData = TFIDF.createDocumentData(words, terms);
            documentsResult.put(document, documentData);
        }
        Map<Double, List<String>> documentsByScore = TFIDF.getDocumentsSortedByScore(terms, documentsResult);
        printResults(documentsByScore);
    }

    private static void printResults(Map<Double, List<String>> documentsByScore) {
        for (Map.Entry<Double, List<String>> docScorePair : documentsByScore.entrySet()) {
            double score = docScorePair.getKey();
            for (String document : docScorePair.getValue()) {
                System.out.printf("Book : %s - score : %f%n", document.split("/")[3], score);
            }
        }
    }
}
