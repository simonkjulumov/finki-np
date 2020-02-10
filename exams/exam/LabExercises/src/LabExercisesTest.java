import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}

class Student implements Comparable<Student> {
    private String index;
    private List<Integer> points;
    private boolean passed;
    private int yearOfStudies;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
        yearOfStudies = Integer.parseInt(index.charAt(0) + "" + index.charAt(1));
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public Double totalPoints() {
        return points.stream().mapToInt(Integer::intValue).sum() / 10.0;
    }

    public boolean didPass() {
        if(points.size() < 8) {
            return false;
        }
        return true;
    }

    public int getYearOfStudies() {
        return yearOfStudies;
    }

    @Override
    public int compareTo(Student t) {
        return totalPoints().compareTo(t.totalPoints());
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, didPass() ? "YES" : "NO", totalPoints());
    }
}

class LabExercises {
    private ArrayList<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n) {
        Comparator<Student> comparator = new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                if(s1.totalPoints().equals(s2.totalPoints())) {
                    return s1.getIndex().compareTo(s2.getIndex());
                }
                return s1.totalPoints().compareTo(s2.totalPoints());
            }
        };


        if(ascending) {
            students.stream().sorted(comparator).limit(n).forEach(System.out::println);
        }
        else {
             students.stream().sorted(comparator.reversed()).limit(n).forEach(System.out::println);
        }
    }

    public List<Student> failedStudents () {
        List<Student> copy = students.stream().filter(x -> x.didPass() == false).sorted(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s1.getIndex().compareTo(s2.getIndex());
            }
        }).collect(Collectors.toList());
        return copy;
    }

    public Map<Integer,Double> getStatisticsByYear() {
        //students.stream().filter(x -> x.didFail() == false)
        return new HashMap<>();
    }
}