package Kolokviumski.Arhiva;

import java.text.SimpleDateFormat;
import java.util.*;

class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

abstract class Archive {
    private int id;
    private Date dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }

    public abstract String open(Date date);
}

class LockedArchive extends Archive {
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }

    @Override
    public String open(Date date) {
        if (!date.before(dateToOpen)) {
            return String.format("opened at %s", PrintDateUTC.printDateUtc(date));
        }
        return String.format("cannot be opened before %s", PrintDateUTC.printDateUtc(dateToOpen));
    }
}

class PrintDateUTC {
    public static String printDateUtc(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(d);
    }
}

class SpecialArchive extends Archive {
    private int maxOpen;
    public int opened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.opened = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }


    @Override
    public String open(Date date) {
        if (this.opened < maxOpen) {
            this.opened++;
            return String.format("opened at %s", PrintDateUTC.printDateUtc(date));
        }
        return String.format("cannot be opened more than %d times", maxOpen);
    }
}

class ArchiveStore {
    private List<Archive> archives;
    public static StringBuilder sb = new StringBuilder();

    public ArchiveStore() {
        this.archives = new ArrayList<>();
    }

    public void archiveItem(Archive item, Date date) {
        item.setDateArchived(date);
        archives.add(item);
        sb.append(String.format("Item %d archived at %s\n", item.getId(), PrintDateUTC.printDateUtc(date)));
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Optional<Archive> item = archives.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!item.isPresent()) {
            throw new NonExistingItemException(id);
        }
        sb.append(String.format("Item %d %s\n", id, item.get().open(date)));

    }

    public String getLog() {
        return sb.toString();
    }

}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде


