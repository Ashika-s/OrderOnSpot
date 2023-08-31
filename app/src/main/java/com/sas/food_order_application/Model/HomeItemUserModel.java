package com.sas.food_order_application.Model;

import android.graphics.Bitmap;

public class HomeItemUserModel {


    String dishName,dishAmount,itemsCount,type;
    Bitmap imageurl;

    public HomeItemUserModel(String dishName, String dishAmount, String itemsCount, String type, Bitmap imageurl) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.itemsCount = itemsCount;
        this.type = type;
        this.imageurl = imageurl;
    }

    public HomeItemUserModel(String dishName, String dishAmount, String itemsCount, String type) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.itemsCount = itemsCount;
        this.type = type;
    }

    public Bitmap getImageurl() {
        return imageurl;
    }

    public void setImageurl(Bitmap imageurl) {
        this.imageurl = imageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(String itemsCount) {
        this.itemsCount = itemsCount;
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
}
