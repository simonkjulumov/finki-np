package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}

class RomanConverter {
    private static Map<Integer, String> romanNumbers = new HashMap<Integer, String>()
    {{
        put(1, "I");
        put(4, "IV");
        put(5, "V");
        put(9, "IX");
        put(10, "X");
        put(40, "XL");
        put(50, "L");
        put(90, "XC");
        put(100, "C");
        put(400, "CD");
        put(500, "D");
        put(900, "CM");
        put(1000, "M");
    }};

    // your solution here
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        StringBuilder romanNumber = new StringBuilder();
        while(n > 0){
            if(n >= 1000){
                romanNumber.append(romanNumbers.get(1000));
                n = n - 1000;
            }
            else if (n >= 900 && n < 1000) {
                romanNumber.append(romanNumbers.get(900));
                n = n - 900;
            }
            else if(n >= 500 && n < 900){
                romanNumber.append(romanNumbers.get(500));
                n = n - 500;
            }
            else if(n >= 400 && n < 500){
                romanNumber.append(romanNumbers.get(400));
                n = n - 400;
            }
            else if(n >= 100 && n < 400){
                romanNumber.append(romanNumbers.get(100));
                n = n - 100;
            }
            else if(n >= 90 && n < 100){
                romanNumber.append(romanNumbers.get(90));
                n = n - 90;
            }
            else if(n >= 50 && n < 90){
                romanNumber.append(romanNumbers.get(50));
                n = n - 50;
            }
            else if(n >= 40 && n < 50){
                romanNumber.append(romanNumbers.get(40));
                n = n - 40;
            }
            else if(n >= 10 && n < 40){
                romanNumber.append(romanNumbers.get(10));
                n = n - 10;
            }
            else if(n == 9){
                romanNumber.append(romanNumbers.get(9));
                n = n - 9;
            }
            else if(n >= 5 && n < 9){
                romanNumber.append(romanNumbers.get(5));
                n = n - 5;
            }
            else if(n == 4){
                romanNumber.append(romanNumbers.get(4));
                n = n - 4;
            }
            else {
                romanNumber.append(romanNumbers.get(1));
                n = n - 1;
            }
        }
        return romanNumber.toString();
    }   

}
