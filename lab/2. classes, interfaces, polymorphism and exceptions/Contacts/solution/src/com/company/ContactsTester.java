package com.company;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

enum Operator {
    VIP,
    ONE,
    TMOBILE
};

class DateCreated  {
    private int year;
    private int month;
    private int day;

    public DateCreated(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}

abstract class Contact {
    private String date;
    private DateCreated dateCreated;

    public Contact(String date) {
        this.date = date;
        String[] numbers = date.split(Pattern.quote("-"));
        this.dateCreated = new DateCreated(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]));
    }

    abstract String getType();

    public boolean isNewerThan(Contact c) {
        if(dateCreated.getYear() > c.dateCreated.getYear()) {
            return true;
        }
        else if(dateCreated.getYear() == c.dateCreated.getYear() && dateCreated.getMonth() == c.dateCreated.getMonth() && dateCreated.getDay() == c.dateCreated.getDay()) {
            return false;
        }
        else if (dateCreated.getYear() == c.dateCreated.getYear() && dateCreated.getMonth() == c.dateCreated.getMonth() && dateCreated.getDay() > c.dateCreated.getDay()) {
            return true;
        }
        else if (dateCreated.getYear() == c.dateCreated.getYear() && dateCreated.getMonth() > c.dateCreated.getMonth() && dateCreated.getDay() > c.dateCreated.getDay()) {
            return true;
        }
        else{
            return false;
        }

        /*
        //Feburary 30 threw an exception
        int comparison = dateCreated.getDate().compareTo(c.dateCreated.getDate());
        if(comparison > 0) {
            return true;
        }
        else if (comparison == 0) {
            return false;
        }
        else {
            return false;
        }
         */
    }

    public DateCreated getDateCreated() {
        return dateCreated;
    }
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    @Override
    String getType() {
        return "Email";
    }

    public String getEmail() {
        return email;
    }
}

class PhoneContact extends Contact {
    private String phoneNumber;
    private Operator operator;

    public PhoneContact(String date, String phoneNumber) {
        super(date);
        this.phoneNumber = phoneNumber;
    }

    @Override
    String getType() {
        return "Phone";
    }

    public String getPhone() {
        return phoneNumber;
    }

    public Operator getOperator() {
        if(operator != null)
            return operator;

        if (phoneNumber.charAt(2) == '0' || phoneNumber.charAt(2) == '1' || phoneNumber.charAt(2) == '2'){
            operator = Operator.TMOBILE;
        }
        else if (phoneNumber.charAt(2) == '5' || phoneNumber.charAt(2) == '6') {
            operator = Operator.ONE;
        }
        else if (phoneNumber.charAt(2) == '7' || phoneNumber.charAt(2) == '8') {
            operator = Operator.VIP;
        }

        return operator;
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private List<Contact> contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getFullName() {
        return String.format("%s %s", firstName.toUpperCase(), lastName.toUpperCase());
    }

    public void addEmailContact(String date, String email) {
        EmailContact emailContact = new EmailContact(date, email);
        contacts.add(emailContact);
    }

    public void addPhoneContact(String date, String phone) {
        PhoneContact phoneContact = new PhoneContact(date, phone);
        contacts.add(phoneContact);
    }

    public Contact[] getEmailContacts() {
        List<Contact> emailContacts = contacts.stream().filter(x -> x.getType() == "Email").collect(Collectors.toList());
        return emailContacts.toArray(new Contact[emailContacts.size()]);
    }

    public Contact[] getPhoneContacts() {
        List<Contact> phoneContacts = contacts.stream().filter(x -> x.getType() == "Phone").collect(Collectors.toList());
        return phoneContacts.toArray(new Contact[phoneContacts.size()]);
    }

    public Contact getLatestContact() {
        Contact latestContact = contacts.get(0);
        for(Contact contact : contacts){
            if(contact.isNewerThan(latestContact)){
                latestContact = contact;
            }
        }

        return latestContact;
    }

    public int getContactsLength () {
        return contacts.size();
    }

    @Override
    public String toString() {
        StringBuilder telefonskiKontakti = new StringBuilder();
        StringBuilder emailKontakti = new StringBuilder();
        for(Contact contact : contacts) {
            if(contact.getType() == "Phone") {
                PhoneContact phoneContact = (PhoneContact)contact;
                telefonskiKontakti.append("\"" + phoneContact.getPhone() + "\", ");
            }
            else if(contact.getType() == "Email") {
                EmailContact emailContact = (EmailContact)contact;
                emailKontakti.append("\"" + emailContact.getEmail() + "\", ");
            }
        }

        telefonskiKontakti.setLength(telefonskiKontakti.length() - 3);
        emailKontakti.setLength(emailKontakti.length() - 3);

        return "{" +
                "\"ime\":" + "\"" + firstName + "\"" +
                ", \"prezime\":" + "\"" + lastName + "\"" +
                ", \"vozrast\":" + age +
                ", \"grad\":" + "\"" + city + "\"" +
                ", \"indeks\":"  + index +
                ", \"telefonskiKontakti\":[" + telefonskiKontakti.toString() +
                "], \"emailKontakti\":[" + emailKontakti.toString() +"]}";
    }
}

class Faculty {
    private String name;
    private List<Student> students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = new ArrayList<Student>(Arrays.asList(students));
    }

    public int countStudentsFromCity(String cityName) {
        /*int count = 0;
        for(Student student : students) {
            if(student.getCity().equals(cityName))
                ++count;
        }

        return count;
         */

        return (int)students.stream()
                            .filter(x -> x.getCity().equals(cityName))
                            .count();
    }

    public Student getStudent(long index) {
        return students.stream()
                        .filter(x -> x.getIndex() == index)
                        .findFirst()
                        .orElse(null);
    }

    public double getAverageNumberOfContacts() {
        double numberOfContacts = 0;
        for(Student student : students) {
           numberOfContacts += student.getContactsLength();
        }

        return numberOfContacts / (double)students.size();
    }

    public Student getStudentWithMostContacts() {
        int maxContacts = 0;
        Student studentWithMostContacts = null;
        for(Student student : students) {
            if(student.getContactsLength() > maxContacts){
                maxContacts = student.getContactsLength();
                studentWithMostContacts = student;
            }
        }

        return studentWithMostContacts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Student s : students){
            sb.append("\"" + s + "\", ");
        }
        sb.setLength(sb.length() - 3);

        return "{\"fakultet\":" +  "\"" + name + "\"" +
                ", \"studenti\":[" +  sb.toString() +
                "]}";
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
