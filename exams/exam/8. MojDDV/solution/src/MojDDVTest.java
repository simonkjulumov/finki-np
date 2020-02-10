import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}

class AmountNotAllowedException extends Exception {
    private String amount;

    public AmountNotAllowedException(String amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return String.format("Receipt with amount %s is not allowed to be scanned", amount);
    }
}

enum TaxType {
    A,
    B,
    V
}

class Item {
    private int price;
    private double taxPercent;

    public Item(int price, String taxType) {
        this.price = price;
        setTaxPercent(taxType);
    }

    public int getPrice() {
        return price;
    }

    public double getTaxMoneyAmount() {
        if(taxPercent == 0)
            return 0;

        return price * (taxPercent / 100.00);
    }

    private void setTaxPercent(String taxType) {
        if(taxType.equals("A")) {
            taxPercent = 18.00;
        }
        else if(taxType.equals("B")){
            taxPercent = 5.00;
        }
        else {
            taxPercent = 0;
        }
    }
}

class Receipt {
    private int id;
    private ArrayList<Item> items;
    private double returnTaxPercent;
    private int sumOfAmounts;
    private int maximumAllowedSumOfAmounts;

    public Receipt(int id) {
        this.id = id;
        this.items = new ArrayList<>();
        returnTaxPercent = 15.0;
        sumOfAmounts = 0;
        maximumAllowedSumOfAmounts = 30000;
    }

    public void addItem(Item item) {
        items.add(item);
        sumOfAmounts += item.getPrice();
    }

    public double taxReturn() {
        double taxMoneyAmount = 0;
        for(Item item : items) {
            taxMoneyAmount += item.getTaxMoneyAmount();
        }

        return taxMoneyAmount * (returnTaxPercent / 100.0);
    }

    public int getSumOfAmounts() {
        return sumOfAmounts;
    }

    @Override
    public String toString() {
        return String.format("%5d\t%d\t%.5f", id, sumOfAmounts, taxReturn());
    }
}

class MojDDV {
    private ArrayList<Receipt> receipts;

    public MojDDV() {
        receipts = new ArrayList<>();
    }

    private void scanReceipt(Receipt receipt) throws AmountNotAllowedException {
        if(receipt.getSumOfAmounts() > 30000){
            throw new AmountNotAllowedException(String.valueOf(receipt.getSumOfAmounts()));
        }
        receipts.add(receipt);
    }

    public void readRecords(InputStream inputStream) {
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        while(scanner.hasNext()){
            String[] receiptItems = scanner.nextLine().split(Pattern.quote(" "));
            int id = Integer.parseInt(receiptItems[0]);
            Receipt receipt = new Receipt(id);
            for(int i = 1; i < receiptItems.length - 1; i+=2) {
                receipt.addItem(new Item(Integer.parseInt(receiptItems[i]), receiptItems[i+1]));
            }

            try{
                scanReceipt(receipt);
            }
            catch(AmountNotAllowedException a) {
                System.out.println(a.getMessage());
            }
        }
        scanner.close();
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        for(Receipt receipt : receipts) {
            printWriter.println(receipt.toString());
        }
        printWriter.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

        Double max = receipts.stream().map(Receipt::taxReturn).max(Double::compareTo).get();
        Double min = receipts.stream().map(Receipt::taxReturn).min(Double::compareTo).get();
        Double sum = receipts.stream().map(Receipt::taxReturn).mapToDouble(Double::doubleValue).sum();
        int count = receipts.size();
        double average = sum / count;

        printWriter.println(String.format("min: %.3f", min));
        printWriter.println(String.format("max: %.3f", max));
        printWriter.println(String.format("sum: %.3f", sum));
        printWriter.println(String.format("count: %d", count));
        printWriter.println(String.format("avg: %-3.3f", average));

        printWriter.flush();
    }
}