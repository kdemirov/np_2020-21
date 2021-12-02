package Lab05.ListaNaCeliBroevi;

import java.util.*;

class IntegerList {
    private List<Integer> integerList;

    public IntegerList() {
        this.integerList = new ArrayList();
    }

    public IntegerList(Integer[] list) {
        this.integerList = new ArrayList<>();
        Arrays.stream(list).forEach(i -> integerList.add(i));
    }

    public void add(int el, int idx) {
        integerList.add(idx, el);

    }

    public int remove(int idx) {
        integerList.remove(idx);
        return idx;
    }

    public void set(int el, int idx) {
        integerList.set(idx, el);
    }

    public int get(int idx) {
        return integerList.get(idx);
    }

    public int size() {
        return integerList.size();
    }

    public int count(int el) {
        return (int) integerList.stream().filter(i -> i.equals(el)).count();
    }

    public void removeDuplicates() {
        for (int i = 0; i < integerList.size(); i++) {
            for (int j = 0; j < integerList.size(); j++) {
                if (integerList.get(i) == integerList.get(j)) {
                    integerList.remove(i);
                }
            }
        }
    }

    public int sumFirst(int k) {
        return integerList.stream().limit(k).mapToInt(i -> i).sum();
    }

    public int sumLast(int k) {
        return integerList.stream().skip(integerList.size() - k).mapToInt(i -> i).sum();
    }

    public IntegerList addValue(int value) {
        Integer[] niza = new Integer[integerList.size()];
        for (int i = 0; i < integerList.size(); i++) {
            niza[i] = integerList.get(i) + value;
        }
        return new IntegerList(niza);
    }

    public void shiftRight(int idx, int k) {

    }

    public void shiftLeft(int idx, int k) {

    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}