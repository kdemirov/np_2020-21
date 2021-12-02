package Kolokviumski.DnevniTemperaturi;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Temperature {
    private int value;

    public Temperature(int value) {
        this.value = value;
    }

    abstract double getCelzius();

    abstract double getFarenheit();

    public int getValue() {
        return value;
    }

    public static Temperature getTemperature(String part) {
        char scale = part.charAt(part.length() - 1);
        switch (scale) {
            case 'C':
                return new CTemperature(Integer.parseInt(part.substring(0, part.length() - 1)));
            case 'F':
                return new FTemperature(Integer.parseInt(part.substring(0, part.length() - 1)));
            default:
                return null;
        }
    }

}

class CTemperature extends Temperature {

    public CTemperature(int value) {
        super(value);
    }

    @Override
    double getCelzius() {
        return getValue();
    }

    @Override
    double getFarenheit() {
        return ((getValue() * 9.0) / 5.0) + 32;
    }
}

class FTemperature extends Temperature {

    public FTemperature(int value) {
        super(value);
    }

    @Override
    double getCelzius() {
        return ((getValue() - 32) * 5.0) / 9.0;
    }

    @Override
    double getFarenheit() {
        return getValue();
    }
}

class DailyMeasurment implements Comparable<DailyMeasurment> {
    int day;
    List<Temperature> temperatures;

    public DailyMeasurment(int day, List<Temperature> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
    }

    public static DailyMeasurment getDailyMeasurment(String line) {
        String[] parts = line.split("\\s+");
        int year = Integer.parseInt(parts[0]);
        List<Temperature> list = Arrays.stream(parts).skip(1).map(part -> Temperature.getTemperature(part)).collect(Collectors.toList());
        return new DailyMeasurment(year, list);
    }

    @Override
    public int compareTo(DailyMeasurment o) {
        return Integer.compare(day, o.day);
    }


    public String toString(char scale) {
        DoubleSummaryStatistics dss = temperatures.stream().mapToDouble(t -> {
            if (scale == 'C') {
                return t.getCelzius();
            } else {
                return t.getFarenheit();
            }
        }).summaryStatistics();
        return String.format("%3d: Count:%3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c", day, dss.getCount(),
                dss.getMin(),
                scale,
                dss.getMax(),
                scale,
                dss.getAverage(),
                scale);
    }
}

class DailyTemperatures {
    private List<DailyMeasurment> dailyMeasurments;

    public DailyTemperatures() {
        dailyMeasurments = new ArrayList<>();
    }

    public void readTemperatures(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        this.dailyMeasurments = br.lines().map(line -> DailyMeasurment.getDailyMeasurment(line)).collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);
        dailyMeasurments.sort(Comparator.naturalOrder());
        dailyMeasurments.forEach(d -> pw.println(d.toString(scale)));
        pw.flush();

    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}