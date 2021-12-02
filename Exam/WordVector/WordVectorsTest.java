package Exam.WordVector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test
 */
class WordVectors {
    private Map<String, List<Integer>> wordsWithVectors;
    private List<String> words;
    private List<String> calculatedWords;
    private List<List<Integer>> vectors;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.wordsWithVectors = new HashMap<>();
        calculatedWords = Arrays.stream(words).collect(Collectors.toList());
        this.words = new ArrayList<>();
        this.vectors = vectors;
    }

    public void readWords(List<String> words) {
        this.words = words;
        this.words.forEach(w -> wordsWithVectors.putIfAbsent(w, calculateVector(w)));
    }

    public List<Integer> slidingWindow(int n) {
        for (int i = 0; i < n; i++) {

        }
        return new ArrayList<>();
    }

    private List<Integer> calculateVector(String word) {
        List<String> tmpList = calculatedWords
                .stream()
                .filter(w -> w.length() == word.length())
                .collect(Collectors.toList());
        int counter = 0;
        if (tmpList.size() != 0) {
            for (String w : calculatedWords) {
                for (int i = 0; i < w.length(); i++) {
                    if (w.charAt(i) != word.charAt(i)) {
                        counter++;
                    }

                }
                if (counter > 1) {
                    counter = 0;
                    continue;
                } else {
                    int indexOfList = calculatedWords.indexOf(w);
                    return this.vectors.get(indexOfList);
                }
            }
        }

        List<Integer> neutralVector = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> 5)
                .collect(Collectors.toList());
        return neutralVector;
    }



}
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}



