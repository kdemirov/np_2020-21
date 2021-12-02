package Lab07.RasporeduvacSoMap;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Scheduler<T> {
    private Map<Date, T> schedulersMap;

    public Scheduler() {
        schedulersMap = new TreeMap<>();
    }

    public void add(Date d, T t) {
        schedulersMap.put(d, t);
    }

    public boolean remove(Date d) {
        if (schedulersMap.containsKey(d)) {
            schedulersMap.remove(d);
            return true;

        }
        return false;
    }

    public T next() {
        return schedulersMap.entrySet()
                .stream()
                .filter(s -> s.getKey().after(Date.from(Instant.now())))
                .findFirst()
                .get()
                .getValue();


    }

    public T last() {

        List<T> tempList = schedulersMap.entrySet().stream().filter(t -> t.getKey().before(Date.from(Instant.now()))).map(t -> t.getValue()).collect(Collectors.toList());
        return tempList.get(tempList.size() - 1);
    }

    public ArrayList<T> getAll(Date begin, Date end) {
        return schedulersMap.entrySet().stream()
                .filter(t -> t.getKey().compareTo(begin) >= 0 && t.getKey().compareTo(end) <= 0)
                .map(t -> t.getValue())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public T getFirst() {
        return schedulersMap.entrySet().stream().findFirst().get().getValue();
    }

    public T getLast() {

        List<T> tempList = schedulersMap.entrySet().stream().skip(schedulersMap.size() - 2).map(t -> t.getValue()).collect(Collectors.toList());
        return tempList.get(0);
    }
}

public class SchedulerTest {


    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.getFirst());
            System.out.println(scheduler.getLast());
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.next());
            System.out.println(scheduler.last());
            ArrayList<String> res = scheduler.getAll(new Date(now.getTime() - 10000000), new Date(now.getTime() + 17000000));
            Collections.sort(res);
            for (String t : res) {
                System.out.print(t + " , ");
            }
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Date> to_remove = new ArrayList<Date>();

            while (jin.hasNextLong()) {
                Date d = new Date(jin.nextLong());
                int i = jin.nextInt();
                if ((counter & 7) == 0) {
                    to_remove.add(d);
                }
                scheduler.add(d, i);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                Date l = new Date(jin.nextLong());
                Date h = new Date(jin.nextLong());
                ArrayList<Integer> res = scheduler.getAll(l, h);
                Collections.sort(res);
                System.out.println(l + " <: " + print(res) + " >: " + h);
            }
            System.out.println("test");
            ArrayList<Integer> res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            for (Date d : to_remove) {
                scheduler.remove(d);
            }
            res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static <T> String print(ArrayList<T> res) {
        if (res == null || res.size() == 0) return "NONE";
        StringBuffer sb = new StringBuffer();
        for (T t : res) {
            sb.append(t + " , ");
        }
        return sb.substring(0, sb.length() - 3);
    }


}