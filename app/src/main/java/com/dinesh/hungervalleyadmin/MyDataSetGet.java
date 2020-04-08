package com.dinesh.hungervalleyadmin;

public class MyDataSetGet {
    String name, status, image_url;

    public MyDataSetGet() {

    }

    public MyDataSetGet(String name, String status, String image_url) {
        this.name = name;
        this.status = status;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
