package Exam.StudentskiDosieta;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * January 2016 Exam problem 1
 */
class Record implements Comparable<Record>{
    private String code;
    private List<Integer> grades;
    public Record(String code){
        this.code=code;
        this.grades=new ArrayList<>();
    }
    public Record(String code ,List<Integer> grades){
        this.code=code;
        this.grades=grades;
    }
    public static Record getRecord(String[] parts) {
        String tmpCode=parts[0];
         List<Integer> tmpGrades = Arrays.stream(parts).skip(2)
                 .map(Integer::parseInt)
                 .collect(Collectors.toList());
         return new Record(tmpCode,tmpGrades);
    }

    public List<Integer> getGrades() {
        return grades;
    }

    private double getAvg(){
        return  this.grades.stream().mapToDouble(g->g).average().getAsDouble();
    }
    @Override
    public String toString() {
        return String.format("%s %.2f",code,
                getAvg());
    }

    public String getCode() {
        return code;
    }

    @Override
    public int compareTo(Record o) {
        Comparator comparator=Comparator.comparing(Record::getAvg).reversed().thenComparing(Record::getCode);
        return comparator.compare(this,o);
    }
}
class DirectionWithGrades implements Comparable<DirectionWithGrades>{
    private String direction;
    int numberOfTen;
    private List<Integer> grades;

    public DirectionWithGrades(String direction,List<Integer> grades){
        this.direction=direction;
        this.grades=grades;
        this.numberOfTen=getCountGrade(grades,10);
    }

    private int getCountGrade(List<Integer> grades,Integer grade){
            return (int)grades.stream()
                    .filter(g->g.equals(grade))
                    .count();
    }
    private String getAsterixCount(List<Integer> grades,Integer grade){
        int count=getCountGrade(grades,grade);
        StringBuilder stringBuilder=new StringBuilder();
        IntStream.range(0,  (count/10)).forEach(i->stringBuilder.append("*"));
        if(count%10!=0){
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s\n",direction));
        IntStream.rangeClosed(6,10).forEach(i->sb.append(String.format("%2d | %s(%d)\n",i,getAsterixCount(this.grades,i),getCountGrade(this.grades,i))));
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public int getNumberOfTen() {
        return numberOfTen;
    }

    @Override
    public int compareTo(DirectionWithGrades o) {
       Comparator comparator= Comparator.comparing(DirectionWithGrades::getNumberOfTen).reversed();
       return comparator.compare(this,o);
    }
}
class StudentRecords{
    private Map<String,Set<Record>> recordsByDir;
    private Set<DirectionWithGrades> directionsWithGrades;

    public StudentRecords(){
        this.recordsByDir=new TreeMap<>();
        this.directionsWithGrades=new TreeSet<>();
    }
    public int readRecords(InputStream inputStream){
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines=br.lines().collect(Collectors.toList());
        lines.forEach(this::fillMap);
        fillSet(this.recordsByDir);
        return lines.size();
    }

    private void fillSet(Map<String, Set<Record>> recordsByDir) {
        Map<String,List<Integer>> tmpMap=
                recordsByDir.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry->entry.getValue()
                        .stream()
                        .flatMap(rec->rec.getGrades().stream())
                        .collect(Collectors.toList())
                ));
        this.directionsWithGrades=tmpMap.entrySet().stream()
                .map(entry->new DirectionWithGrades(entry.getKey(),entry.getValue()))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void writeTable(OutputStream outputStream){
        PrintWriter pw=new PrintWriter(outputStream);
        pw.print(toString());
        pw.flush();
    }
    public void writeDistribution(OutputStream outputStream){
        PrintWriter pw=new PrintWriter(outputStream);
        this.directionsWithGrades.forEach(dir->pw.println(dir));
       pw.flush();


    }


    private void fillMap(String line){
        String[] parts=line.split("\\s+");
        this.recordsByDir.putIfAbsent(parts[1],new TreeSet<>());
        this.recordsByDir.get(parts[1]).add(Record.getRecord(parts));

    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        this.recordsByDir.forEach((k,v)->sb.append(String.format("%s\n%s\n",
                k,v.stream()
                        .map(Record::toString)
                        .collect(Collectors.joining("\n")))));
        return sb.toString();
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here