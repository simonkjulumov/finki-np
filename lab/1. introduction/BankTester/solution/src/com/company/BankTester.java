package com.company;
import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

class Helpers {
    public static double moneyToNumeric(String money){
        //money format is "20.34$"
        //String[] split = money.split(Pattern.quote("."));
        //double first = Double.parseDouble(split[0]);
        //double second = Double.parseDouble(split[1].split(Pattern.quote("$"))[0]);
        //return first + second;
        //THE ABOVE CODE WAS CAUSING PROBLEMS - NOT TRANSFORMING CORRECTLY IN SOME CASES, DONT USE IT!!

        return Double.parseDouble(money.substring(0,money.length()-1));
    }

    public static String numericToMoney(double money){
        return String.format("%.2f$", money).replace(",",".");
        //return new DecimalFormat("0.00").format(money) + "$";
    }
}

class Account {
    private long id;
    private String name;
    private double balance;

    public Account(String name, String balance) {
        this.id = new Random().nextLong();
        this.name = name;
        setBalance(balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %s\n", this.name, this.getBalanceString());
    }

    @Override
    public boolean equals(Object obj) {
        Account account = (Account)obj;
        return obj != null && this.id == account.getId() && this.name == account.getName() && this.balance == account.getBalance();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBalanceString(){
        return Helpers.numericToMoney(this.balance);
    }

    public double getBalance(){
        return this.balance;
    }

    public void setBalance(String balance){
        this.balance = Helpers.moneyToNumeric(balance);
    }

    public void addFunds(double amount){ this.balance += amount; }

    public void subtractFunds(double amount){ this.balance -= amount; }
}

abstract class Transaction {
    private long id;

    protected double fromId;
    protected double toId;
    protected String description;
    protected double amount;

    protected Transaction(double fromId, double toId, String description, String amount) {
        this.id = new Random().nextLong();
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = Helpers.moneyToNumeric(amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }

    @Override
    public boolean equals(Object obj) {
        Transaction transaction = (Transaction)obj;
        return obj != null && this.id == transaction.getId();
    }

    public abstract double getProvision();

    public long getId() { return this.id; }

    public double getFromId() {
        return this.fromId;
    }

    public double getToId() {
        return this.toId;
    }

    public String getDescription() { return this.description; }

    public String getAmount() {
        return Helpers.numericToMoney(this.amount);
    }

    public double getAmountNumeric() {
        return this.amount;
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private double flatProvision;

    public FlatAmountProvisionTransaction(double fromId, double toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = Helpers.moneyToNumeric(flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatProvision);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public double getProvision() { return this.flatProvision; }
}

class FlatPercentProvisionTransaction extends Transaction {
    private int flatPercent;

    public FlatPercentProvisionTransaction(double fromId, double toId, String amount, int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.flatPercent = centsPerDollar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatPercent);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public double getProvision(){
        // rounder = 15
        // percent = 5
        // 5 %
        //double rounded = Math.round(this.amount);
        return flatPercent / 100.0 * (int)super.getAmountNumeric();
    }
}

class Bank {
    private String name;
    private Account[] accounts;
    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account accounts[]) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.totalTransfers = 0;
        this.totalProvision = 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accounts, totalTransfers, totalProvision);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n\n", this.name));
        for(Account account : this.accounts){
            sb.append(account.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Bank other = (Bank) obj;

        if (name != other.getName()) {
            return false;
        }
        if (accounts.length != other.getAccounts().length) {
            return false;
        }
        for (int i = 0; i < accounts.length; i++) {
            if (!accounts[i].equals(other.getAccounts()[i])) {
                return false;
            }
        }
        if (!totalTransfers().equals(other.totalTransfers())) {
            return false;
        }
        if (!totalProvision().equals(other.totalProvision())) {
            return false;
        }
        return true;
    }

    public String getName(){
        return this.name;
    }

    public Account[] getAccounts() {
        return this.accounts;
    }

    public boolean makeTransaction(Transaction t) {
        Account from = Arrays.asList(this.accounts)
                .stream()
                .filter(x -> x.getId() == t.getFromId())
                .findFirst()
                .orElse(null);
        Account to = Arrays.asList(this.accounts)
                .stream()
                .filter(x -> x.getId() == t.getToId())
                .findFirst()
                .orElse(null);

        double totalAmount = t.getAmountNumeric() + t.getProvision();

        if(from == null || to == null) return false;
        if(from.getBalance() < totalAmount) return false;

        from.subtractFunds(totalAmount);
        to.addFunds(t.getAmountNumeric());

        this.totalProvision += t.getProvision();
        this.totalTransfers += t.getAmountNumeric();

        return true;
    }

    public String totalTransfers() {
       return Helpers.numericToMoney(this.totalTransfers);
    }

    public String totalProvision() {
        return Helpers.numericToMoney(this.totalProvision);
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}

