package com.company;

import java.util.*;

class InvalidExtraTypeException extends Exception {

}

class InvalidPizzaTypeException extends Exception {

}

class ItemOutOfStockException extends Exception {
    private Item item;

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }
}

class EmptyOrder extends Exception {

}

class ArrayIndexOutOfBоundsException extends Exception {

}

class OrderLockedException extends Exception {

}

interface Item {
    int getPrice();
    String getType();
    int getCount();
    void setCount(int count);
}

class PizzaItem implements Item {
    private String type;
    private int price;
    private int count;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(type.equals("Standard")) {
            this.price = 10;
        }
        else if (type.equals("Pepperoni")) {
            this.price = 12;
        }
        else if (type.equals("Vegetarian")) {
            this.price = 8;
        }
        else {
            throw new InvalidPizzaTypeException();
        }

        this.type = type;
        this.count = 1;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}

class ExtraItem implements Item {
    private String type;
    private int price;
    private int count;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(type.equals("Ketchup")) {
            this.price = 3;
        }
        else if (type.equals("Coke")) {
            this.price = 5;
        }
        else {
            throw new InvalidExtraTypeException();
        }

        this.type = type;
        this.count = 1;
    }
    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}

class Order {
    private List<Item> items;
    private boolean isLocked;

    public Order() {
        items = new ArrayList<Item>();
        isLocked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {

        if(isLocked) {
            throw new OrderLockedException();
        }
        if(count > 10) {
            throw new ItemOutOfStockException(item);
        }

        Item existing = items.stream().filter(x -> x.getType().equals(item.getType())).findFirst().orElse(null);
        if(existing != null) {
            existing.setCount(count);
        }
        else{
            item.setCount(count);
            items.add(item);
        }
    }

    public int getPrice() {
        int price = 0;
        for(Item item : items) {
            price += (item.getPrice() * item.getCount());
        }
        return price;

    }

    void displayOrder() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for(Item item : items) {
            int totalPriceForItem = item.getPrice() * item.getCount();
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", index++, item.getType(), item.getCount(), totalPriceForItem));
        }
        sb.append(String.format("Total:%21d$", getPrice()));
        System.out.println(sb.toString());
    }

    void removeItem(int index) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if(isLocked) {
            throw new OrderLockedException();
        }
        if(index < 0 || index >= items.size()) {
            throw new ArrayIndexOutOfBоundsException();
        }

        items.remove(index);
    }

    void lock() throws EmptyOrder {
        if(items.size() <= 0) {
            throw new EmptyOrder();
        }
        isLocked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}