package Kolokviumski.F1Trka;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class DateFormat implements Comparable<DateFormat> {
    private String minute;
    private String second;
    private String milisecond;

    public DateFormat(String line) {
        String[] parts = line.split(":");
        this.minute = parts[0];
        this.second = parts[1];
        this.milisecond = parts[2];
    }

    @Override
    public int compareTo(DateFormat o) {
        int finalresult = 0;
        int minutes = Integer.parseInt(this.minute);
        int seconds = Integer.parseInt(this.second);
        int miliseconds = Integer.parseInt(this.milisecond);
        int minuteO = Integer.parseInt(o.minute);
        int second0 = Integer.parseInt(o.second);
        int milisecond = Integer.parseInt(o.milisecond);
        if (minutes == minuteO && seconds == second0 && miliseconds == milisecond) {
            finalresult = 0;
        } else if (minutes < minuteO) {
            finalresult = 1;
        } else if (minutes > minuteO) {
            finalresult = -1;
        } else if (minutes == minuteO) {
            if (seconds < second0) {
                finalresult = 1;
            } else if (seconds > second0) {
                finalresult = -1;
            } else {
                if (miliseconds < milisecond) {
                    finalresult = 1;
                } else {
                    finalresult = -1;
                }
            }
        }
        return finalresult;
    }

    @Override
    public String toString() {
        return minute + ":" + second + ":" + milisecond;
    }
}

class F1RaceBest implements Comparable<F1RaceBest> {
    private String name;
    private DateFormat bestlap;

    public F1RaceBest(F1Race f1Race) {
        this.name = f1Race.getName();
        this.bestlap = f1Race.bestLap();
    }

    @Override
    public int compareTo(F1RaceBest o) {
        return bestlap.compareTo(o.bestlap);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, bestlap);
    }
}

class F1RaceList {
    public static List<F1Race> f1Races = new ArrayList<>();
    public static List<F1RaceBest> bests = new ArrayList<>();

    public static String print() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (int i = 0; i < bests.size(); i++) {
            sb.append(String.format("%d. %s", counter, bests.get(i).toString()));
            sb.append("\n");
            counter++;

        }
        return sb.toString();
    }
}

class F1Race implements Comparable<F1Race> {
    // vashiot kod ovde
    private String name;
    private List<DateFormat> laps;
    private DateFormat bestlap;

    public F1Race() {
        this.name = null;
        laps = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public F1Race(String line) {
        String[] parts = line.split("\\s+");
        this.name = parts[0];
        DateFormat lap1 = new DateFormat(parts[1]);
        DateFormat lap2 = new DateFormat(parts[2]);
        DateFormat lap3 = new DateFormat(parts[3]);
        this.laps = new ArrayList<>();
        this.laps.add(lap1);
        this.laps.add(lap2);
        this.laps.add(lap3);
    }

    public DateFormat bestLap() {
        return laps.stream().max(Comparator.naturalOrder()).get();
    }

    void readResults(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        F1RaceList.f1Races = bufferedReader.lines()
                .map(line -> new F1Race(line))
                .collect(Collectors.toList());
    }

    void printSorted(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        F1RaceList.bests = F1RaceList.f1Races.stream().map(f1Race -> new F1RaceBest(f1Race)).collect(Collectors.toList());
        F1RaceList.bests.sort(Comparator.reverseOrder());
        printWriter.println(F1RaceList.print());
        printWriter.flush();
    }

    @Override
    public String toString() {
        return String.format("%-10s%-10s", name, bestLap().toString());
    }

    @Override
    public int compareTo(F1Race o) {
        return name.compareTo(o.name);
    }
}