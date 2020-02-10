import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

class DuplicateNumberException extends Exception {
    private String number;
    public DuplicateNumberException(String number) {
        this.number = number;
    }

    @Override
    public String getMessage() {
        return String.format("Duplicate number: %s", this.number);
    }
}

class Contact {
    private String name;
    private ArrayList<String> numbers;

    public Contact(String name, ArrayList<String> numbers) {
        this.name = name;
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String number : numbers) {
            sb.append(name + " " + number + "\n");
        }
        return sb.toString();
    }
}

class PhoneBook {
      private TreeMap<String, TreeSet<String>> contacts;

      public PhoneBook() {
          contacts = new TreeMap<>();
      }

      public void addContact(String name, String number) throws DuplicateNumberException {
          TreeSet<String> numbers = contacts.getOrDefault(name, null);
          if(numbers == null){
              numbers = new TreeSet<String>();
              numbers.add(number);
              contacts.put(name, numbers);
          }
          else {
              for(String num : numbers){
                  if(num.equals(number)){
                      throw new DuplicateNumberException(number);
                  }
              }
              contacts.get(name).add(number);
          }
      }

      public void contactsByNumber(String number) {
          boolean found = false;
          for(Map.Entry<String, TreeSet<String>> contact : contacts.entrySet()) {
              TreeSet<String> numbers = contact.getValue();

              for(String s : numbers) {
                  if(s.contains(number)){
                      System.out.println(contact.getKey() + " " + s);
                      found = true;
                  }
              }
          }

          if(!found){
              System.out.println("NOT FOUND");
          }
      }

      public void contactsByName(String name) {
          TreeSet<String> numbers = contacts.getOrDefault(name, null);
          if(numbers == null)
          {
              System.out.println("NOT FOUND");
              return;
          }

          for(String number : numbers){
              System.out.println(name + " " + number);
          }
      }
}

