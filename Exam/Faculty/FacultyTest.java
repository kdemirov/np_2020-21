package Exam.Faculty;



import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Course implements Comparable<Course>{
    private String name;
    private List<Student> allStudents;
    public Course(String name){
        this.name=name;
        this.allStudents=new ArrayList<>();
    }
    public void addStudent(Student student){
        this.allStudents.add(student);
    }
    public double averageGrade(){
        return this.allStudents
                .stream()
                .mapToDouble(Student::averageGrade)
                .average()
                .getAsDouble();
    }
    public int countListeners(){
        return this.allStudents.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
    public String printCourses(){
        return String.format("%s %d %.2f",name,countListeners(),averageGrade());
    }

    @Override
    public int compareTo(Course o) {
        return this.name.compareTo(o.name);
    }
}
class Term implements Comparable<Term>{
    private int term;
    private Map<Course,Integer> coursesWithGrades;

    public Term(int term){
        this.term=term;
        this.coursesWithGrades=new HashMap<>();
    }
    public void addCourseWithGrade(Course course,int grade){
        this.coursesWithGrades.putIfAbsent(course,grade);
    }
    public double averageGrade(){
         return this.coursesWithGrades.values()
                .stream()
                .mapToInt(i->i)
                .average()
                 .getAsDouble();
    }

    public Map<Course, Integer> getCoursesWithGrades() {
        return coursesWithGrades;
    }
    public int getGradesCount(){
        return (int)this.getCoursesWithGrades().values().stream().mapToInt(i->i).count();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Term %d:\n",term));
        sb.append(String.format("Courses for term: %d\n",this.coursesWithGrades.keySet().size()));
        sb.append(String.format("Average grade for term: %.2f",averageGrade()));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term1 = (Term) o;
        return term == term1.term;
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }

    @Override
    public int compareTo(Term o) {
        return Integer.compare(this.term,o.term);
    }
}
class OperationNotAllowedException extends Exception{
    public OperationNotAllowedException(int term,String index){
        super(String.format("Term %d is not possible for student with ID %s",term,index));
    }
    public OperationNotAllowedException(String index,int term){
        super(String.format("Student %s already has 3 grades in term %d",index,term));
    }
}
class Student implements Comparable<Student>{
    private String index;
    private int yearOfStudies;
    private Map<Integer,Term> terms;
    private Set<Course> allCourses;

    public Student(String index,int yearOfStudies){
        this.index=index;
        this.yearOfStudies=yearOfStudies;
        this.terms=new TreeMap<>();
        this.allCourses=new TreeSet<>();
    }
    public void addTerm(int term,String courseName,int grade) throws OperationNotAllowedException {

        if(this.yearOfStudies==3&&term>6){
            throw new OperationNotAllowedException(term,this.index);
        }
        if(this.yearOfStudies==4&&term>8){
            throw new OperationNotAllowedException(term,this.index);
        }
        this.terms.putIfAbsent(term,new Term(term));
        if(this.terms.get(term).getGradesCount()>3){
            throw new OperationNotAllowedException(this.index,term);
        }else{
            this.terms.get(term).addCourseWithGrade(new Course(courseName),grade);
            this.allCourses.add(new Course(courseName));
        }

    }
    public int getCoursePassed(){
        return (int)this.terms.values()
                .stream().flatMap(t->t.getCoursesWithGrades().values().stream())
                .filter(t->t>=6)
                .count();
    }
    public double averageGrade(){
        OptionalDouble optionalDouble=
                this.terms.values()
                        .stream()
                        .flatMap(t->t.getCoursesWithGrades().values().stream())
                        .mapToInt(i->i)
                        .average();


        if(optionalDouble.isPresent()){
            return optionalDouble.getAsDouble();
        }else
        {
            return 5.00;
        }
    }
   public boolean isGraduated(){
        int passedCourses=getCoursePassed();
        if(this.yearOfStudies==3){
            return passedCourses==18;
        }else{
            return passedCourses==24;
        }
    }
    public String graduatedPrint(){
        return String.format("Student with ID %s graduated with average grade %.2f in %d years.",index,averageGrade(),yearOfStudies);
    }
    public String firstNStudentsPrint(){
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",index,getCoursePassed(),averageGrade());
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Student: %s\n",index));
        this.terms.values().forEach(t->sb.append(t+"\n"));
        sb.append(String.format("Average grade: %.2f\n",averageGrade()));
        sb.append(String.format("Courses attended: %s",
                this.allCourses
                .stream()
                .map(Course::toString)
                .collect(Collectors.joining(","))));
        return sb.toString();
    }

    @Override
    public int compareTo(Student o) {
        Comparator comparator=Comparator.comparing(Student::getCoursePassed).thenComparing(Student::averageGrade).reversed();
        return comparator.compare(this,o);
    }
}
class Faculty {
    private Map<String,Student> studentsById;
    private List<Student> graduatedStudens;
    private Set<Student> firstNStudents;
    private Map<String,Course> coursesWithStudents;

    public Faculty() {
        this.studentsById=new HashMap<>();
        this.graduatedStudens=new ArrayList<>();
        this.firstNStudents=new TreeSet<>(Comparator.comparing(Student::getCoursePassed).thenComparing(Student::averageGrade));
        this.coursesWithStudents=new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        this.studentsById.putIfAbsent(id,new Student(id,yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        this.studentsById.get(studentId).addTerm(term, courseName, grade);
        Student student=this.studentsById.get(studentId);
        if(student.isGraduated()){
            this.graduatedStudens.add(student);
            this.studentsById.remove(studentId);
        }
        this.coursesWithStudents.putIfAbsent(courseName,new Course(courseName));
        this.coursesWithStudents.get(courseName).addStudent(student);

    }

    String getFacultyLogs() {
       return this.graduatedStudens
                .stream()
                .map(Student::graduatedPrint)
                .collect(Collectors.joining("\n"));

    }

    String getDetailedReportForStudent(String id) {
        return this.studentsById.get(id).toString();
    }

    void printFirstNStudents(int n) {
        this.firstNStudents= new TreeSet<>(this.studentsById.values());
        this.firstNStudents
                .stream()
                .limit(n)
                .map(Student::firstNStudentsPrint)
                .forEach(System.out::println);
    }

    void printCourses() {
        TreeSet<Course> courses=new TreeSet<>(Comparator.comparing(Course::countListeners).thenComparing(Course::averageGrade));
        courses= new TreeSet<>(this.coursesWithStudents
                .values());
        courses
                .stream()
                .map(Course::printCourses)
                .forEach(System.out::println);
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
