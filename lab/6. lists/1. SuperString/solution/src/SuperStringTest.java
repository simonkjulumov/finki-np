import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}

class SuperString {
    private LinkedList<String> strings;
    private Stack<String> lastAdded;

    public SuperString() {
        strings = new LinkedList<>();
        lastAdded = new Stack<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String s : strings){
            sb.append(s);
        }
        return sb.toString();
    }

    public void append(String s) {
        strings.addLast(s);
        lastAdded.push(s);
    }

    public void insert(String s) {
        strings.addFirst(s);
        lastAdded.push(s);
    }

    public boolean contains(String s) {
        return this.toString().contains(s);
    }

    public void reverse() {
        LinkedList<String> reversedList = new LinkedList<>();
        for(String s : strings) {
            reversedList.addFirst(getReversedString(s));
        }

        strings = new LinkedList<>(reversedList);
    }

    public void removeLast(int k) {
        while(k > 0) {
            String toRemove = lastAdded.pop();
            boolean removed = strings.remove(toRemove);
            if(!removed) {
                strings.remove(getReversedString(toRemove));
            }
            k--;
        }
    }

    private String getReversedString(String s){
        return new StringBuilder(s).reverse().toString();
    }
}