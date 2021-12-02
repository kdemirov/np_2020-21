package Lab03.Picerija;

import java.util.*;

interface Item {
    public int getPrice();
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super("Invalid pizza type exception");
    }
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super("Invalid extra type exception");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super("Item out of Stok Exception");
    }
}

class ArrayIndexOutOFBoundsException extends Exception {

    public ArrayIndexOutOFBoundsException(int index) {
        super("This" + index + "Does not exist");
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
        super("Order Locked Exception");
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
        super("EmptyOrder");
    }
}

class ExtraItem implements Item {
    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Ketchup") || type.equals("Coke")) {
            this.type = type;
        } else {
            throw new InvalidExtraTypeException();
        }

    }

    @Override
    public int getPrice() {
        int price = 0;
        if (type.equals("Ketchup")) {
            price = 3;
        }
        if (type.equals("Coke")) {
            price = 5;
        }
        return price;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class PizzaItem implements Item {

    private String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian")) {
            this.type = type;
        } else {
            throw new InvalidPizzaTypeException();
        }

    }

    @Override
    public int getPrice() {
        int price = 0;
        if (type.equals("Standard")) {
            price = 10;
        }
        if (type.equals("Vegetarian")) {
            price = 8;
        }
        if (type.equals("Pepperoni")) {
            price = 12;
        }
        return price;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class Order {
    private List<Item> itemList;
    private List<Integer> quantityList;
    private boolean isLocked = false;

    public Order() {
        this.itemList = new ArrayList<>();
        this.quantityList = new ArrayList<>();
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (!isLocked) {
            boolean itemRemoved = false;
            if (count > 10) {
                throw new ItemOutOfStockException(item);
            }
            Integer indexCounter = null;
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).equals(item)) {
                    itemList.remove(i);
                    itemRemoved = true;
                    indexCounter = i;
                }
            }
            if (indexCounter != null) {
                quantityList.remove(indexCounter);
            }
            add(itemRemoved, item, count, indexCounter);
        } else {
            throw new OrderLockedException();
        }
    }

    private void add(boolean itemRemoved, Item item, int count, Integer indexCounter) {
        if (itemRemoved) {
            itemList.add(indexCounter, item);
            quantityList.add(indexCounter, count);
        } else {
            itemList.add(item);
            quantityList.add(count);
        }
    }

    public int getPrice() {
        int sum = 0;
        for (int i = 0; i < itemList.size(); i++) {
            int price = itemList.get(i).getPrice();
            int quantity = quantityList.get(i);
            sum += price * quantity;
        }
        return sum;
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (int i = 0; i < itemList.size(); i++) {
            int itemPrice = itemList.get(i).getPrice();
            int quantyti = quantityList.get(i);
            int price = itemPrice * quantyti;
            sb.append(String.format("%3d.%-15sx%2d%5d$", counter, itemList.get(i).toString(), quantityList.get(i), price));
            sb.append("\n");
            counter++;
        }
        sb.append(String.format("Total:%21d$", getPrice()));
        System.out.println(sb.toString());
    }

    public void removeItem(int idx) throws ArrayIndexOutOFBoundsException, OrderLockedException {
        if (!isLocked) {
            if (idx < 0 || idx > itemList.size()) {
                throw new ArrayIndexOutOFBoundsException(idx);
            }
            itemList.remove(idx);
            quantityList.remove(idx);
        } else {
            throw new OrderLockedException();
        }
    }

    public void lock() throws EmptyOrder {
        if (itemList.size() < 1) {
            throw new EmptyOrder();
        }
        this.isLocked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}