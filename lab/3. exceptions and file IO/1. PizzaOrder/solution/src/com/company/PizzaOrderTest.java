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

class Menu {
    public static Map<String, Integer> PizzaItems = new HashMap<String, Integer>() {
        {
            put("Standard", 10);
            put("Pepperoni", 12);
            put("Vegetarian", 8);
        }
    };

    public static Map<String, Integer> ExtraItems = new HashMap<String, Integer>() {
        {
            put("Ketchup", 3);
            put("Coke", 5);
        }
    };
}

abstract class OrderItem implements Item {
    protected String type;
    protected int price;
    protected int count;

    protected OrderItem() {
        this.count = 0;
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

class PizzaItem extends OrderItem {
    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(!Menu.PizzaItems.containsKey(type)) {
            throw new InvalidPizzaTypeException();
        }

        this.type = type;
        this.price = Menu.PizzaItems.get(type);
    }
}

class ExtraItem extends OrderItem {
    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(!Menu.ExtraItems.containsKey(type)) {
            throw new InvalidExtraTypeException();
        }

        this.type = type;
        this.price = Menu.ExtraItems.get(type);
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

        Item existing = getItemByType(item.getType());
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

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for(Item item : items) {
            int totalPriceForItem = item.getPrice() * item.getCount();
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", index++, item.getType(), item.getCount(), totalPriceForItem));
        }
        sb.append(String.format("Total:%21d$", getPrice()));
        System.out.println(sb.toString());
    }

    public void removeItem(int index) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if(isLocked) {
            throw new OrderLockedException();
        }
        if(index < 0 || index >= items.size()) {
            throw new ArrayIndexOutOfBоundsException();
        }

        items.remove(index);
    }

    public void lock() throws EmptyOrder {
        if(items.size() <= 0) {
            throw new EmptyOrder();
        }
        isLocked = true;
    }

    private Item getItemByType(String type) {
        return items
                .stream()
                .filter(x -> x.getType().equals(type))
                .findFirst()
                .orElse(null);
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