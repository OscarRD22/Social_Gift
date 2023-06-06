package com.example.social_gift.model;

import java.io.Serializable;

public class WishListGift implements Serializable {

    private int id;
    private int id_Wish_List;
    private String url_Product;
    private int priority;
    private int booked;


    public WishListGift(int id, int id_Wish_List, String url_Product, int priority, int booked) {
        this.id = id;
        this.id_Wish_List = id_Wish_List;
        this.url_Product = url_Product;
        this.priority = priority;
        this.booked = booked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Wish_List() {
        return id_Wish_List;
    }

    public void setId_Wish_List(int id_Wish_List) {
        this.id_Wish_List = id_Wish_List;
    }

    public String getUrl_Product() {
        return url_Product;
    }

    public void setUrl_Product(String url_Product) {
        this.url_Product = url_Product;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    //Return 1 regalo reservado - Return 0 regalo no reservado
    public int isBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }
}
