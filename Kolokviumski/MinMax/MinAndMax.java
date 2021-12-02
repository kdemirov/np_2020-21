package Kolokviumski.MinMax;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    private T max;
    private T min;
    private int total;
    private int minCount;
    private int maxCount;

    public MinMax() {
        this.total = 0;
        this.minCount = 0;
        this.maxCount = 0;
    }

    public void update(T element) {
        if (total == 0) {
            min = element;
            max = element;
        }
        ++total;
        if (element.compareTo(min) < 0) {
            minCount = 1;
            min = element;
        } else {
            if (element.compareTo(min) == 0) {
                minCount++;
            }
        }
        if (element.compareTo(max) > 0) {
            maxCount = 1;
            max = element;
        } else {
            if (element.compareTo(max) == 0) {
                maxCount++;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min, max, total - (maxCount + minCount));
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}