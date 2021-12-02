package Exam.Discount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
class PricesWithDiscount implements Comparable<PricesWithDiscount>{
    private int price;
    private int priceWithDiscount;

    public PricesWithDiscount(int priceWithDiscount,int price){
        this.priceWithDiscount=priceWithDiscount;
        this.price=price;
    }
    public int getPercent(){
        return ((price-priceWithDiscount)*100)/price;
    }
    public int getTotalDiscount(){
        return price-priceWithDiscount;
    }
    public static PricesWithDiscount getPriceWithDiscount(String part){
        String [] parts=part.split(":");
        int price=Integer.parseInt(parts[1]);
        int priceWithDiscount=Integer.parseInt(parts[0]);
        return new PricesWithDiscount(priceWithDiscount,price);
    }
    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",getPercent(),priceWithDiscount,price);
    }

    @Override
    public int compareTo(PricesWithDiscount o) {
        Comparator comparator=Comparator.comparing(PricesWithDiscount::getPercent).thenComparing(PricesWithDiscount::getTotalDiscount).reversed();
        int result=comparator.compare(this,o);
        if(result==0){
            return 1;
        }
        return comparator.compare(this,o);
            }
}
class Store {
    private String name;
    private Set<PricesWithDiscount> pricesWithDiscounts;

    public Store(String name,Set<PricesWithDiscount> pricesWithDiscounts){
        this.name=name;
        this.pricesWithDiscounts=pricesWithDiscounts;
    }
    public double getAverageDiscount(){
        int sum=this.pricesWithDiscounts
                .stream()
                .mapToInt(PricesWithDiscount::getPercent)
                .sum();
        return sum*1.0/this.pricesWithDiscounts.size();
    }
    public int getTotalDiscount(){
        return this.pricesWithDiscounts
                .stream()
                .mapToInt(PricesWithDiscount::getTotalDiscount)
                .sum();
    }

    public String getName() {
        return name;
    }

    public static Store getStore(String line){
        String [] parts=line.split("\\s+");
        String name =parts[0];
       Set<PricesWithDiscount> pricesWithDiscountList=
                Arrays.stream(parts).skip(1)
                        .map(PricesWithDiscount::getPriceWithDiscount)
                        .collect(Collectors.toCollection(TreeSet::new));
        return new Store(name,pricesWithDiscountList);

    }
    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(String.format("%s\n",name));
        sb.append(String.format("Average discount: %.1f%%\n",getAverageDiscount()));
        sb.append(String.format("Total discount: %d\n",getTotalDiscount()));
        this.pricesWithDiscounts.forEach(p->sb.append(String.format("%s\n",p)));
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


}
class Discounts{
    private List<Store> stores;

    public Discounts(){
        this.stores=new ArrayList<>();
    }
    public int readStores(InputStream inputStream){
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        this.stores=br.lines()
                .map(Store::getStore)
                .collect(Collectors.toList());
        return this.stores.size();
    }
    public List<Store> byAverageDiscount(){
        return this.stores
                .stream()
                .sorted(Comparator.comparing(Store::getAverageDiscount).reversed().thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        return this.stores
                .stream()
                .sorted(Comparator.comparing(Store::getTotalDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde