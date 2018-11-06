package com.teko.honeybits.honeybits.API.Getters;

import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Webb;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class GetValidity extends AsyncTask<Request, Void, Boolean> {

    private OnResultReadyListener<Boolean> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<Boolean> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Request... requests) {
        Request requestOBJ = requests[0];

        Webb webb = Webb.create();
        JSONObject result = webb
                .get(requestOBJ.url)
                .header("Authorization", requestOBJ.headers.get("Authorization"))
                .asJsonObject()
                .getBody();

        try {
            return result.getBoolean("valid");
        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
            Log.e("GET VALIDITY ERROR: ", e.getMessage());
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null){
            listener.onResultReady(aBoolean);
        }
    }
}
