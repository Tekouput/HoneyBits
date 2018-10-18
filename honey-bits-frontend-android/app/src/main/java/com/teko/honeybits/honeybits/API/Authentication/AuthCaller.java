package com.teko.honeybits.honeybits.API.Authentication;

import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Webb;
import com.goebl.david.WebbException;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AuthCaller extends AsyncTask<Request, Void, String> {

    private OnResultReadyListener<String> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<String> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Request... requests) {

        Request requestOBJ = requests[0];

        Webb webb = Webb.create();
        JSONObject result = webb
                .post(requestOBJ.url)
                .param("email", requestOBJ.parameters.get("email"))
                .param("password", requestOBJ.parameters.get("password"))
                .asJsonObject()
                .getBody();

        try {
            return result.getString("auth_token");
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);
        if (listener != null){
            listener.onResultReady(token);
        }
    }
}
