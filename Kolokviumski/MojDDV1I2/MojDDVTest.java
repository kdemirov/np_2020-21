package Kolokviumski.MojDDV1I2;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

abstract class Item {
    private int itemPrice;

    public Item(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public abstract double getDDV();

    public double getReturnDDV() {
        return getDDV() * 0.15;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public static Item createItemFromPart(Integer price, String type) {
        switch (type) {
            case "A":
                return new ItemA(price);
            case "B":
                return new ItemB(price);
            default:
                return new ItemV(price);
        }
    }
}

class ItemA extends Item {


    public ItemA(int itemPrice) {
        super(itemPrice);
    }

    @Override
    public double getDDV() {
        return getItemPrice() * 0.18;
    }

}

class ItemB extends Item {


    public ItemB(int itemPrice) {
        super(itemPrice);
    }

    @Override
    public double getDDV() {
        return getItemPrice() * 0.05;
    }
}

class ItemV extends Item {


    public ItemV(int itemPrice) {
        super(itemPrice);
    }

    @Override
    public double getDDV() {
        return 0;
    }
}

class Receipt {
    private int id;
    private List<Item> items;

    public Receipt(int id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public int getSum() {
        return items.stream().mapToInt(i -> i.getItemPrice()).sum();
    }

    public double getTaxReturn() {
        return items.stream().mapToDouble(i -> i.getReturnDDV()).sum();
    }

    public static Receipt getReceiptFromLine(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        int id = Integer.parseInt(parts[0]);
        List<Item> list = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            Integer price = Integer.parseInt(parts[i]);
            String type = parts[i + 1];
            Item item = Item.createItemFromPart(price, type);
            list.add(item);
        }
        Receipt receipt = new Receipt(id, list);
        if (receipt.getSum() > 30000) {
            throw new AmountNotAllowedException(receipt.getSum());
        }
        return receipt;
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f", id, getSum(), getTaxReturn());
    }
}

class MojDDV {
    List<Receipt> receipts;

    public MojDDV() {
        receipts = new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        receipts = br.lines().map(line -> {
            try {
                return Receipt.getReceiptFromLine(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        receipts.forEach(r -> pw.println(r.toString()));
        pw.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        DoubleSummaryStatistics dss = receipts.stream().mapToDouble(r -> r.getTaxReturn()).summaryStatistics();
        pw.printf("min:\t%5.3f\nmax:\t%5.3f\nsum:\t%5.3f\ncount:\t%-5d\navg:\t%5.3f", dss.getMin(), dss.getMax(), dss.getSum(), dss.getCount(), dss.getAverage());
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);
        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);


    }
}