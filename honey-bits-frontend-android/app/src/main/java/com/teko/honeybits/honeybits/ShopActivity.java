package com.teko.honeybits.honeybits;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.adapters.Search.SearchShopAdapter;
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
    private TextView distance;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        context = this;

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
        distance = findViewById(R.id.distance);

        storeId = getIntent().getStringExtra("STORE_ID");

        new GetProductInformation().execute();

    }

    class LatLng {

        public double latitude;
        public double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
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

            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            android.location.Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng myLoc = new LatLng(latitude, longitude);

            distance.setText(String.format("%.2f KM", CalculationByDistance(
                    new LatLng(shop.getLocation().getLatitude(), shop.getLocation().getLongitude()),
                    myLoc)));
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
