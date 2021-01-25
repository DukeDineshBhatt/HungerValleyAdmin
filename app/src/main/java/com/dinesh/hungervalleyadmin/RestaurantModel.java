package com.dinesh.hungervalleyadmin;

public class RestaurantModel {

    String Status,Restaurant_name;

    public RestaurantModel() {

    }

    public RestaurantModel(String status, String restaurant_name) {
        Status = status;
        Restaurant_name = restaurant_name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRestaurant_name() {
        return Restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        Restaurant_name = restaurant_name;
    }
}
