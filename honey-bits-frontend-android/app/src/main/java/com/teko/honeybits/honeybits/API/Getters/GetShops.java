package com.teko.honeybits.honeybits.API.Getters;

import android.os.AsyncTask;
import android.util.Log;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.models.Location;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.Shop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetShops extends AsyncTask<Request, Void, Shop[]> {

    private OnResultReadyListener<Shop[]> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<Shop[]> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Shop[] doInBackground(Request... requests) {
        try {

            HttpGet httpRequest = new HttpGet(requests[0].url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpRequest);

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);


                ArrayList<Shop> shops = new ArrayList<>();
                JSONArray jArray = new JSONArray(data);
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject jShop = jArray.getJSONObject(i);
                    Log.i("SHOP", jShop.toString());
                    JSONObject jShopPicture = jShop.getJSONObject("shop_picture");
                    JSONObject jShopLogo = jShop.getJSONObject("shop_logo");
                    JSONObject jLocation = jShop.getJSONObject("map_location");
                    Shop shop = new Shop(
                            jShop.getString("id"),
                            jShop.getString("name"),
                            jShop.getString("description"),
                            new Picture(
                                    null,
                                    null,
                                    new Picture.SizeSources(
                                            jShopPicture.getString("big"),
                                            jShopPicture.getString("medium"),
                                            jShopPicture.getString("thumb")
                                    )
                            ),
                            new Picture(
                                    null,
                                    null,
                                    new Picture.SizeSources(
                                            jShopLogo.getString("big"),
                                            jShopLogo.getString("medium"),
                                            jShopLogo.getString("thumb")
                                    )

                            ),
                            new Location(
                                    jLocation.getString("place_id"),
                                    jLocation.getDouble("latitude"),
                                    jLocation.getDouble("longitude")
                            ),
                            jShop.getString("policy"),
                            jShop.getString("raiting"),
                            jShop.getBoolean("is_favorite")
                    );

                    shops.add(shop);
                }

                return shops.toArray(new Shop[shops.size()]);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new Shop[0];
    }

    @Override
    protected void onPostExecute(Shop[] shops) {
        super.onPostExecute(shops);
        if (listener != null){
            listener.onResultReady(shops);
        }
    }
}
