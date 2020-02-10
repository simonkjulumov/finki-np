//package com.company;
import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException () {

    }

    @Override
    public String getMessage() {
        return "Denominator cannot be zero";
    }
}
class GenericFraction<T extends Number, U extends Number>{
    private T numerator;
    private U denominator;

    public GenericFraction (T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.doubleValue() <= 0)
            throw new ZeroDenominatorException();

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public T getNumerator () {
        return numerator;
    }

    public U getDenominator () {
        return denominator;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> genericFraction) throws ZeroDenominatorException {
        double commonDenominator = commonDenominator(denominator.doubleValue(), genericFraction.getDenominator().doubleValue());
        // nom = denom/denom1*nom1 + denom/denom2*nom2.
        return new GenericFraction<Double, Double>(
                commonDenominator/denominator.doubleValue()*numerator.doubleValue(),
                commonDenominator/genericFraction.getDenominator().doubleValue() * genericFraction.getNumerator().doubleValue());
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString () {
        return String.format("%.2f",numerator) + " / " + String.format("%.2f",denominator);
    }

    private double commonDenominator(double a, double b){
        double lcm = (a > b) ? a : b;
        while(true)
        {
            if( lcm % a == 0&&lcm % b == 0 )
            {
                break;
            }
            ++lcm;
        }
        return lcm;
    }
}