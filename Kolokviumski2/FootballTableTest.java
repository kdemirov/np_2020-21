package Kolokviumski2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum Status {
    WIN, LOSS, DRAW, INITIAL
}

class Team implements Comparable<Team> {
    private String name;
    private int score;
    private int goalDiffence;

    public Team(String name) {
        this.name = name;
        this.score = 0;
        this.goalDiffence = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getGoalDiffence() {
        return goalDiffence;
    }

    public void setGoalDiffence(int goalDiffence) {
        this.goalDiffence += goalDiffence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Team o) {
        Comparator<Team> comparator = Comparator.comparing(Team::getScore).thenComparing(Team::getGoalDiffence).reversed();
        int result = comparator.compare(this, o);
        if (result == 0) {
            return this.getName().compareTo(o.getName());
        }
        return result;

    }

    @Override
    public String toString() {
        return name.trim();
    }
}

class Game {
    private Team homeTeam;
    private Team awayTeam;
    private int homeGoals;
    private int awayGoals;
    private Status status;

    public Game(Team homeTeam, Team awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.status = Status.INITIAL;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Game{" +
                "homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", homeGoals=" + homeGoals +
                ", awayGoals=" + awayGoals +
                '}';
    }
}

class FootballTable {
    private Map<Team, List<Game>> teamListMap;

    public FootballTable() {
        this.teamListMap = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = new Team(homeTeam);
        Team away = new Team(awayTeam);
        teamListMap.putIfAbsent(home, new ArrayList<>());
        teamListMap.putIfAbsent(away, new ArrayList<>());
        teamListMap.get(home).add(new Game(home, away, homeGoals, awayGoals));
        teamListMap.get(away).add(new Game(home, away, homeGoals, awayGoals));
    }

    public void printTable() {
        setGameStatus();
        calculatePoints();
        calculateGoalDifference();

        final int[] count = {1};

        teamListMap.entrySet().
                stream().
                sorted(Comparator.comparing(Map.Entry::getKey)).
                forEach(t -> System.out.println(String.format("%2d. %-15s%5d%5d%5d%5d%5d", count[0]++, t.getKey().toString(), t.getValue().size(), totalWins(t.getValue()), totalDraws(t.getValue()), totalLosses(t.getValue()), t.getKey().getScore())));
    }

    private void setGameStatus() {
        for (Map.Entry<Team, List<Game>> entry : teamListMap.entrySet()) {
            for (Game g : entry.getValue()) {
                if (g.getHomeTeam().equals(entry.getKey())) {
                    if (g.getHomeGoals() > g.getAwayGoals()) {
                        g.setStatus(Status.WIN);
                    } else if (g.getHomeGoals() < g.getAwayGoals()) {
                        g.setStatus(Status.LOSS);
                    } else {
                        g.setStatus(Status.DRAW);
                    }
                }
                if (g.getAwayTeam().equals(entry.getKey())) {
                    if (g.getHomeGoals() > g.getAwayGoals()) {
                        g.setStatus(Status.LOSS);
                    } else if (g.getHomeGoals() < g.getAwayGoals()) {
                        g.setStatus(Status.WIN);
                    } else {
                        g.setStatus(Status.DRAW);
                    }
                }
            }
        }
    }

    private void calculatePoints() {
        for (Map.Entry<Team, List<Game>> entry : teamListMap.entrySet()) {
            for (Game g : entry.getValue()) {
                if (g.getStatus().equals(Status.WIN)) {
                    entry.getKey().setScore(3);
                }
                if (g.getStatus().equals(Status.DRAW)) {
                    entry.getKey().setScore(1);
                }
            }
        }
    }

    private void calculateGoalDifference() {
        for (Map.Entry<Team, List<Game>> entry : teamListMap.entrySet()) {
            for (Game game : entry.getValue()) {
                if (entry.getKey().equals(game.getHomeTeam())) {
                    entry.getKey().setGoalDiffence(game.getHomeGoals() - game.getAwayGoals());
                }
                if (entry.getKey().equals(game.getAwayTeam())) {
                    entry.getKey().setGoalDiffence(game.getAwayGoals() - game.getHomeGoals());
                }
            }
        }
    }

    private long totalWins(List<Game> games) {
        return games.stream().filter(g -> g.getStatus().equals(Status.WIN)).count();
    }

    private long totalDraws(List<Game> games) {
        return games.stream().filter(g -> g.getStatus().equals(Status.DRAW)).count();
    }

    private long totalLosses(List<Game> games) {
        return games.stream().filter(g -> g.getStatus().equals(Status.LOSS)).count();
    }
}

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

