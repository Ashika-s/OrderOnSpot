package com.sas.food_order_application.Model;
public class HomeItemUserModel {

    int image;
    String dishName,dishAmount,itemsCount;



    public HomeItemUserModel(String dishName, String dishAmount,String itemsCount) {
//        this.image = image;
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.itemsCount=itemsCount;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(String itemsCount) {
        this.itemsCount = itemsCount;
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
