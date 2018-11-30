package com.teko.honeybits.honeybits.API.Getters;

import android.os.AsyncTask;
import android.util.Log;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.models.Location;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.Price;
import com.teko.honeybits.honeybits.models.Product;
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

public class GetProducts extends AsyncTask<Request, Void, Product[]> {

    private OnResultReadyListener<Product[]> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<Product[]> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Product[] doInBackground(Request... requests) {
        try {

            HttpGet httpRequest = new HttpGet(requests[0].url);

            for (int i = 0; i < requests[0].headers.size(); i++) {
                System.out.println(requests[0].headers.get("Authorization"));
                httpRequest.setHeader("Authorization", requests[0].headers.get("Authorization"));
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpRequest);

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);


                ArrayList<Product> products = new ArrayList<>();
                JSONArray jArray = new JSONArray(data);
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject jObject = jArray.getJSONObject(i);
                    Log.i("Product", jObject.toString());

                    JSONObject jShop = jObject.getJSONObject("shop");
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

                    JSONObject jPrice = jObject.getJSONObject("price");
                    Product product = new Product(
                            jObject.getString("id"),
                            jObject.getString("name"),
                            jObject.getString("description"),
                            shop,
                            jObject.getBoolean("is_favorite"),
                            new Price(
                                    jPrice.getString("raw"),
                                    jPrice.getString("formatted")
                            )
                    );

                    JSONArray jPictures = jObject.getJSONArray("pictures");
                    for (int j = 0; j < jPictures.length(); j++){
                        JSONObject jPicture = jPictures.getJSONObject(j);
                        JSONObject jUrls = jPicture.getJSONObject("urls");
                        Log.i("Test", jUrls.toString());
                        Picture picture = new Picture(
                                jPicture.getString("id"),
                                jPicture.getString("product"),
                                new Picture.SizeSources(
                                        jUrls.getString("big"),
                                        jUrls.getString("medium"),
                                        jUrls.getString("thumb")
                                )

                        );
                        product.getPicture().add(picture);
                    }

                    products.add(product);
                }

                return products.toArray(new Product[products.size()]);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new Product[0];
    }

    @Override
    protected void onPostExecute(Product[] products) {
        super.onPostExecute(products);
        if (listener != null){
            listener.onResultReady(products);
        }
    }
}