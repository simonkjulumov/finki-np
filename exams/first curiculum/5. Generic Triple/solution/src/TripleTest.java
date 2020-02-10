import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple

class Triple<T> {
    private T first;
    private T second;
    private T third;
    private List<T> elements;

    public Triple(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;

        this.elements = new ArrayList<T>() {
            {
                add(first);
                add(second);
                add(third);
            }
        };
    }

    public double max() {
        return getDoubleValue(elements.stream().max(Comparator.comparingDouble(this::getDoubleValue)).get());
    }

    public double avarage() {
        return (getDoubleValue(first) + getDoubleValue(second) + getDoubleValue(third)) / 3;
    }

    public void sort() {
       elements = elements.stream().sorted(Comparator.comparingDouble(this::getDoubleValue)).collect(Collectors.toList());
    }

    private double getDoubleValue(T element) {
        Number d =  (Number) element;
        return d.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", getDoubleValue(elements.get(0)), getDoubleValue(elements.get(1)), getDoubleValue(elements.get(2)));
    }
}