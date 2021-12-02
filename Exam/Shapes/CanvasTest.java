package Exam.Shapes;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;

class InvalidDimensionException extends Exception{
    public InvalidDimensionException(double x){
        super(String.format("Dimension %d is not allowed!",0));
    }

}
abstract class Shape {
    private double x;
    private User user;

    public Shape(double x,User user) throws InvalidDimensionException {
        if(x<=0.0){
            throw new InvalidDimensionException(x);
        }
        this.x=x;
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public static Shape factoryShape(String line) throws InvalidIdException, InvalidDimensionException {
        String[] parts=line.split("\\s+");
        int number=Integer.parseInt(parts[0]);
        switch (number){
            case 1:
                return new Circle(Double.parseDouble(parts[2]),new User(parts[1]));
            case 2:
                return new Square(Double.parseDouble(parts[2]),new User(parts[1]));
            case 3:
                return new Rectangle(Double.parseDouble(parts[2]),new User(parts[1]),Double.parseDouble(parts[3]));
            default:
                return null;
        }
    }
    abstract double getArea();
    abstract double getPerimeter();
    abstract void scaleShape(double coef);
}
class Circle extends Shape {

    public Circle(double x,User user) throws InvalidDimensionException {
        super(x,user);
    }

    @Override
    double getArea() {
        return Math.PI*getX()*getX();
    }


    @Override
    double getPerimeter() {
        return 2*Math.PI*getX();
    }

    @Override
    void scaleShape(double coef) {
        setX(getX()*coef);
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",getX(),getArea(),getPerimeter());
    }
}
class Square extends Shape{

    public Square(double x,User user) throws InvalidDimensionException {
        super(x,user);
    }

    @Override
    double getArea() {
        return getX()*getX();
    }

    @Override
    double getPerimeter() {
        return 4*getX();
    }

    @Override
    void scaleShape(double coef) {
        setX(getX()*coef);
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",getX(),getArea(),getPerimeter());
    }
}
class Rectangle extends Shape{

    private double y;
    public Rectangle(double x,User user,double y) throws InvalidDimensionException {
        super(x,user);
        if(y<=0.0){
            throw new InvalidDimensionException(y);
        }

        this.y=y;
    }

    @Override
    double getArea() {
        return getX()*y;
    }

    @Override
    double getPerimeter() {
        return (2*getX())+(2*y);
    }

    @Override
    void scaleShape(double coef) {
        setX(getX()*coef);
        y=y*coef;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",getX(),y,getArea(),getPerimeter());
    }
}
class InvalidIdException extends Exception{
    public InvalidIdException(String message){
        super(String.format("ID %s is not valid",message));
    }
}
class User{
    private String id;

    public User(String id) throws InvalidIdException {
        if(!isValid(id)){
            throw new InvalidIdException(id);
        }
        this.id=id;
    }

    public String getId() {
        return id;
    }

    private boolean isValid(String id) {
        return id.length()==6&&id.chars().allMatch(c->c>='a'&&c<='z'||c>='A'&&c<='Z'||c>'0'&&c<'9');
    }
}
class Canvas{
    private Set<Shape> shapes;
    private Map<String,Set<Shape>> shapesByUser;


    public Canvas(){
        this.shapes=new TreeSet<>(Comparator.comparing(Shape::getArea));
        this.shapesByUser=new HashMap<>();

    }
    public void readShapes(InputStream is) {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        List<String> lines =br.lines().collect(Collectors.toList());
        for(String line:lines){
            try {
                this.shapes.add(Shape.factoryShape(line));
            } catch (InvalidIdException | InvalidDimensionException e) {
                System.out.println(e.getMessage());
                if(e instanceof InvalidDimensionException){
                    break;
                }
            }
        }
        this.shapesByUser=this.shapes
                .stream()
                .collect(Collectors.groupingBy(s->s.getUser().getId(),
                        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Shape::getPerimeter)))));


    }
    public void scaleShapes(String userID,double coef){
        this.shapes.stream()
                .filter(s->s.getUser().getId().equals(userID))
                .forEach(s->s.scaleShape(coef));
    }
    public void printAllShapes(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        this.shapes.forEach(s->pw.println(s));
        pw.flush();
    }
    public void statistics(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        DoubleSummaryStatistics dss=this.shapes.stream().mapToDouble(Shape::getArea).summaryStatistics();
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("count: %d\n",dss.getCount()));
        sb.append(String.format("sum: %.2f\n",dss.getSum()));
        sb.append(String.format("min: %.2f\n",dss.getMin()));
        sb.append(String.format("average: %.2f\n",dss.getAverage()));
        sb.append(String.format("max: %.2f",dss.getMax()));
        pw.print(sb.toString());
        pw.flush();
    }
    public void printByUserId(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        Comparator<Map.Entry<String,Set<Shape>>> comparator=new Comparator<Map.Entry<String, Set<Shape>>>() {
            @Override
            public int compare(Map.Entry<String, Set<Shape>> o1, Map.Entry<String, Set<Shape>> o2) {
                int result=Integer.compare(o2.getValue().size(),o1.getValue().size());
                double sumAreaO1=o1.getValue()
                        .stream()
                        .mapToDouble(s->s.getArea())
                        .sum();
                double sumAreaO2=o2.getValue()
                        .stream()
                        .mapToDouble(s->s.getArea())
                        .sum();
                if(result==0){
                    return Double.compare(sumAreaO1,sumAreaO2);
                }
                return result;
            }
        };
         this.shapesByUser
                .entrySet()
                .stream()
                .sorted(comparator)
                 .map(entry->String.format("Shapes of user: %s\n%s",entry.getKey(),
                         entry.getValue()
                        .stream().map(s->s.toString())
                        .collect(Collectors.joining("\n"))))
                .forEach(System.out::println);

    }

}
public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}