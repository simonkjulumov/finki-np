package com.company;

import java.util.Scanner;
import java.util.LinkedList;

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

class ResizableArray<T> {

    private T[] elements;
    private int size;
    private int length;

    public ResizableArray() {
        elements = (T[]) new Object[10];
        size = 10;
        length = 0;
    }

    public void addElement(T element) {
        if (size == length) {
            size *=2;
            T[] temp = (T[]) new Object[size];
            copy(temp,elements);
            elements = temp;
        }
        elements[length++] = element;
    }

    private void copy(T[]a, T[] b) {
        for (int i=0; i<b.length; ++i)
            a[i] = b[i];
    }

    public boolean removeElement(T element) {
        int index = this.find(element);
        if (index == -1)
            return false;
        for (int i=index; i<length-1;++i) {
            elements[i] = elements[i+1];
        }
        --length;
        return true;
    }

    private int find(T element) {
        for (int i=0; i<length; ++i) {
            if (elements[i].equals(element))
                return i;
        }
        return -1;
    }

    public boolean contains(T element) {
        if (this.find(element) != -1)
            return true;
        return false;
    }

    public Object[] toArray() {
        T[] arr = (T[]) new Object[length];
        for (int i=0; i<length; ++i)
            arr[i] = elements[i];
        return arr;
    }

    public boolean isEmpty() {
        return length ==0;
    }

    public int count() {
        return length;
    }

    public T elementAt(int index)  {
        if (index >= length || index < 0)
            throw new ArrayIndexOutOfBoundsException();
        return elements[index];
    }
	
	/*public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src)  {
		
		for (int i=0; i< src.count() ; ++i)
			dest.addElement(src.elementAt(i));
		
	}*/

    static<T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        for(int i = 0, len = src.count(); i<len; i++){
            dest.addElement(src.elementAt(i));
        }
    }


}

class IntegerArray extends ResizableArray<Integer> {

    public IntegerArray() {
        super();
    }

    public double sum()  {
        double sum=0;
        for (int i=0; i<count(); ++i) {
            sum += elementAt(i);
        }
        return sum;
    }

    public double mean()  {
        return sum() / count();
    }

    public int countNonZero()  {
        int counter = 0;
        for (int i=0; i<count(); ++i) {
            if (elementAt(i) != 0)
                ++counter;
        }
        return counter;
    }

    public IntegerArray distinct() {
        IntegerArray arr = new IntegerArray();
        for (int i=0; i<this.count(); ++i) {
            if (!(arr.contains(elementAt(i)))) {
                arr.addElement(elementAt(i));
            }
        }
        return arr;
    }

    public IntegerArray increment(int offset) {
        IntegerArray temp = new IntegerArray();
        for (int i=0; i<count(); ++i) {
            temp.addElement(elementAt(i) + offset);
        }
        return temp;

    }

}