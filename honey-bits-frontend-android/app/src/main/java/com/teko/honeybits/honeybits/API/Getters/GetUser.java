package com.teko.honeybits.honeybits.API.Getters;

import android.content.Context;
import android.os.AsyncTask;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.models.Avatar;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class GetUser extends AsyncTask<Request, Void, User> {

    private OnResultReadyListener<User> listener;
    private Context context;

    public GetUser (Context context) {
        this.context = context;
    }

    public void registerOnResultReadyListener(OnResultReadyListener<User> listener){
        this.listener = listener;
    }

    @Override
    protected User doInBackground(Request... requests) {

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://104.248.61.12/users")
                .get()
                .addHeader("Authorization", new LoginHandler(context).getToken())
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonUser = new JSONObject(response.body().string());
            User user = new User(
                    jsonUser.getString("id"),
                    jsonUser.getString("first_name"),
                    jsonUser.getString("last_name"),
                    new Avatar(jsonUser.getString("profile_pic")),
                    jsonUser.getString("sex"),
                    null,
                    jsonUser.getString("email"),
                    jsonUser.getString("role"),
                    jsonUser.getInt("followers"),
                    jsonUser.getInt("following"),
                    jsonUser.getInt("favorites"),
                    null
            );

            return user;
        } catch (IOException | JSONException e) {
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
