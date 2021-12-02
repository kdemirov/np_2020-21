package Kolokviumski2.KalendarNaNastani;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Event implements Comparable<Event> {
    private String name;
    private String location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Event o) {
        Comparator comparator = Comparator.comparing(Event::getDate).thenComparing(Event::getName);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
        return String.format("%s at %s, %s", df.format(getDate()), getLocation(), getName());
    }
}

class PrintDateUTC {
    public static String printDateUtc(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(d);
    }
}

class WrongDateException extends Exception {
    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", PrintDateUTC.printDateUtc(date)));
    }
}

class EventCalendar {
    private Map<Date, TreeSet<Event>> events;
    private Map<Integer, List<Event>> mapByMonth;
    private int year;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
        mapByMonth = new TreeMap<>();
    }

    public void addEvent(String name, String Location, Date date) throws WrongDateException {
        if (date.getYear() != year - 1900) {
            throw new WrongDateException(date);
        }
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date dateKey = Date.from(dayInst);
        this.events.putIfAbsent(dateKey, new TreeSet<>());
        this.mapByMonth.putIfAbsent(dateKey.getMonth() + 1, new ArrayList<>());
        this.events.get(dateKey).add(new Event(name, Location, date));
        this.mapByMonth.get(dateKey.getMonth() + 1).add(new Event(name, Location, date));
    }

    public void listEvents(Date date) {
        if (this.events.containsKey(date)) {
            this.events.get(date).stream().forEach(t -> System.out.println(t));
        } else {
            System.out.println("No events on this day!");
        }
    }

    public void listByMonth() {
//        for(int i=1;i<=12;i++){
//            if(!mapByMonth.containsKey(i)){
//                mapByMonth.put(i,new ArrayList<>());
//            }
//        }

        IntStream.range(1, 12).filter(i -> !mapByMonth.containsKey(i)).forEach(i -> mapByMonth.put(i, new ArrayList<>()));
        mapByMonth.forEach((k, v) -> System.out.println(String.format("%d : %d", k, v.size())));
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde