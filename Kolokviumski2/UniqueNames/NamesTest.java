package Kolokviumski2.UniqueNames;

import java.util.*;
import java.util.stream.Collectors;

class Names {

    private Set<String> names;
    private Map<String, Integer> namesMap;

    public Names() {
        this.names = new TreeSet<>();
        this.namesMap = new TreeMap<>();
    }

    public void addName(String name) {
        this.names.add(name);
        this.namesMap.putIfAbsent(name, 0);
        this.namesMap.computeIfPresent(name, (k, v) -> {
            v++;
            return v;

        });
    }

    public void printN(int n) {
        this.namesMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= n)
                .map(entry -> String.format("%s (%d) %d", entry.getKey(), entry.getValue(), countUniqueLetters(entry.getKey())))
                .forEach(System.out::println);
    }

    public String findName(int len, int x) {
        List<String> tempNAmes = this.names
                .stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());
        if (x > tempNAmes.size()) {
            int i = 0;
            while (true) {
                for (int j = 0; j < tempNAmes.size(); j++) {
                    if (i == x) {
                        return tempNAmes.get(j);
                    }
                    i++;
                }

            }

        }
        return tempNAmes.get(x);
    }

    public static int countUniqueLetters(String name) {
        boolean[] isItThere = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < name.length(); i++) {
            isItThere[name.toLowerCase().charAt(i)] = true;
        }
        int count = 0;
        for (int i = 0; i < isItThere.length; i++) {
            if (isItThere[i] == true) {
                count++;
            }
        }
        return count;
    }

}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde