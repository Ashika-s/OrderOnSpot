package com.sas.food_order_application.admin;

public class Categoryclass {

    String item;
    String category;
    String type;
    String amount;

    public Categoryclass(String data) {
        this.category=data;
    }
    public Categoryclass() {

    }


    public Categoryclass(String item, String category, String type, String amount) {
        this.item = item;
        this.category = category;
        this.type = type;
        this.amount = amount;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
