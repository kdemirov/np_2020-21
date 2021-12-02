
package Lab07.Anagrami;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
        //System.out.println(anagramKey("cares"));

    }

    public static boolean isAnagram(String word, String text) {
        if (word.length() != text.length()) return false;
        char[] temp1 = text.toLowerCase().toCharArray();
        char[] temp2 = word.toLowerCase().toCharArray();
        Arrays.sort(temp1);
        Arrays.sort(temp2);
        return Arrays.equals(temp1, temp2);
    }

    public static String anagramKey(String word) {
        char[] temp1 = word.toCharArray();
        Arrays.sort(temp1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp1.length; i++) {
            sb.append(temp1[i]);
        }
        return sb.toString();
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> words = br.lines().collect(Collectors.toList());
        Map<String, TreeSet<String>> anagrams = new LinkedHashMap<>();
        for (String word : words) {
            anagrams.putIfAbsent(anagramKey(word), new TreeSet<>());
            for (String temp : words) {
                if (isAnagram(word, temp)) {
                    anagrams.get(anagramKey(word)).add(word);
                }
            }
        }
        StringBuilder sb = new StringBuilder();

        for (TreeSet<String> treeSet : anagrams.values()) {
            if (treeSet.size() >= 5) {
                for (String s : treeSet) {
                    sb.append(s + " ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
    }

}

