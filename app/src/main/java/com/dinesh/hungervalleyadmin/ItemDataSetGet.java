package com.dinesh.hungervalleyadmin;

public class ItemDataSetGet {

    String pName, price, quantity;

    public ItemDataSetGet() {

    }

    public ItemDataSetGet(String pName, String price, String quantity) {
        this.price = price;
        this.quantity = quantity;
        this.pName = pName;


    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
