package com.teko.honeybits.honeybits.models;

import java.util.Date;

public class User {

    private String id;
    private String first_name;
    private String last_name;
    private Picture profile_pic;
    private String sex;
    private Date birth_day;
    private String email;
    private String role;
    private User[] followers;
    private User[] following;
    private Shop[] favorites;
    private Date date_joined;

}
