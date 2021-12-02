package Exam.LabaratoriskiVezbi;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Student{
    private String index;
    private List<Integer> labPoints;

    public Student(String index,List<Integer> points){
        this.index=index;
        this.labPoints=points;
    }
    public double averagePoints(){
        int sum=this.labPoints.stream()
                .mapToInt(p->p)
                .sum();
        return sum/10.0;
    }
    public boolean isFailed(){
        return this.labPoints.size()<8;
    }

    public String getIndex() {
        return index;
    }
    public int getYearOfStuding(){
        int tfd=Integer.parseInt(index.substring(1,2));
        switch (tfd){
            case 8:
                return 2;
            case 7:
                return 3;
            case 6:
                return 4;
            case 5:
                return 5;
            case 4:
                return 6;
            case 3:
                return 7;
            default:
        return 0;
        }
    }
    @Override
    public String toString() {
        return String.format("%s %s %.2f",index,isFailed()?"NO":"YES",averagePoints());
    }
}
class LabExercises{
    private List<Student> students;

    public LabExercises(){
        this.students=new ArrayList<>();
    }
    public void addStudent(Student student){
        this.students.add(student);
    }
    public void printByAveragePoints(boolean ascending,int n){
        Comparator comparator=Comparator.comparing(Student::averagePoints).thenComparing(Student::getIndex);
        Stream<Student> studentStream=this.students.stream();
        if(ascending){

                    studentStream
                    .sorted(comparator)
                    .limit(n)
                    .forEach(System.out::println);
        }else{
                    studentStream
                    .sorted(comparator.reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }

    }
    public List<Student> failedStudents(){
        return this.students
                .stream()
                .filter(Student::isFailed)
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::averagePoints))
                .collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        List<Student> passedStudents;
        passedStudents = this.students
                .stream()
                .filter(s->!s.isFailed())
                .collect(Collectors.toList());
        return passedStudents.stream().collect(Collectors.groupingBy(
                Student::getYearOfStuding,
                Collectors.averagingDouble(Student::averagePoints)
        ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}