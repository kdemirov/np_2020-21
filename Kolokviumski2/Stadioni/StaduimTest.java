package Kolokviumski2.Stadioni;

import java.util.*;
import java.util.stream.IntStream;

enum TypeTicket {
    NEUTRAL, HOME, AWAY
}

class SeatTakenException extends Exception {

}

class SeatNotAllowedException extends Exception {

}

class FactoryTicket {
    public static TypeTicket getTypeTicket(int type) {
        switch (type) {
            case 0:
                return TypeTicket.NEUTRAL;

            case 1:
                return TypeTicket.HOME;

            case 2:
                return TypeTicket.AWAY;

            default:
                return null;
        }
    }
}

class Sector implements Comparable<Sector> {
    private String code;
    private int seat;
    private List<Boolean> seats;
    private List<TypeTicket> typeTickets;


    public Sector(String code, int seat) {
        this.code = code;
        this.seat = seat;
        this.seats = new ArrayList<>();
        this.typeTickets = new ArrayList<>();
        IntStream.range(0, seat).forEach(i -> seats.add(i, false));

    }

    public void setSeat(int position, int type) throws SeatTakenException, SeatNotAllowedException {

        if (seats.get(position - 1)) {
            throw new SeatTakenException();
        }
        TypeTicket typeTicket = FactoryTicket.getTypeTicket(type);

        if (typeTicket == TypeTicket.AWAY) {
            if (this.typeTickets.stream().anyMatch(t -> t.equals(TypeTicket.HOME))) {
                throw new SeatNotAllowedException();
            }
        }
        if (typeTicket == TypeTicket.HOME) {
            if (this.typeTickets.stream().anyMatch(t -> t.equals(TypeTicket.AWAY))) {
                throw new SeatNotAllowedException();
            }
        }
        this.typeTickets.add(typeTicket);
        seats.remove(position - 1);
        seats.add(position - 1, true);
    }


    public int availableSeats() {
        return (int) this.seats.stream().filter(b -> !b).count();
    }

    public double percent() {
        return (this.seat - availableSeats()) * (100.0 / this.seat);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, availableSeats(), seat, percent());
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return Objects.equals(code, sector.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(Sector o) {
        Comparator comparator = Comparator.comparing(Sector::availableSeats).reversed().thenComparing(Sector::getCode);
        return comparator.compare(this, o);
    }
}

class Stadium {
    private String name;
    private List<Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new ArrayList<>();
    }

    public void createSectors(String[] names, int[] sizes) {
        IntStream.range(0, names.length).forEach(i ->
                sectors.add(new Sector(names[i], sizes[i])));
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = this.sectors.stream().filter(s -> s.getCode().equals(sectorName)).findFirst().get();
        sector.setSeat(seat, type);

    }

    public void showSectors() {
        this.sectors
                .stream().sorted()
                .forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
