package com.teko.honeybits.honeybits.API.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.teko.honeybits.honeybits.API.Getters.GetValidity;
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

    public void performLogin(String user, String password, final View parentLayout){
        Map<String, Object> params = new HashMap<>();
        params.put("email", user);
        params.put("password", password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");

        final Request request = new Request("users/token", params, headers);
        final SessionListener sessionListener = new SessionListener(session, "TOKEN");
        sessionListener.registerOnResultReadyListener(new OnResultReadyListener<String>() {
            @Override
            public void onResultReady(String object) {
                if (object.length() > 0) {
                    Intent i = context.getPackageManager()
                            .getLaunchIntentForPackage(context.getPackageName());
                    assert i != null;
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else {
                    Snackbar.make(parentLayout, "Wrong credentials", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", null)
                            .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }
            }
        });

        AuthCaller authCaller = new AuthCaller();
        authCaller.registerOnResultReadyListener(sessionListener);
        authCaller.execute(request);
    }

    public void verifyToken(String token){
        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        Request request = new Request("validity", params, headers);
        GetValidity getValidity = new GetValidity();
        getValidity.registerOnResultReadyListener(new OnResultReadyListener<Boolean>() {
            @Override
            public void onResultReady(Boolean valid) {
                if (!valid) {

                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("TOKEN", null);
                    editor.apply();

                    Intent i = context.getPackageManager()
                            .getLaunchIntentForPackage(context.getPackageName());
                    assert i != null;
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }
        });
        getValidity.execute(request);
    }

    public String getToken(){
        String token = session.getString("TOKEN", null);
        if (token == null){
            Configuration.PromptAuth();
        }
        return token;
    }

    public boolean tokenAvailable(){
        String token = session.getString("TOKEN", null);
        if (token != null){
            verifyToken(token);
            return true;
        }
        return false;
    }
}
