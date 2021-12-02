package Kolokviumski.StackedCanvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weigth();
}

abstract class Shape implements Scalable, Stackable {
    private String id;
    private Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public abstract float weigth();

    public abstract void scale(float scaleFactor);

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Objects.equals(id, shape.id) &&
                color == shape.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color);
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

class Circle extends Shape {

    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public float weigth() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius = this.radius * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", getId(), getColor(), weigth());
    }
}

class Rectangle extends Shape {
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public float weigth() {
        return width * height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width = this.width * scaleFactor;
        this.height = this.height * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", getId(), getColor(), weigth());
    }
}

class Canvas {
    private List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        Circle circle = new Circle(id, color, radius);
        addShape(circle);
    }

    public void addShape(Shape shape) {
        if (shapes.size() > 0) {
            int i;
            for (i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).weigth() < shape.weigth())
                    break;
            }
            shapes.add(i, shape);
        } else {
            shapes.add(shape);
        }
    }

    public void add(String id, Color color, float width, float height) {
        Rectangle rectangle = new Rectangle(id, color, width, height);
        addShape(rectangle);
    }

    public void scale(String id, float scaleFactor) {
        Shape shape = shapes.stream().filter(s -> s.getId().equals(id)).findFirst().get();
        shapes.removeIf(s -> s.equals(shape));
        shape.scale(scaleFactor);
        addShape(shape);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        shapes.forEach(s -> sb.append(s.toString()));

        return sb.toString();
    }
}