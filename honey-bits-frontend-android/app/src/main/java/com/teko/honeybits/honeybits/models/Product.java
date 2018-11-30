package com.teko.honeybits.honeybits.models;

import android.widget.TextView;

import java.util.ArrayList;

public class Product {

    private String id;
    private String name;
    private String description;
    private Shop shop;
    public boolean isFavorite;
    private Price price;
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Picture> picture = new ArrayList<>();

    public int amount = 1;

    public Product(String id, String name, String description, Shop shop, boolean isFavorite, Price price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shop = shop;
        this.isFavorite = isFavorite;
        this.price = price;
    }

    public Product(String id, String name, String description, Shop shop, Price price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shop = shop;
        this.isFavorite = false;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Price getPrice() {
        return price;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Picture> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<Picture> picture) {
        this.picture = picture;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
