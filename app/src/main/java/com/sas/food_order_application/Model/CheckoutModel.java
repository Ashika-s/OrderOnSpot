package com.sas.food_order_application.Model;

public class CheckoutModel {

    String dishName,dishAmount;
    int dishQuantity,imageID;

    public CheckoutModel(String dishName, String dishAmount, int dishQuantity, int imageID) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.dishQuantity = dishQuantity;
        this.imageID = imageID;
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

    public int getDishQuantity() {
        return dishQuantity;
    }

    public void setDishQuantity(int dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

}