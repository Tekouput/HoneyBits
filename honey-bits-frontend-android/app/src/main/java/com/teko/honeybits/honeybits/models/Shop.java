package com.teko.honeybits.honeybits.models;

public class Shop {

    private String id;
    private String name;
    private String description;
    private Picture picture;
    private Picture logo;
    private Location location;
    private String policy;
    private String rating;
    public boolean isFavorite;
    private int sales_count;

    public int distance = 0;

    public Shop(String id,
                String name,
                String description,
                Picture picture,
                Picture logo,
                Location location,
                String policy,
                String rating,
                boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.logo = logo;
        this.location = location;
        this.policy = policy;
        this.rating = rating;
        this.isFavorite = isFavorite;
    }

    /*public Shop(String id,
                String name,
                String description,
                Picture picture,
                Picture logo,
                Location location,
                String policy,
                String rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.logo = logo;
        this.location = location;
        this.policy = policy;
        this.rating = rating;
        this.isFavorite = false;
    }*/

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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Picture getLogo() {
        return logo;
    }

    public void setLogo(Picture logo) {
        this.logo = logo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getSales_count() {
        return sales_count;
    }

    public void setSales_count(int sales_count) {
        this.sales_count = sales_count;
    }
}
