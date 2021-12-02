package Kolokviumski.Dropki;

import java.util.Scanner;

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}

class GenericFraction<T extends Number, U extends Number> {
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        this.numerator = numerator;
        if (denominator.intValue() == 0) {
            throw new ZeroDenominatorException();
        }
        this.denominator = denominator;
    }

    private int lcm(int number1, int number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }

        int absNumber1 = Math.abs(number1);
        int absNumber2 = Math.abs(number2);
        int absHigherNumber = Math.max(absNumber1, absNumber2);
        int absLowerNumber = Math.min(absNumber1, absNumber2);
        int lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    public GenericFraction() {
        this.denominator = null;
        this.numerator = null;
    }

    public void setNumerator(T numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(U denominator) {
        this.denominator = denominator;
    }

    public T getNumerator() {
        return numerator;
    }

    public U getDenominator() {
        return denominator;
    }

    public int gcd(int number1, int number2) {
        return number2 == 0 ? number1 : gcd(number2, number1 % number2);
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        Double numerator = null;
        Double denomirator = null;
        if (this.denominator.intValue() == gf.denominator.intValue()) {
            numerator = this.numerator.doubleValue() + gf.numerator.doubleValue();
            denomirator = this.denominator.doubleValue();

        } else {
            int lcm = lcm(this.denominator.intValue(), gf.getDenominator().intValue());
            denomirator = (double) lcm;
            numerator = this.numerator.doubleValue() * (denomirator / this.denominator.doubleValue()) + gf.getNumerator().doubleValue() * (lcm / gf.getDenominator().doubleValue());

        }
        int gcd = gcd(numerator.intValue(), denomirator.intValue());
        numerator = numerator / gcd;
        denomirator = denomirator / gcd;
        GenericFraction<Double, Double> newFraction = new GenericFraction<>(numerator, denomirator);
        return newFraction;
    }

    public double toDouble() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f", numerator.doubleValue(), denominator.doubleValue());
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
