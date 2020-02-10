import java.util.*;

class IntegerList {
    private List<Integer> numbers;

    public IntegerList(Integer... numbers) {
        this.numbers = new LinkedList<Integer>();
        for(Integer number : numbers) {
            this.numbers.add(number);
        }
    }

    public void add(int element, int index) {
        if(index >= numbers.size()) {
            int counter = index - numbers.size();
            while(counter-- > 0) {
                numbers.add(0);
            }
        }

        numbers.add(index, element);
    }

    public int remove(int index) {
        validateIndexIsInBounds(index);

        return numbers.remove(index);
    }

    public void set(int element, int index) {
        validateIndexIsInBounds(index);

        numbers.set(index, element);
    }

    public int get(int index) {
        validateIndexIsInBounds(index);

        return numbers.get(index);
    }

    public int size() {
        return numbers.size();
    }

    public int count(int element) {
        return (int)numbers
                .stream()
                .filter(x -> x == element)
                .count();
    }

    public void removeDuplicates() {
        List<Integer> temp = new ArrayList<>();
        ListIterator<Integer> li = numbers.listIterator(numbers.size());

        while (li.hasPrevious()) {
            int elem = li.previous();
            if (temp.indexOf(elem) == -1) {
                temp.add(0, elem);
            }
        }
        this.numbers = temp;

        /*
         Collections.reverse(numbers);
        numbers = numbers.stream().distinct().collect(Collectors.toList());
        Collections.reverse(numbers);
         */
    }

    public int sumFirst(int k) {
        //validateIndexIsInBounds(k);
        if(k == 0) return 0;
        if(k == 1) return numbers.get(0);
        if(k >= numbers.size()) k = numbers.size();

        //return numbers.subList(0, k).stream().reduce(Integer::sum).orElse(0);
        int sum = 0;
        for(int i = 0; i < k; i++) {
            sum += numbers.get(i);
        }
        return sum;
    }

    public int sumLast(int k) {
        //validateIndexIsInBounds(k);
        if(k == 0) return 0;
        if(k == 1) return numbers.get(numbers.size() - 1);
        if(k >= numbers.size() || k < 0) k = 0;

        //return numbers.stream().skip(numbers.size() - k).reduce(Integer::sum).orElse(0);
        //return numbers.subList(size() - k, size()).stream().reduce(Integer::sum).orElse(0);
        int sum = 0;
        for(int i = numbers.size() - 1; i >= k; i--) {
            sum += numbers.get(i);
        }
        return sum;
    }

    public void shiftRight(int index, int k) {
        validateIndexIsInBounds(index);

        int newIndex = (index + k) % size();
        Integer element = numbers.remove(index);
        add(element, newIndex);
    }

    public void shiftLeft(int index, int k) {
        validateIndexIsInBounds(index);

        int newIndex = index - (k % size());
        if (newIndex < 0)
            newIndex += size();
        Integer element = numbers.remove(index);
        numbers.add(newIndex, element);
    }

    public IntegerList addValue(int value) {
        Integer[] newNumbers = new Integer[numbers.size()];
        int index = 0;

        for(Integer number : numbers) {
            newNumbers[index++] = number + value;
        }

        /*
        IntegerList result = new IntegerList();
        numbers.forEach(i -> result.add(i + value, numbers.indexOf(i)));
        return result;
         */
        return new IntegerList(newNumbers);
    }

    private void validateIndexIsInBounds(int index) {
        if(index < 0 || index >= numbers.size())
            throw new IndexOutOfBoundsException();
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}