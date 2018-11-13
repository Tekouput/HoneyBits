package com.teko.honeybits.honeybits;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.adapters.You.Admin.Shop.ShopModulesAdapter;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Location;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopAdminActivity extends AppCompatActivity {

    private Context context;
    private TextView title;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_admin);

        context = this;
        shopId = getIntent().getStringExtra("STORE_ID");

        setTitle("Stores");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);


        title = findViewById(R.id.tvTitle);
        title.setText("...");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetShop().execute();

        ViewPager viewPager = findViewById(R.id.fragment_holder_vp);
        ShopModulesAdapter adapter = new ShopModulesAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout_elements);
        tabLayout.setupWithViewPager(viewPager);
    }


    class GetShop extends AsyncTask<Void, Void, Shop> {

        @Override
        protected Shop doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://104.248.61.12/shops/single?id=" + shopId)
                    .get()
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                assert response.body() != null;
                JSONObject jShop = new JSONObject(response.body().string());
                System.out.println(jShop.toString());
                JSONObject jShopPicture = jShop.getJSONObject("shop_picture");
                JSONObject jShopLogo = jShop.getJSONObject("shop_logo");
                JSONObject jLocation = jShop.getJSONObject("map_location");
                return new Shop(
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
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Shop o) {
            if (o != null) {
                title.setText(o.getName());

                // Setting image
                Map<String, Object> params = new HashMap<>();
                Map<String, String> headers = new HashMap<>();
                com.teko.honeybits.honeybits.API.Request requestImage = new com.teko.honeybits.honeybits.API.Request(o.getPicture().getUrls().getBig(), params, headers);
                GetImage getImage = new GetImage();

                ImageReadyListener imageReadyListener = new ImageReadyListener((ImageView) findViewById(R.id.shop_image));
                getImage.registerOnResultReadyListener(imageReadyListener);
                getImage.execute(requestImage);


            }
            super.onPostExecute(o);

        }
    }

}
