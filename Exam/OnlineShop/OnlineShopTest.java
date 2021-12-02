package Exam.OnlineShop;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(String.format("Product with id %s does not exist in the online shop!",message));
    }
}


class Product {
private String category;
private String id;
private String name;
private LocalDateTime createdAt;
double price;
private int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold=0;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}
class ComparatorBuilder{
    public static Comparator<Product> getComparator(COMPARATOR_TYPE comparator_type){
        switch (comparator_type){
            case NEWEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt).reversed();
            case OLDEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt);
            case MOST_SOLD_FIRST:
                return new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        int result=Integer.compare(o2.getQuantitySold(),o1.getQuantitySold());
                        if(result==0){
                            return 1;
                        }
                        return result;
                    }
                };
            case LEAST_SOLD_FIRST:
                return new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        int result=Integer.compare(o1.getQuantitySold(),o2.getQuantitySold());
                        if(result==0){
                            return 1;
                        }
                        return result;
                    }
                };
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice).reversed();
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice);
            default:
                return Comparator.comparing(Product::getId);
        }
    }
}

class OnlineShop {

    private Map<Product,Integer> productByQuntity;
    private List<Product> allProducts;

    OnlineShop() {
        this.productByQuntity=new HashMap<>();
        this.allProducts=new ArrayList<>();

    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product product=new Product(category,id,name,createdAt,price);
        this.allProducts.add(product);
        this.productByQuntity.putIfAbsent(product,0);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        Optional<Product> optionalProduct=this.allProducts.stream()
                .filter(p->p.getId().equals(id))
                .findFirst();
        if(!optionalProduct.isPresent()) {
            throw new ProductNotFoundException(id);
        }
        this.productByQuntity.computeIfPresent(optionalProduct.get(),(k,v)->{
            v+=quantity;
            return v;
        });
        return optionalProduct.get().price*quantity;
        //return 0.0;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();

        if(category!=null){
            result=getPagination(category,comparatorType,pageSize,allProducts);
        }else{
            result=getPagination(category,comparatorType,pageSize,allProducts);
        }
        return result;
    }

    private List<List<Product>> getPagination(String category, COMPARATOR_TYPE comparatorType, int pageSize, List<Product> allProducts) {
        List<List<Product>> result=new ArrayList<>() ;
        setQuantitySold();
        if(category!=null){
            allProducts=this.allProducts
                    .stream().filter(p->p.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        allProducts=allProducts.stream()
                .sorted(ComparatorBuilder.getComparator(comparatorType)).collect(Collectors.toList());
        List<Product> tmp=new ArrayList<>();
        if(allProducts.size()<pageSize){
            result.add(allProducts);
        }else {
            for (int i = 0; i < allProducts.size(); i++) {
                tmp.add(allProducts.get(i));
                if (tmp.size() == pageSize) {
                    result.add(tmp);
                    tmp = new ArrayList<>();
                }
                if(i==allProducts.size()-1&&tmp.size()!=0){
                    result.add(tmp);
                }
            }
        }

        return result;
    }

    private void setQuantitySold() {
        this.productByQuntity.forEach(Product::setQuantitySold);
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

