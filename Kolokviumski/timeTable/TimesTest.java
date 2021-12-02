package Kolokviumski.timeTable;

import java.io.*;
import java.security.spec.ECFieldF2m;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String c) {
        super(String.format("%s", c));
    }

}

class InvalidTimeException extends Exception {
    public InvalidTimeException(Time t) {
        super(String.format("%s", t.print24HoursFormat()));
    }

    public InvalidTimeException(String line) {
        super(line);
    }
}

class Time implements Comparable<Time> {
    private int hours;
    private int minutes;
    private String hour;
    private String minute;


    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.hour = null;
        this.minute = null;
    }

    private void setStringHoursAndMinutes() {

        if (hours >= 10 && minutes >= 10) {
            hour = hours + "";
            minute = minutes + "";
        } else if (hours >= 10 && minutes < 10) {
            hour = hours + "";
            minute = "0" + minutes;
        } else if (hours < 10 && minutes >= 10) {
            hour = " " + hours;
            minute = minutes + "";
        } else if (hours < 10 && minutes < 10) {
            hour = " " + hours;
            minute = "0" + minutes;
        }
    }

    public String print24HoursFormat() {
        setStringHoursAndMinutes();
        return String.format("%s:%s\n", hour, minute);
    }

    public String printPmAmHoursFormat() {

        String timeFormat = null;
        if (hours == 0 && minutes <= 59) {
            hours = 12;
            timeFormat = "AM";
        } else if (hours >= 1 && hours <= 11) {
            timeFormat = "AM";
        } else if (hours == 12 && minutes <= 59) {
            timeFormat = "PM";
        } else if (hours >= 13 && hours <= 23) {
            hours = hours - 12;
            timeFormat = "PM";
        }
        setStringHoursAndMinutes();
        return String.format("%s:%s %s\n", hour, minute, timeFormat);
    }

    @Override
    public int compareTo(Time o) {
        if (this.hours == o.hours) {
            return Integer.compare(this.minutes, o.minutes);
        }
        return Integer.compare(this.hours, o.hours);
    }
}

class TimeTable {
    private List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    private char findIndex(String line) {
        int i;
        for (i = 0; i < line.length(); i++) {
            if (!Character.isDigit(line.charAt(i))) {
                break;
            }
        }
        return line.charAt(i);
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();


            String[] parts = line.split("\\s+");
            for (int i = 0; i < parts.length; i++) {
                String[] part = parts[i].split("\\W");
                Character indexOfSeparator = findIndex(parts[i]);
                if (!(indexOfSeparator.equals(':') || indexOfSeparator.equals('.'))) {
                    throw new UnsupportedFormatException(parts[i]);
                }
                int hour = Integer.parseInt(part[0]);
                int minute = Integer.parseInt(part[1]);
                if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                    throw new InvalidTimeException(new Time(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                }
                Time time = new Time(Integer.parseInt(part[0]), Integer.parseInt(part[1]));
                times.add(time);
            }
        }
    }

    public void writeTimes(OutputStream out, TimeFormat format24) {
        PrintWriter printWriter = new PrintWriter(out);
        StringBuilder sb = new StringBuilder();
        Collections.sort(times);
        switch (format24) {
            case FORMAT_24:
                times.forEach(t -> sb.append(t.print24HoursFormat()));
                break;
            case FORMAT_AMPM:
                times.forEach(t -> sb.append(t.printPmAmHoursFormat()));
        }
        sb.deleteCharAt(sb.length() - 1);
        printWriter.println(sb.toString());
        printWriter.flush();
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
