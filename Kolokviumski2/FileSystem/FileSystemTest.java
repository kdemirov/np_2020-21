package Kolokviumski2.FileSystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File> {
    private String name;
    private int size;
    private LocalDateTime createdAt;

    public File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getByMonthAndDay() {
        return String.format("%s-%d", createdAt.getMonth(), createdAt.getDayOfMonth());
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size &&
                Objects.equals(name, file.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }

    @Override
    public int compareTo(File o) {
        Comparator comparator = Comparator.comparing(File::getCreatedAt).thenComparing(File::getName).thenComparing(File::getSize);
        return comparator.compare(this, o);
    }

    public boolean isHiddenAndLessThanSize(int size) {
        return this.name.startsWith(".") && this.size < size;
    }
}

class FileSystem {
    private Map<Character, Set<File>> folders;
    private List<File> allFiles;

    public FileSystem() {
        this.folders = new HashMap<>();
        allFiles = new LinkedList<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        File file = new File(name, size, createdAt);
        this.folders.putIfAbsent(folder, new TreeSet<>());
        this.folders.get(folder).add(file);
        this.allFiles.add(file);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        return folders.values()
                .stream().flatMap(Collection::stream)
                .filter(file -> file.isHiddenAndLessThanSize(size))
                .collect(Collectors.toList());


    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        return folders.stream().flatMap(f -> this.folders.get(f).stream())
                .mapToInt(File::getSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear() {
        return this.allFiles.stream()
                .collect(Collectors.groupingBy(
                        file -> file.getCreatedAt().getYear(),
                        Collectors.toCollection(TreeSet::new)
                ));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return this.allFiles.stream()
                .collect(Collectors.groupingBy(
                        File::getByMonthAndDay,
                        Collectors.summingLong(File::getSize)
                ));
    }

}

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

