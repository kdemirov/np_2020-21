package Kolokviumski.mernastanica;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WeatherCondition implements Comparable<WeatherCondition> {
    private float temperature;
    private float humidity;
    private float wind;
    private float visibility;
    private Date date;

    public WeatherCondition(float temperature, float humidity, float wind, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    public boolean isLessThanTwoAndAHalfMinutes(Date d) {
        long timeInMiliSeconds = this.date.getTime();
        long timeInMiliSecondsCompared = d.getTime();
        if (Math.abs(timeInMiliSeconds - timeInMiliSecondsCompared) < 150000) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%c %.1f km %s", temperature, wind, humidity, '%', visibility, date.toString());
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(WeatherCondition o) {
        return this.date.compareTo(o.date);
    }

    public float getTemperature() {
        return temperature;
    }
}

class WeatherStation {
    private List<WeatherCondition> weatherConditions;
    private int x;

    public WeatherStation(int x) {
        this.x = x;
        this.weatherConditions = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        boolean flag = false;
        if (weatherConditions.size() != 0) {
            for (WeatherCondition w : weatherConditions) {
                if (!w.isLessThanTwoAndAHalfMinutes(date)) {
                    flag = true;
                }
            }
        } else {
            weatherConditions.add(new WeatherCondition(temperature, humidity, wind, visibility, date));
            flag = true;
        }
        if (!flag) {
            weatherConditions.add(new WeatherCondition(temperature, humidity, wind, visibility, date));
        }
        weatherConditions.removeIf(w -> isLessThanXDays(w.getDate(), date));

    }

    private boolean isLessThanXDays(Date date, Date dateCompared) {
        long days = ChronoUnit.DAYS.between(date.toInstant(), dateCompared.toInstant());
        if (days >= x) return true;
        return false;
    }


    public float averageTemperature(List<WeatherCondition> weatherConditions) {
        float average = 0;
        for (WeatherCondition w : weatherConditions) {
            average += w.getTemperature();
        }
        return average / weatherConditions.size();
    }

    public int total() {
        return weatherConditions.size();
    }

    public void status(Date from, Date to) {
        List<WeatherCondition> newWeatherStation = new ArrayList<>();

        newWeatherStation = weatherConditions.stream()
                .filter(w -> w.getDate().after(from) && w.getDate().before(to) || w.getDate().equals(from) || w.getDate().equals(to))
                .collect(Collectors.toList());
        newWeatherStation.sort(Comparator.naturalOrder());
        if (newWeatherStation.size() == 0) {
            throw new RuntimeException();
        } else {
            newWeatherStation.forEach(w -> System.out.println(w));
            System.out.println(String.format("Average temperature: %.2f", averageTemperature(newWeatherStation)));
        }

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