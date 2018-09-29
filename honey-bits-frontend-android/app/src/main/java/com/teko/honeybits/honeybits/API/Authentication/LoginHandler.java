package com.teko.honeybits.honeybits.API.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.Configuration;
import com.teko.honeybits.honeybits.listeners.SessionListener;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler {

    private SharedPreferences session;
    private Context context;

    public LoginHandler(Context context){
        this.context = context;
        session = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void performLogin(String user, String password){
        Map<String, Object> params = new HashMap<>();
        params.put("email", user);
        params.put("password", password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");

        Request request = new Request("users/token", params, headers);
        SessionListener sessionListener = new SessionListener(session, "TOKEN");
        sessionListener.registerOnResultReadyListener(new OnResultReadyListener<String>() {
            @Override
            public void onResultReady(String object) {
                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage( context.getPackageName() );
                assert i != null;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        AuthCaller authCaller = new AuthCaller();
        authCaller.registerOnResultReadyListener(sessionListener);
        authCaller.execute(request);
    }

    public void verifyToken(){
        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Request request = new Request("")
    }

    public String getToken(){
        String token = session.getString("TOKEN", null);
        if (token == null){
            Configuration.PromptAuth();
        }
        return token;
    }
}
