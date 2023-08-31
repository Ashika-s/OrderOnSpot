package com.sas.food_order_application.Model;

import java.util.Map;

public class FeedbackModel {

    private float rating;
    private String message;
    private String Restaurantname;
    private Map<String, Object> receivedMap;


    public FeedbackModel(float rating, String message, String restaurantname, Map<String, Object> receivedMap, int orderid) {
        this.rating = rating;
        this.message = message;
        Restaurantname = restaurantname;
        this.receivedMap = receivedMap;
        this.orderid = orderid;
    }
    public void setReceivedMap(Map<String, Object> receivedMap) {
        this.receivedMap = receivedMap;
    }
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    private int orderid;



    public String getRestaurantname() {
        return Restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        Restaurantname = restaurantname;
    }

    public float getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
