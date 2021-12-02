package Exam.GenerickoSortiranje;

import java.util.*;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if(n==1){
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            System.out.println(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map = new HashMap<>();
            fillIntegerStringMap(l, map);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map);
            System.out.println(map);
            System.out.println(s);
        }

    }

    private  static <T,U extends Comparable<U>> SortedSet<Map.Entry<T, U>> entriesSortedByValues(Map<T, U> map) {
        SortedSet<Map.Entry<T,U>> entries;
        entries = new TreeSet<>(new Comparator<Map.Entry<T, U>>() {
            @Override
            public int compare(Map.Entry<T, U> o1, Map.Entry<T, U> o2) {
                int result=o2.getValue().compareTo(o1.getValue());
                if(result!=0){
                    return result;
                }
                return 1;
            }
        });
        entries.addAll(map.entrySet());
        return entries;
    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String,Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

    //вашиот код овде
}