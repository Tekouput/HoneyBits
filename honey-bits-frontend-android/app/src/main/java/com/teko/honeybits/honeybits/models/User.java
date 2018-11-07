package com.teko.honeybits.honeybits.models;

import java.util.Date;

public class User {

    private String id;
    private String first_name;
    private String last_name;
    private Avatar profile_pic;
    private String sex;
    private Date birth_day;
    private String email;
    private String role;
    private int followers;
    private int following;
    private int favorites;
    private Date date_joined;

    public User(String id, String first_name, String last_name, Avatar profile_pic, String sex, Date birth_day, String email, String role, int followers, int following, int favorites, Date date_joined) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_pic = profile_pic;
        this.sex = sex;
        this.birth_day = birth_day;
        this.email = email;
        this.role = role;
        this.followers = followers;
        this.following = following;
        this.favorites = favorites;
        this.date_joined = date_joined;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Avatar getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Avatar profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(Date birth_day) {
        this.birth_day = birth_day;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public Date getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }
}
