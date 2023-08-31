package com.sas.food_order_application.Model;

import android.graphics.Bitmap;

public class CheckoutModel {

    String dishName,dishAmount,dishQuantity;
   Bitmap imageurl;

    public CheckoutModel(String dishName, String dishAmount, String dishQuantity) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.dishQuantity = dishQuantity;
    }

    public CheckoutModel(String dishName, String dishAmount, String dishQuantity, Bitmap imageurl) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.dishQuantity = dishQuantity;
        this.imageurl = imageurl;
    }

    public Bitmap getImageurl() {
        return imageurl;
    }

    public void setImageurl(Bitmap imageurl) {
        this.imageurl = imageurl;
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