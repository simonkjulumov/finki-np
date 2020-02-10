
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;

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
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

class NonExistingItemException extends Exception {
    private int id;

    public NonExistingItemException(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Item with id %d doesn't exist", id);
    }
}

class ItemOpenedTooManyTimesException extends Exception { }

class ItemCannotBeOpenWithGivenDateItemException extends Exception { }

class Archive {
    protected int id;
    protected Date dateArchived;
    private boolean isOpened;

    protected Archive(int id) {
        this.id = id;
        isOpened = false;
    }

    public void setDateArchived(Date date) {
        dateArchived = date;
    }

    public int getId() {
        return id;
    }
}

class LockedArchive extends Archive {
    private Date dateToOpen;

    protected LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public void open(Date date) throws ItemCannotBeOpenWithGivenDateItemException {
        if(date.compareTo(dateToOpen) < 0) {
            throw new ItemCannotBeOpenWithGivenDateItemException();
        }
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive {
    private int maxOpenCount;
    private int openedCount;

    protected SpecialArchive(int id, int maxOpenCount) {
        super(id);
        this.maxOpenCount = maxOpenCount;
        this.openedCount = 0;
    }

    public void open() throws ItemOpenedTooManyTimesException {
        if(openedCount >= maxOpenCount){
            throw new ItemOpenedTooManyTimesException();
        }
        ++openedCount;
    }

    public int getMaxOpenCount() {
        return this.maxOpenCount;
    }
}

class ArchiveStore {
    private List<Archive> archives;
    private StringBuilder logger;
    public ArchiveStore() {
        archives = new ArrayList<>();
        logger = new StringBuilder();
    }

    public void archiveItem(Archive item, Date date) {
        item.setDateArchived(date);
        archives.add(item);
        logger.append(String.format("Item %d archived at %s\n", item.getId(), toUTCDate(date)));
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive item = archives.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if(item == null)
        {
            throw new NonExistingItemException(id);
        }

        if(item instanceof SpecialArchive) {
            SpecialArchive specialArchive = (SpecialArchive)item;
            try{
                specialArchive.open();
                logger.append(String.format("Item %d opened at %s\n", id,  toUTCDate(date)));
            }
            catch(ItemOpenedTooManyTimesException ex) {
                logger.append(String.format("Item %d cannot be opened more than %d times\n", id, specialArchive.getMaxOpenCount()));
            }
        }
        else {
            LockedArchive lockedArchive = (LockedArchive)item;
            try{
                lockedArchive.open(date);
                logger.append(String.format("Item %d opened at %s\n", id,  toUTCDate(date)));
            }
            catch(ItemCannotBeOpenWithGivenDateItemException ex) {
                logger.append(String.format("Item %d cannot be opened before %s\n", id, toUTCDate(lockedArchive.getDateToOpen())));
            }
        }
    }

    public String getLog() {
        return logger.toString();
    }

    private String toUTCDate(Date date) {
        return date.toString().replaceAll("GMT", "UTC");
    }
}