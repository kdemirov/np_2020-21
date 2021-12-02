package Kolokviumski.NaslovnaStranica;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String name) {
        super(String.format("Category %s was not found", name));
    }
}

class Category {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

}

abstract class NewsItem {
    private String title;
    private Date datePublished;
    private Category category;

    public NewsItem(String title, Date datePublished, Category category) {
        this.title = title;
        this.datePublished = datePublished;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public String getTitle() {
        return title;
    }

    public long publishedNewsMins() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return Math.abs(ChronoUnit.MINUTES.between(date.toInstant(), this.datePublished.toInstant()));
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem {

    private String text;

    public TextNewsItem(String title, Date datePublished, Category category, String text) {
        super(title, datePublished, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n", getTitle(), publishedNewsMins(), printTest());
    }

    private String printTest() {
        if (text.length() > 80) {
            return String.copyValueOf(text.toCharArray(), 0, 80);
        } else
            return text;
    }
}

class MediaNewsItem extends NewsItem {

    private String url;
    private int numberViews;

    public MediaNewsItem(String title, Date datePublished, Category category, String url, int numberViews) {
        super(title, datePublished, category);
        this.url = url;
        this.numberViews = numberViews;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", this.getTitle(), publishedNewsMins(), url, numberViews);
    }
}

class FrontPage {
    private List<NewsItem> newsItems;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        newsItems = new ArrayList<>();
    }

    public List<NewsItem> listByCategory(Category category) {
        return newsItems.stream()
                .filter(n -> n.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String name) throws CategoryNotFoundException {
        if (categoryIsPresent(name)) {
            return newsItems.stream()
                    .filter(n -> n.getCategory().equals(new Category(name)))
                    .collect(Collectors.toList());
        } else {
            throw new CategoryNotFoundException(name);
        }

    }

    private boolean categoryIsPresent(String name) {
        Optional<Category> category = Arrays.stream(categories)
                .filter(c -> c.getName().equals(name))
                .findFirst();
        if (category.isPresent()) {
            return true;
        }
        return false;
    }

    public void addNewsItem(NewsItem newsItem) {
        if (newsItem != null) {
            this.newsItems.add(newsItem);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        newsItems.forEach(n -> sb.append(n.getTeaser()));
        return sb.toString();
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde