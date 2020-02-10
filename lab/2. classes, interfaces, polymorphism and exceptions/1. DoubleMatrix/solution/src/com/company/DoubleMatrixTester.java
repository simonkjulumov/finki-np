package com.company;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

class InsufficientElementsException extends Exception {
    private String message;

    public InsufficientElementsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

class InvalidRowNumberException extends Exception {
    private String message;

    public InvalidRowNumberException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

class InvalidColumnNumberException extends Exception {
    private String message;

    public InvalidColumnNumberException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

class DoubleMatrix {
    private double[][] matrix;
    private int m;
    private int n;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        if(a.length < (m * n)) {
            throw new InsufficientElementsException("Insufficient number of elements");
        }

        matrix = new double[m][n];
        this.m = m;
        this.n = n;

        int index = a.length > (m * n)
                ? a.length - (m * n)
                : 0;
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                matrix[i][j] = a[index++];
            }
        }
    }

    public String getDimensions() {
        return String.format("[%d x %d]", m, n);
    }

    public int rows() {
        return m;
    }

    public int columns() {
        return n;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row > m || row < 1)
            throw new InvalidRowNumberException("Invalid row number");

        double maxElement = -999999999;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(maxElement < matrix[row - 1][j]) {
                    maxElement = matrix[row - 1][j];
                }
            }
        }

        return maxElement;
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column > n || column < 1) {
            throw new InvalidColumnNumberException("Invalid column number");
        }

        double maxElement = -999999999;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(maxElement < matrix[i][column - 1]) {
                    maxElement = matrix[i][column - 1];
                }
            }
        }

        return maxElement;
    }

    public double sum() {
        double sum = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                sum += matrix[i][j];
            }
        }

        return sum;
    }

    public double[] toSortedArray() {
        double[] sorted = new double[m * n];
        int index = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                sorted[index++] = matrix[i][j];
            }
        }

        for(int i = 0; i < m * n; i++){
            for(int j = i + 1; j < m * n; j++){
                if(sorted[i] < sorted[j]) {
                    double temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }
        return sorted;
    }

    public double getElementAt(int i, int j){
        return matrix[i][j];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(j < n - 1)
                    sb.append(String.format("%.2f\t", matrix[i][j]));
                else
                    sb.append(String.format("%.2f", matrix[i][j]));
            }
            if(i < m - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        DoubleMatrix clone = (DoubleMatrix)obj;
        if(m != clone.rows() || n != clone.columns()) return false;

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(this.matrix[i][j] != clone.getElementAt(i, j))
                    return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + m;
        result = prime * result + Arrays.deepHashCode(matrix);
        result = prime * result + n;
        return result;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) {
        Scanner scan = new Scanner(input);
        int m = scan.nextInt();
        int n = scan.nextInt();
        double[] numbers = new double[m * n];
        for(int i = 0; i < m * n; i++) {
            numbers[i] = scan.nextDouble();
        }
        try{
            DoubleMatrix matrix = new DoubleMatrix(numbers, m, n);
            return matrix;
        } catch(InsufficientElementsException e){

        }
        return null;
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
