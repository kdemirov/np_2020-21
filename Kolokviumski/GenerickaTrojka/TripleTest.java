package Kolokviumski.GenerickaTrojka;

import java.util.*;

class Triple<E extends Number & Comparable<E>> {

    private List<E> numbers;

    public Triple(E numberOne, E numberTwo, E numberThree) {
        numbers = new ArrayList<>();
        numbers.add(numberOne);
        numbers.add(numberTwo);
        numbers.add(numberThree);
    }

    public double max() {
        DoubleSummaryStatistics dsm = new DoubleSummaryStatistics();
        numbers.forEach(i -> dsm.accept(i.doubleValue()));
        return dsm.getMax();
    }

    public double average() {
        DoubleSummaryStatistics dsm = new DoubleSummaryStatistics();
        numbers.forEach(i -> dsm.accept(i.doubleValue()));
        return dsm.getSum() / numbers.size();
    }

    public void sort() {
        Collections.sort(numbers);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        numbers.forEach(e -> sb.append(String.format("%.2f ", e.doubleValue())));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


