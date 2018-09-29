package com.teko.honeybits.honeybits.listeners;

import android.content.SharedPreferences;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;

public class SessionListener implements OnResultReadyListener<String> {

    private SharedPreferences session;
    private String key;

    private OnResultReadyListener<String> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<String> listener){
        this.listener = listener;
    }

    public SessionListener(SharedPreferences session, String key) {
        this.session = session;
        this.key = key;
    }

    @Override
    public void onResultReady(String value) {
        SharedPreferences.Editor editor = this.session.edit();
        editor.putString(key, value);
        editor.apply();

        if (listener != null){
            listener.onResultReady(value);
        }
    }
}
