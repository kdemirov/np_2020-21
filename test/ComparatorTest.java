package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparatorTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Stoke");
        list.add("West Ham");
        list.stream().sorted(Comparator.comparing(String::toString)).forEach(s -> System.out.println(s));
    }
}
