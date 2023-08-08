package com.sas.food_order_application.Model;

public class Item {


    String ItemName;
    int image;
    String Amount;
    String Type;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Item(String itemName, int image, String amount, String type) {

        ItemName = itemName;
        this.image = image;
        Amount = amount;
        Type = type;
    }

    public Item() {
    }
}
