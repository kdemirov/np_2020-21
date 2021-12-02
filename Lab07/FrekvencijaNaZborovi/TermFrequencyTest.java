package Lab07.FrekvencijaNaZborovi;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class Term {
    String term;

    public Term(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term.toLowerCase();
    }

    public static List<Term> factoryTerm(String line) {
        String[] parts = line.split("\\s+");
        List<Term> terms = new ArrayList<>();
        for (String part : parts) {
            if (containsSigns(part) && !part.isEmpty() && !part.chars().allMatch(c -> c == '-')) {
                String temp = removeSigns(part);
                terms.add(new Term(temp));
            } else {
                if (!part.isEmpty() && !part.chars().allMatch(c -> c == '-'))
                    terms.add(new Term(part));
            }
        }
        return terms;
    }

    private static boolean containsSigns(String part) {
        return part.chars().anyMatch(c -> c == '.') || part.chars().anyMatch(c -> c == ',');
    }

    public static String removeSigns(String word) {

        int index = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ',' ||
                    word.charAt(i) == '.') {
                index = i;
            }
        }
        return word.substring(0, index);

    }
}

class TermFrequency {
    private List<String> stop;
    private List<String> totalWords;
    private Set<String> distinctWords;
    private InputStream inputStream;

    public TermFrequency(InputStream inputStream, String[] stop) {
        this.stop = Arrays.asList(stop.clone());
        this.inputStream = inputStream;
        this.totalWords = new ArrayList<>();
        this.distinctWords = new TreeSet<>();
    }

    private List<String> fillTotalWords() {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return br.lines().map(line -> Term.factoryTerm(line)).flatMap(t -> t.stream().map(Term::getTerm))
                .filter(term -> !stop.contains(term))
                .collect(Collectors.toList());
    }

    private Set<String> fillDistinctWords() {
        totalWords.forEach(t -> distinctWords.add(t));
        return distinctWords;
    }

    public int countTotal() {
        totalWords = fillTotalWords();
        return totalWords.size();
    }

    public int countDistinct() {
        distinctWords = fillDistinctWords();
        return distinctWords.size();
    }

    public List<String> mostOften(int k) {
        TreeMap<String, Integer> map;

        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "TermFrequency{" +
                ", totalWords=" + totalWords +
                ", distinctWords=" + distinctWords +

                '}';
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
        System.out.println(tf);
    }
}
// vasiot kod ovde
