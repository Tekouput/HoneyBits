package com.teko.honeybits.honeybits;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.Getters.GetShops;
import com.teko.honeybits.honeybits.adapters.home.ShopAdapter;
import com.teko.honeybits.honeybits.listeners.ShopsReadyListener;
import com.teko.honeybits.honeybits.models.Location;
import com.teko.honeybits.honeybits.models.Picture;
import com.teko.honeybits.honeybits.models.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreatorActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Shop> shops = new ArrayList<>();
    ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        context = this;

        setTitle("Stores");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);

        ((TextView) findViewById(R.id.tvTitle)).setText("Stores");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager linearLayoutShops = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);

        RecyclerView editorsStores = findViewById(R.id.stores_holder);
        editorsStores.setHasFixedSize(true);
        editorsStores.setLayoutManager(linearLayoutShops);

        shopAdapter = new ShopAdapter(ShopAdapter.LayoutDirection.HORIZONTAL, context, R.layout.view_shop_large, new ShopAdminActivity());
        editorsStores.setAdapter(shopAdapter);

        new GetOwnersStores().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_button) {
            Intent intent = new Intent(this, ShopCreatorActivity.class);
            startActivity(intent);
        } else if (id == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("fragment_id", R.id.navigation_users);

            // Now start your activity
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetOwnersStores extends AsyncTask<Object, Void, ArrayList<Shop>> {

        @Override
        protected ArrayList<Shop> doInBackground(Object... objects) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://104.248.61.12/users/shops/")
                    .get()
                    .addHeader("Authorization", new LoginHandler(context).getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONArray jsonArray = new JSONArray(response.body().string());
                ArrayList<Shop> shops = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jShop = jsonArray.getJSONObject(i);
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
                    System.out.println(shop.getId());
                    shops.add(shop);
                }
                return shops;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Shop> o) {
            super.onPostExecute(o);
            if (o.size() > 0) {
                View emptyHolder = findViewById(R.id.empty_holder);
                ViewGroup parentEmptyHolder = (ViewGroup) emptyHolder.getParent();
                parentEmptyHolder.removeView(emptyHolder);

                shopAdapter.setShops(o.toArray(new Shop[o.size()]));
                shopAdapter.notifyDataSetChanged();
            }
        }
    }
}
