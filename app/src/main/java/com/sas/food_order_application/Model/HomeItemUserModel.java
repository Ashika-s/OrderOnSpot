package com.sas.food_order_application.Model;
public class HomeItemUserModel {

    int image;
    String dishName,dishAmount;



    public HomeItemUserModel(String dishName, String dishAmount) {
//        this.image = image;
        this.dishName = dishName;
        this.dishAmount = dishAmount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
