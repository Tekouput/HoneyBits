package com.teko.honeybits.honeybits.API.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.teko.honeybits.honeybits.Configuration;

public class LoginHandler {

    private SharedPreferences session;

    public LoginHandler(Context context){
        session = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void performLogin(String user, String password){

    }

    public String getToken(){
        String token = session.getString("TOKEN", null);
        if (token == null){
            Configuration.PromptAuth();
        }
        return token;
    }

}
