package com.sas.food_order_application.Model;
public class HomeItemUserModel {

    int image;
    String dishName,dishAmount,itemsCount,type;



    public HomeItemUserModel(String dishName, String dishAmount, String itemsCount, String type) {
        this.dishName = dishName;
        this.dishAmount = dishAmount;
        this.itemsCount=itemsCount;
        this.type=type;
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
