package Kolokviumski2.MernaStanica;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


class WeatherDetails implements Comparable<WeatherDetails> {
    private float temperature;
    private float humidity;
    private float wind;
    private float visibility;
    private Date date;
    private static long TWO_HALF_MINUTES = 150000;

    public WeatherDetails(float temperature, float humidity, float wind, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date.toString());
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    public boolean isLessThanTwoAndHalfMinutes(Date date) {
        long timeInMiliSeconds = this.date.getTime();
        long timeInMiliSecondsCompared = date.getTime();
        if (Math.abs(timeInMiliSeconds - timeInMiliSecondsCompared) < TWO_HALF_MINUTES) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(WeatherDetails o) {
        return this.date.compareTo(o.date);
    }
}

class WeatherStation {
    private List<WeatherDetails> weatherDetails;
    private int days;

    public WeatherStation(int days) {
        this.weatherDetails = new ArrayList<>();
        this.days = days;
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        WeatherDetails wd = new WeatherDetails(temperature, humidity, wind, visibility, date);
        if (weatherDetails.stream().noneMatch(w -> w.isLessThanTwoAndHalfMinutes(date))) {
            weatherDetails.add(wd);
        }
        weatherDetails.removeIf(w -> isLessThanXDays(w.getDate(), date));
    }

    private boolean isLessThanXDays(Date date, Date dateCompared) {
        long days = ChronoUnit.DAYS.between(date.toInstant(), dateCompared.toInstant());
        if (days >= this.days) {
            return true;
        }
        return false;
    }

    public int total() {
        return this.weatherDetails.size();
    }

    public void status(Date from, Date to) {
        List<WeatherDetails> wd = this.weatherDetails.stream()
                .filter(w -> w.getDate().after(from) && w.getDate().before(to) || w.getDate().equals(from) || w.getDate().equals(to))
                .sorted()
                .collect(Collectors.toList());
        if (wd.size() == 0) {
            throw new RuntimeException();
        }
        wd.forEach(System.out::println);
        DoubleSummaryStatistics dss = wd.stream().mapToDouble(w -> w.getTemperature()).summaryStatistics();
        System.out.println(String.format("Average temperature: %.2f", dss.getAverage()));
    }
}


public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde
