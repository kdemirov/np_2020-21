package Exam.GenerickiKlaster;

import java.util.*;

interface Point{
    double calculateDistance(Point a,Point b);
    void setDistanceFromNearElement(double distance);
    double getDistanceFromNearElement();
    long getId();
    float getX();
    float getY();
}
class Cluster<T extends Point>{
    private List<T> allItems;

    public Cluster(){
        this.allItems=new ArrayList<>();
    }
    public void addItem(T element){
        this.allItems.add(element);
    }
    public void near(long id,int top){
        T element=this.allItems
                .stream()
                .filter(e->e.getId()==id)
                .findFirst().get();
        final int[] count = {1};
        this.allItems.forEach(e->e.setDistanceFromNearElement(e.calculateDistance(element,e)));
        this.allItems
                .stream().filter(e->e.getDistanceFromNearElement()>0)
                .sorted(Comparator.comparing(T::getDistanceFromNearElement))
                .limit(top)
                .forEach(e->System.out.println(String.format("%d.%3s", count[0]++,e)));
    }
}
class Point2D implements Point{
    private long id;
    private float x;
    private float y;
    private double distanceFromNearElement;

    public Point2D(long id,float x,float y){
        this.id=id;
        this.x=x;
        this.y=y;
    }

    @Override
    public double calculateDistance(Point a, Point b) {
        float powx=(a.getX()-b.getX())*(a.getX()-b.getX());
        float powy=(a.getY()-b.getY())*(a.getY()-b.getY());
        return Math.sqrt(powx+powy);
    }

    @Override
    public void setDistanceFromNearElement(double distance) {
        this.distanceFromNearElement=distance;
    }

    @Override
    public double getDistanceFromNearElement() {
        return this.distanceFromNearElement;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("%d -> %.3f",id,distanceFromNearElement);
    }
}
/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here