package com.sas.food_order_application.user;

import java.util.HashMap;

public class OrderDetails {

    String RestName,userEmail;
    int TotalAmt,tableNo;
    int Id;
    HashMap<String, Integer> hashMapItems=new HashMap<>();

    public String getRestName() {
        return RestName;
    }

    public void setRestName(String restName) {
        RestName = restName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getTableNo() {
        return tableNo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public int getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(int totalAmt) {
        TotalAmt = totalAmt;
    }

    public HashMap<String, Integer> getHashMapItems() {
        return hashMapItems;
    }

    public void setHashMapItems(HashMap<String, Integer> hashMapItems) {
        this.hashMapItems = hashMapItems;
    }

    public OrderDetails(int i, String restName, String userEmail, int tableNo, int totalAmt, HashMap<String, Integer> hashMapItems) {
        this.Id=i;
        this.RestName = restName;
        this.userEmail = userEmail;
        this.tableNo = tableNo;
        this.TotalAmt = totalAmt;
        this.hashMapItems = hashMapItems;
    }
}
