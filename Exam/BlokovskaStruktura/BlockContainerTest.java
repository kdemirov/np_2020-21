package Exam.BlokovskaStruktura;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BlockContainer<T extends Comparable<T>> implements Comparable<T>{
    private Map<Integer,Set<T>> blockElements;
    private int n;
    private Integer counter;
    public BlockContainer(int n){
        this.n=n;
        this.counter=0;
        this.blockElements=new HashMap();
    }
    public void add (T a){
        if(this.blockElements.size()!=0){
          Set<T> tmpSet=this.blockElements.get(this.blockElements.size()-1);
          if(tmpSet.size()<n) {
              tmpSet.add(a);
          }else{
              this.blockElements.putIfAbsent(counter++,new TreeSet<>());
              this.blockElements.get(this.blockElements.size()-1).add(a);
          }
        }else{

            this.blockElements.putIfAbsent(counter++,new TreeSet<>());
            this.blockElements.get(this.blockElements.size()-1).add(a);
        }
    }
    public boolean remove(T a){
        Set<T> tmpSet=this.blockElements.get(this.blockElements.size()-1);
        tmpSet.remove(a);
        if(tmpSet.size()==0){
            this.blockElements.remove(this.blockElements.size()-1);
        }
        return true;
    }
    public void sort(){
      List<T> tmpList=this.blockElements
              .values()
              .stream()
              .flatMap(s->s.stream())
              .sorted()
              .collect(Collectors.toList());

       Map<Integer,Set<T>> tmpListWithSet=new HashMap<>();
        Set<T> newSet=new TreeSet<>();
            Integer counter=0;
            for (int j = 0; j < tmpList.size(); j++) {
                if(newSet.size()==n){
                    tmpListWithSet.putIfAbsent(counter++,newSet);
                    newSet=new TreeSet<>();
                    newSet.add(tmpList.get(j));

                }else{
                    newSet.add(tmpList.get(j));
                }
                if(j==tmpList.size()-1){
                    tmpListWithSet.putIfAbsent(counter++,newSet);
            }
        }
            this.blockElements=tmpListWithSet;

    }

    @Override
    public String toString() {
        return this.blockElements
                .values()
                .stream()
                .map(s->s.toString())
                .collect(Collectors.joining(","));
    }

    @Override
    public int compareTo(T o) {
        return this.compareTo(o);
    }
}
public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



