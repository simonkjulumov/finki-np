import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidNumberException extends Exception {

}

class MaximumSizeExceddedException extends Exception {

}

class InvalidFormatException extends Exception {

}

class InvalidNameException extends Exception {
    public InvalidNameException(String name) {
        this.name = name;
    }

    public String name;
}

class PhoneOperators {
    public static List<String> prefixes = new ArrayList<String>()
    {
        {
            add("070");
            add("071");
            add("072");
            add("075");
            add("076");
            add("077");
            add("078");
        }
    };
}

class PhoneNumber implements Comparable<PhoneNumber> {
    private static int phoneNumberLength = 9;
    private int phoneNumber;
    private String prefix;
    private String formattedNumber;

    public PhoneNumber(String phoneNumber) throws InvalidNumberException {
        if(phoneNumber.length() != phoneNumberLength) {
            throw new InvalidNumberException();
        }

        String prefix = phoneNumber.substring(0, 3);
        if(!PhoneOperators.prefixes.contains(prefix)){
            throw new InvalidNumberException();
        }

        try{
            this.phoneNumber = Integer.parseInt(phoneNumber);
        }
        catch(Exception e) {
            throw new InvalidNumberException();
        }

        this.prefix = prefix;
        this.formattedNumber = phoneNumber;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFormattedNumber() {
        return formattedNumber;
    }

    @Override
    public int compareTo(PhoneNumber phoneNumber) {
        return this.getPhoneNumber() - phoneNumber.getPhoneNumber();
    }

    @Override
    public String toString() {
        return formattedNumber;
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private PhoneNumber[] phoneNumbers;
    private int phoneNumbersCount;
    private int maximumNumberOfContacts = 5;

    public Contact(String name, String... phoneNumbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException  {
        if(!nameIsValid(name)){
            throw new InvalidNameException(name);
        }

        this.name = name;
        this.phoneNumbersCount = 0;
        this.phoneNumbers = new PhoneNumber[phoneNumbersCount];
        for(String phoneNumber : phoneNumbers) {
            addNumber(phoneNumber);
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n", "", "\n");
        stringJoiner.add(name);
        stringJoiner.add(phoneNumbersCount + "");

        String[] sortedNumbers = getNumbers();
        for(String phoneNumber : sortedNumbers){
            stringJoiner.add(phoneNumber);
        }

        return stringJoiner.toString();
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        PhoneNumber[] numbers = Arrays.copyOf(phoneNumbers, phoneNumbers.length);
        Arrays.sort(numbers);
        return Arrays
                .stream(numbers)
                .map(x -> x.getFormattedNumber())
                .toArray(String[]::new);
    }

    public void addNumber(String phoneNumber) throws InvalidNumberException, MaximumSizeExceddedException {
        if(canAddPhoneNumber()) {
            PhoneNumber[] phoneNumbersCopy = Arrays.copyOf(phoneNumbers, phoneNumbersCount + 1);
            phoneNumbersCopy[phoneNumbersCount] = new PhoneNumber(phoneNumber);
            phoneNumbers = phoneNumbersCopy;
            ++phoneNumbersCount;
        }
        else{
            throw new MaximumSizeExceddedException();
        }
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        try{
            return new Contact(s);
        }
        catch(Exception ex){
            throw new InvalidFormatException();
        }
    }

    public boolean hasPhoneNumberWithPrefix(String prefix) {
        for(PhoneNumber phoneNumber : phoneNumbers) {
            if(phoneNumber.getPrefix().equals(prefix)) {
                return true;
            }
        }

        return false;
    }

    private boolean canAddPhoneNumber() {
        return phoneNumbersCount >= 0 && phoneNumbersCount < maximumNumberOfContacts;
    }

    private boolean nameIsValid(String name) {
        return name.matches("[a-zA-Z0-9]{4,10}");
    }

    @Override
    public int compareTo(Contact contact) {
        return name.compareTo(contact.getName());
    }
}

class PhoneBook {
    private Contact[] contacts;
    private int maximumNumberOfContacts = 250;

    public PhoneBook() {
        contacts = new Contact[0];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Contact sorted[] = getContacts();
        for (Contact contact : sorted) {
            sb.append(contact.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if(numberOfContacts() == maximumNumberOfContacts) {
            throw new MaximumSizeExceddedException();
        }

        if(contactExistsInPhoneBook(contact)) {
            throw new InvalidNameException(contact.getName());
        }

        Contact[] newContacts = Arrays.copyOf(contacts, contacts.length + 1);
        newContacts[contacts.length] = contact;
        contacts = newContacts;
    }

    public Contact getContactForName(String name) {
        return Arrays
                .stream(contacts)
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public int numberOfContacts() {
        return contacts.length;
    }

    public Contact[] getContacts() {
        Arrays.sort(contacts);
        return Arrays.copyOf(contacts, contacts.length);
    }

    public boolean removeContact(String name) {
        for(int i = 0; i < contacts.length; i++) {
            if(contacts[i].getName().equals(name)) {
                int index = 0;
                Contact[] newContacts = new Contact[contacts.length - 1];
                for(int j = 0; j < contacts.length; j ++) {
                    if(i == j)
                    {
                        continue;
                    }
                    newContacts[index++] = contacts[j];
                }
                contacts = newContacts;
                return true;
            }
        }

        return false;
    }

    public static boolean saveAsTextFile(PhoneBook phonebook,String path) throws IOException, FileNotFoundException {
        try {
            File temp = new File(path);
            temp.createNewFile(); // if file already exists will do nothing
            PrintWriter pw = new PrintWriter(temp);
            pw.print(phonebook.toString());
            pw.flush();
            pw.close(); // flushes before close
        } catch (FileNotFoundException e) {
            return false;
        }

        return true;
    }

    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidFormatException, InvalidNumberException, InvalidNameException, MaximumSizeExceddedException {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
            String current;
            PhoneBook phoneBook = new PhoneBook();
            while ((current = br.readLine()) != null) {
                String name = current;
                int len = Integer.parseInt(br.readLine());
                String phoneNumbers[] = new String[len];
                for (int i = 0; i < len; i++) {
                    phoneNumbers[i] = br.readLine();
                }
                Contact contact = new Contact(name, phoneNumbers);
                phoneBook.addContact(contact);
                br.readLine();
            }
            return phoneBook;
        }
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        Contact[] contactsForNumber = Arrays
                .stream(contacts)
                .filter(x -> x.hasPhoneNumberWithPrefix(number_prefix))
                .toArray(Contact[]::new);
        Arrays.sort(contactsForNumber);

        return contactsForNumber;
    }

    private boolean contactExistsInPhoneBook(Contact c) {
        Contact exists = getContactForName(c.getName());
        return exists != null;
    }
}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
