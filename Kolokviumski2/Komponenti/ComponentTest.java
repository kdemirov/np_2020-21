package Kolokviumski2.Komponenti;

import java.util.*;
import java.util.stream.IntStream;

class Component implements Comparable<Component> {
    private String color;
    private int weight;
    private Set<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new TreeSet<>();
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void changeColor(int weight, String color) {
        if (this.weight < weight) this.color = color;
        this.components.forEach(c -> c.changeColor(weight, color));
    }

    public Set<Component> getComponents() {
        return components;
    }

    @Override
    public int compareTo(Component o) {
        Comparator comparator = Comparator.comparing(Component::getWeight).thenComparing(Component::getColor);
        return comparator.compare(this, o);
    }


    public String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, indent).forEach(i -> sb.append("---"));
        sb.append(String.format("%d:%s\n", weight, color));
        components.stream().forEach(c -> sb.append(c.toString(indent + 1)));
        return sb.toString();
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, already taken!", position));
    }
}

class Window {
    private String name;
    private Map<Integer, List<Component>> map;
    private static int INDENT = 0;

    public Window(String name) {
        this.name = name;
        map = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (!map.containsKey(position)) {
            map.putIfAbsent(position, new ArrayList<>());
            map.get(position).add(component);
        } else {
            throw new InvalidPositionException(position);
        }
    }

    public void changeColor(int weight, String color) {
        map.values().stream()
                .flatMap(c -> c.stream())
                .forEach(c -> c.changeColor(weight, color));
    }

    public void swichComponents(int pos1, int pos2) {
        List<Component> temp = map.get(pos1);
        map.put(pos1, map.get(pos2));
        map.put(pos2, temp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WINDOW %s\n", name));
        for (Map.Entry<Integer, List<Component>> entry : map.entrySet()) {
            sb.append(entry.getKey() + ":");
            for (Component list : entry.getValue()) {
                sb.append(String.format("%s", list.toString(INDENT)));


            }

        }

        return sb.toString();
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
