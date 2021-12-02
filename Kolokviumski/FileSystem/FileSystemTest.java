package Kolokviumski.FileSystem;

import java.util.*;

interface IFile extends Comparable<IFile> {
    String getFileName();

    long getFileSize();

    String getFileInfo(int indent);

    void sortBySize();

    long findLargestFile();

    @Override
    default int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }
}

class File implements IFile {
    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("\t");
        }
        sb.append(String.format("File name:%10s\tFile size:%10d\n", name, size));
        return sb.toString();
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class FileNameExistsException extends Exception {
    public FileNameExistsException(String name) {
        super(String.format("There is already a file named %s in the folder test", name));
    }
}

class Folder extends File {

    private List<IFile> files;

    public Folder(String name) {
        super(name, 0);
        files = new ArrayList<>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        Optional<IFile> ifile = files.stream().filter(f -> f.getFileName().equals(file.getFileName())).findFirst();
        if (!ifile.isPresent()) {
            files.add(file);
        } else {
            throw new FileNameExistsException(file.getFileName());
        }


    }

    @Override
    public String getFileName() {
        return getName();
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(f -> f.getFileSize()).sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getFileInfo(indent));

        for (IFile file : files) {
            sb.append(file.getFileInfo(indent + 1));
        }
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        Collections.sort(files);
        for (IFile file : files) {
            file.sortBySize();
        }
    }

    @Override
    public long findLargestFile() {
        return files.stream().mapToLong(i -> i.findLargestFile()).max().orElse(0);
    }
}

class FileSystem {
    private Folder folder;

    public FileSystem() {
        folder = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        folder.addFile(file);
    }

    public void sortBySize() {
        folder.sortBySize();
    }

    public long findLargestFile() {
        return folder.findLargestFile();
    }

    @Override
    public String toString() {
        return folder.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}