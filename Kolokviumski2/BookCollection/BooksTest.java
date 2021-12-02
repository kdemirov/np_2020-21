package Kolokviumski2.BookCollection;

import java.util.*;
import java.util.stream.Collectors;

class Book implements Comparable<Book> {
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Float.compare(book.price, price) == 0 &&
                Objects.equals(title, book.title) &&
                Objects.equals(category, book.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, category, price);
    }

    @Override
    public int compareTo(Book o) {
        Comparator comparator = Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice);
        return comparator.compare(this, o);
    }
}

class BookCollection {
    private Map<String, Set<Book>> books;

    public BookCollection() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        this.books.putIfAbsent(book.getCategory(), new TreeSet<>());
        this.books.get(book.getCategory()).add(book);
    }

    public void printByCategory(String category) {
        this.books.get(category).forEach(t -> System.out.println(t));
    }

    public List<Book> getCheapestN(int n) {
        return this.books.entrySet().stream().flatMap(t -> t.getValue().stream()).sorted(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle)).limit(n).collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде