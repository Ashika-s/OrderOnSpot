package com.sas.food_order_application.Model;

public class CheckoutModel {

    String dishName,dishAmount,dishQuantity;
    int imageID;

    public CheckoutModel(String dishName, String dishAmount, String dishQuantity) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.dishQuantity = dishQuantity;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(String dishAmount) {
        this.dishAmount = dishAmount;
    }

    public String getDishQuantity() {
        return dishQuantity;
    }

    public void setDishQuantity(String dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

}