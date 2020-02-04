package com.company;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class IntegerArray {
    private int[] numbers;

    public IntegerArray(int[] numbers) {
        this.numbers = Arrays.copyOf(numbers, numbers.length);
    }

    public int length() {
        return this.numbers.length;
    }

    public int getElementAt(int i){
        return this.numbers[i];
    }

    public int sum() {
        return Arrays.stream(numbers).sum();
    }

    public double average() {
        return this.sum() / (double)length();
    }

    public IntegerArray getSorted() {
        int[] sorted = Arrays.copyOf(numbers, numbers.length);
        Arrays.sort(sorted);
        return new IntegerArray(sorted);
    }

    public IntegerArray concat(IntegerArray ia){
        int[] concatenated = new int[this.length() + ia.length()];
        for(int i = 0; i < this.length(); i++){
            concatenated[i] = this.numbers[i];
        }
        int index = 0;
        for(int i = this.length(); i < this.length() + ia.length(); i++){
            concatenated[i] = ia.getElementAt(index++);
        }

        return new IntegerArray(concatenated);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < this.numbers.length - 1; i++){
            sb.append(this.numbers[i] + ", ");
        }
        sb.append(this.numbers[this.numbers.length - 1]);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;

        IntegerArray compare = (IntegerArray)obj;

        if(compare == null) return false;
        if(length() != compare.length()) return false;

        for(int i = 0; i < length(); i++){
            if(numbers[i] != compare.getElementAt(i))
                return false;
        }

        return true;
    }
}

class ArrayReader{
    public static IntegerArray readIntegerArray(InputStream input) {
        Scanner scanner = new Scanner(input);
        String[] numbers = scanner.nextLine().split(" ");
        int n = Integer.parseInt(numbers[0]);
        if(numbers.length == 1){
            int[] array = new int[n];
            String[] numbers2 = scanner.nextLine().split(" ");
            for(int i = 0 ; i < n; i++){
                array[i] = Integer.parseInt(numbers2[i]);
            }
            return new IntegerArray(array);
        }
        int[] array = new int[n];
        for(int i = 0 ; i < n; i++){
            array[i] = Integer.parseInt(numbers[i + 1]);
        }

        return new IntegerArray(array);
    }
}

public class IntegerArrayTester {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        IntegerArray ia = null;
        switch (s) {
            case "testSimpleMethods":
                ia = new IntegerArray(generateRandomArray(scanner.nextInt()));
                testSimpleMethods(ia);
                break;
            case "testConcat":
                testConcat(scanner);
                break;
            case "testEquals":
                testEquals(scanner);
                break;
            case "testSorting":
                testSorting(scanner);
                break;
            case "testReading":
                testReading(new ByteArrayInputStream(scanner.nextLine().getBytes()));
                break;
            case "testImmutability":
                int a[] = generateRandomArray(scanner.nextInt());
                ia = new IntegerArray(a);
                testSimpleMethods(ia);
                testSimpleMethods(ia);
                IntegerArray sorted_ia = ia.getSorted();
                testSimpleMethods(ia);
                testSimpleMethods(sorted_ia);
                sorted_ia.getSorted();
                testSimpleMethods(sorted_ia);
                testSimpleMethods(ia);
                a[0] += 2;
                testSimpleMethods(ia);
                ia = ArrayReader.readIntegerArray(new ByteArrayInputStream(integerArrayToString(ia).getBytes()));
                testSimpleMethods(ia);
                break;
        }
        scanner.close();
    }

    static void testReading(InputStream in) throws IOException{
        IntegerArray read = ArrayReader.readIntegerArray(in);
        System.out.println(read);
    }

    static void testSorting(Scanner scanner) {
        int[] a = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        System.out.println(ia.getSorted());
    }

    static void testEquals(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        int[] c = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        IntegerArray ib = new IntegerArray(b);
        IntegerArray ic = new IntegerArray(c);
        System.out.println(ia.equals(ib));
        System.out.println(ia.equals(ic));
        System.out.println(ib.equals(ic));
    }

    static void testConcat(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        IntegerArray array1 = new IntegerArray(a);
        IntegerArray array2 = new IntegerArray(b);
        IntegerArray concatenated = array1.concat(array2);
        System.out.println(concatenated);
    }

    static void testSimpleMethods(IntegerArray ia) {
        System.out.print(integerArrayToString(ia));
        System.out.println(ia);
        System.out.println(ia.sum());
        System.out.printf("%.2f\n", ia.average());
    }


    static String integerArrayToString(IntegerArray ia) {
        StringBuilder sb = new StringBuilder();
        sb.append(ia.length()).append('\n');
        for (int i = 0; i < ia.length(); ++i)
            sb.append(ia.getElementAt(i)).append(' ');
        sb.append('\n');
        return sb.toString();
    }

    static int[] readArray(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        return a;
    }


    static int[] generateRandomArray(int k) {
        Random rnd = new Random(k);
        int n = rnd.nextInt(8) + 2;
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = rnd.nextInt(20) - 5;
        }
        return a;
    }

}
