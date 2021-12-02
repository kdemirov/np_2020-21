package Kolokviumski2.Aerodromi;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.*;

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Flight implements Comparable<Flight> {
    private String from;
    private String to;
    private int time, duration, hours, minutes, hoursDuration, minutesDuration, durHours, durMinites;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
        setHoursAndMinutes();
        setHoursAndMinutesDuration();
        calculateFlightTime();
    }

    public int getTime() {
        return time;
    }

    private void calculateFlightTime() {
        hoursDuration = hours + durHours;
        minutesDuration = minutes + durMinites;
        if (hoursDuration >= 24) {
            hoursDuration = hoursDuration - 24;
        }
        if (minutesDuration >= 60) {
            minutesDuration = minutesDuration - 60;
            hoursDuration += 1;
            if (hoursDuration >= 24) {
                hoursDuration = hoursDuration - 24;
            }
        }
    }

    public String arrivedTime() {
        return String.format("%02d:%02d", hoursDuration, minutesDuration);
    }

    private void setHoursAndMinutesDuration() {
        this.durHours = duration / 60;
        this.durMinites = duration - durHours * 60;
    }

    public String durationTime() {
        int tempHours = hours + durHours;
        int tempMinutes = minutes + durMinites;
        if (tempHours >= 24 || tempHours == 23 && tempMinutes >= 60) {
            return String.format("+1d %dh%02dm", durHours, durMinites);
        }
        return String.format("%dh%02dm", durHours, durMinites);
    }

    @Override
    public String toString() {


        return String.format("%s-%s %s-%s %s", from, to, Time(), arrivedTime(), durationTime());
    }

    public void setHoursAndMinutes() {
        this.hours = time / 60;
        this.minutes = time - hours * 60;
    }

    private String Time() {
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public int compareTo(Flight o) {
        Comparator comparator = Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime);
        return comparator.compare(this, o);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}

class Airports {
    private Set<Airport> airports;
    private Map<String, Set<Flight>> flights;

    public Airports() {
        this.airports = new HashSet<>();
        this.flights = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        this.airports.add(new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        this.flights.putIfAbsent(from, new TreeSet<>());
        this.flights.get(from).add(new Flight(from, to, time, duration));
    }

    public void showFlightsFromAirport(String code) {
        final int[] count = {1};
        System.out.println(this.airports.stream().filter(a -> a.getCode().equals(code)).findFirst().get().toString());
        this.flights.get(code).forEach(f -> System.out.println(String.format("%d. %s", count[0]++, f)));

    }

    public void showDirectFlightsFromTo(String from, String to) {
        if (this.flights.get(from).stream().filter(f -> f.getTo().equals(to)).collect(Collectors.toList()).size() != 0) {
            this.flights.get(from).stream().filter(f -> f.getTo().equals(to)).forEach(f -> System.out.println(f));
        } else {
            System.out.println(String.format("No flights from %s to %s", from, to));
        }

    }

    public void showDirectFlightsTo(String to) {
        this.flights.values().stream().flatMap(f -> f.stream()).filter(f -> f.getTo().equalsIgnoreCase(to)).collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::getTime).thenComparing(Flight::getFrom)))).forEach(f -> System.out.println(f));
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

