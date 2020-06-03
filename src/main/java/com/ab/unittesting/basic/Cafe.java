package com.ab.unittesting.basic;

/**
 * @author Arpit Bhardwaj
 */
public class Cafe {

    private int beanInStock = 0;
    private int milkInStock = 0;

    public Coffee brew(CoffeeType coffeeType){
        return brew(coffeeType,1);
    }

    private Coffee brew(CoffeeType coffeeType, int quantity) {
        int requiredBeans = coffeeType.getRequiredBeans() * quantity;
        int requiredMilk = coffeeType.getRequiredMilk() * quantity;

        if(requiredBeans > beanInStock || requiredMilk > milkInStock){
            throw new IllegalStateException("Insufficient beans and milk in stock");
        }

        beanInStock -= requiredBeans;
        milkInStock -= requiredMilk;

        return new Coffee(coffeeType,requiredBeans,requiredMilk);
    }

    public void reStockBeans(int weightInGrams){
        beanInStock += weightInGrams;
    }
    public void reStockMilk(int weightInGrams){
        milkInStock += weightInGrams;
    }

    public int getBeanInStock() {
        return beanInStock;
    }

    public int getMilkInStock() {
        return milkInStock;
    }
}
