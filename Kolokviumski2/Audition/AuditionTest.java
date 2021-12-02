package Kolokviumski2.Audition;

import java.util.*;

class Participant implements Comparable<Participant> {
    private String code;
    private String name;
    private int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int compareTo(Participant o) {
        Comparator comparator = Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode);
        return comparator.compare(this, o);
    }
}

class Audition {
    private Map<String, Set<Participant>> participantsByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        this.participantsByCity.putIfAbsent(city, new HashSet<>());
        this.participantsByCity.get(city).add(new Participant(code, name, age));
    }

    public void listByCity(String city) {
        this.participantsByCity.get(city)
                .stream().sorted()
                .forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
