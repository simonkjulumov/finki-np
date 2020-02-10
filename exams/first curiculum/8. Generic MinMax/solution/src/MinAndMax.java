import java.util.Scanner;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T> {
    private T minimum;
    private T maximum;
    private int numberOfUpdatedElements = 0;

    public void update(T element) {
        if(minimum == null)
        {
            minimum = element;
            maximum = element;
            return;
        }

        if(element instanceof Number){
            Number min = (Number)minimum;
            Number max = (Number)maximum;

            Number el = (Number)element;
            if(el.doubleValue() > max.doubleValue()) {
                maximum = element;
                numberOfUpdatedElements += 1;
            }

            if(el.doubleValue() < min.doubleValue()){
                minimum = element;
                numberOfUpdatedElements += 1;
            }
        }

        if(element instanceof String) {
            String min = minimum.toString();
            String max = maximum.toString();
            if(element.toString().compareTo(max) > 0){
                maximum = element;
                numberOfUpdatedElements += 1;
            }
            if(element.toString().compareTo(min) < 0){
                minimum = element;
                numberOfUpdatedElements += 1;
            }
        }
    }

    public T max() {
        return maximum;
    }

    public T min() {
        return minimum;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min(), max(), numberOfUpdatedElements);
    }
}