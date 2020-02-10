import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

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

interface IFile {
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    long findLargestFile();
    String getType();
}

class File implements IFile {
    private String name;
    private long size;
    private String type = "File";

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

    @Override
    public String getFileInfo() {
        return String.format("File name %10s File size: %10s", name, getFileSize());
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }

    @Override
    public String getType() {
        return "File";
    }

    @Override
    public String toString() {
        return String.format("File name: \t%s \tFile size: \t%d", name, size);
    }
}

class FileNameExistsException extends Exception {
    private String fileName;

    public FileNameExistsException(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getMessage() {
        return String.format("There is already a file named test in the folder %s", fileName);
    }
}

class Folder implements IFile {
    private ArrayList<IFile> filesAndFolders;
    private String name;

    public Folder(String name) {
        this.name = name;
        this.filesAndFolders = new ArrayList<>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        IFile exists = filesAndFolders.stream().filter(x -> x.getFileName().equals(file.getFileName())).findFirst().orElse(null);
        if(exists != null) {
            throw new FileNameExistsException(file.getFileName());
        }

        filesAndFolders.add(file);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return filesAndFolders.stream().mapToLong(x -> x.getFileSize()).sum();
    }

    @Override
    public String getFileInfo() {
        return String.format("Folder name \t\t%s Folder size: \t\t%s", name, getFileSize());
    }

    @Override
    public void sortBySize() {
        Collections.sort(filesAndFolders, Comparator.comparingLong(x -> x.getFileSize()));
    }

    @Override
    public long findLargestFile() {
        long largest = 0;
        IFile files = filesAndFolders.get(0);
        for(IFile f : filesAndFolders) {
            if(f.getType().equals("File")) {
                if(largest < f.getFileSize()) {
                    largest += f.getFileSize();
                }
            }
        }
        return largest;
        //return filesAndFolders.stream().filter(x -> x instanceof File).map(IFile::findLargestFile).max(Long::compareTo).get();
    }

    @Override
    public String getType() {
        return "Folder";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Folder name: \t%s \tFile size: \t%d", name, getFileSize()));
        for(IFile f : filesAndFolders) {
            sb.append(f.toString());
        }
        return sb.toString();
    }
}

class FileSystem {
    private Folder rootDirectory;

    public FileSystem() {
        rootDirectory = new Folder("root");
    }

    public void addFile (IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    public long findLargestFile() {
        return rootDirectory.findLargestFile();
    }

    public void sortBySize() {
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.toString();
    }
}