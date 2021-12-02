package Kolokviumski2.NajdobriFilmovi;

import java.util.*;
import java.util.stream.Collectors;

class Movie implements Comparable<Movie> {
    private String title;
    private int[] rating;
    private double ratingCoef;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.rating = ratings;
    }

    public void setRatingCoef(double ratingCoef) {
        this.ratingCoef = ratingCoef;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, getAvgRating(), rating.length);
    }

    public double getAvgRating() {
        return Arrays.stream(rating).average().getAsDouble();
    }

    public String getTitle() {
        return title;
    }

    public int[] getRating() {
        return rating;
    }

    public double getRatingCoef() {
        return ratingCoef;
    }

    public int getSizeRatings() {
        return this.rating.length;
    }

    @Override
    public int compareTo(Movie o) {
        Comparator comparator = Comparator.comparing(Movie::getAvgRating).reversed();
        int result = comparator.compare(this, o);
        if (result == 0) {
            return this.title.compareTo(o.title);
        }
        return result;
    }
}

class MoviesList {
    Set<Movie> movies;
    Map<Double, Movie> mapRatingCoef;

    public MoviesList() {
        this.movies = new TreeSet<>();
        this.mapRatingCoef = new TreeMap<>();
    }

    public void addMovie(String title, int[] ratings) {

        this.movies.add(new Movie(title, ratings));

    }

    private int findMaxRatingNumber() {
        return this.movies.stream().mapToInt(t -> t.getRating().length).max().getAsInt();
    }

    public List<Movie> top10ByAvgRating() {
        return this.movies.stream().limit(10).collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        this.movies.stream().forEach(m -> m.setRatingCoef(calculateCoef(m)));
        return this.movies.stream().sorted(Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }

    private double calculateCoef(Movie movie) {
        return movie.getAvgRating() * movie.getSizeRatings() / findMaxRatingNumber();
    }

}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde