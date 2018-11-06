package com.teko.honeybits.honeybits.API.Getters;

import android.os.AsyncTask;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.models.User;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class GetUser extends AsyncTask<Request, Void, User> {

    private OnResultReadyListener<User> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<User> listener){
        this.listener = listener;
    }

    @Override
    protected User doInBackground(Request... requests) {

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://teko1.servehttp.com:10092/users")
                .get()
                .addHeader("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJleHAiOjE1NDI1NjkyNjh9.F7Gjpo6yOCpKhvCYPV1vrQYF-cS_JSvM61YOCdPpWZk")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (listener != null) {
            listener.onResultReady(user);
        }
    }
}
