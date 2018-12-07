package com.teko.honeybits.honeybits;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Location;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.Price;
import com.teko.honeybits.honeybits.models.Product;
import com.teko.honeybits.honeybits.models.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private String storeId;

    private ImageView logo;
    private ImageView image;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        setTitle("Store profile");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);
        TextView title = findViewById(R.id.tvTitle);
        title.setText("Store profile");

        logo = findViewById(R.id.logo);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);

        storeId = getIntent().getStringExtra("STORE_ID");

        new GetProductInformation().execute();

    }

    private class GetProductInformation extends AsyncTask<Integer, Void, Object> {

        @Override
        protected Object doInBackground(Integer... integers) {



            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://104.248.61.12/shops?id=" + storeId)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();

                assert response.body() != null;
                JSONObject jShop = new JSONObject(response.body().string());
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

                return shop;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Shop shop = (Shop) o;

            name.setText(shop.getName());

            setImage(shop.getPicture().getUrls().getBig(), image);
            setImage(shop.getLogo().getUrls().getMedium(), logo);
        }

        public void setImage(String url, ImageView imageView){
            Map<String, Object> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            com.teko.honeybits.honeybits.API.Request request = new com.teko.honeybits.honeybits.API.Request(url, params, headers);
            GetImage getImage = new GetImage();

            ImageReadyListener imageReadyListener = new ImageReadyListener(imageView);
            getImage.registerOnResultReadyListener(imageReadyListener);
            getImage.execute(request);
        }
    }
}
