package com.ab.unittesting.basic;

/**
 * @author Arpit Bhardwaj
 */
public class Coffee {

    private final CoffeeType type;
    private final int bean;
    private final int milk;

    public Coffee(CoffeeType type, int bean, int milk) {
        this.type = type;
        this.bean = bean;
        this.milk = milk;
    }

    public CoffeeType getType() {
        return type;
    }

    public int getBean() {
        return bean;
    }

    public int getMilk() {
        return milk;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "type=" + type +
                ", bean=" + bean +
                ", milk=" + milk +
                '}';
    }
}
