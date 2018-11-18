package com.teko.honeybits.honeybits;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.R;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductActivity extends AppCompatActivity {

    TextView storeName;
    ImageView storeLogo;
    ImageView productImage;
    TextView productName;
    TextView productPrice;
    TextView productAvailability;
    TextView productDescription;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        setTitle("Product profile");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);
        TextView title = findViewById(R.id.tvTitle);
        title.setText("Product profile");

        productId = getIntent().getStringExtra("PRODUCT_ID");

        storeName = findViewById(R.id.store_name);
        storeLogo = findViewById(R.id.store_logo);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productAvailability = findViewById(R.id.product_availability);
        productDescription = findViewById(R.id.product_description);

        new GetProductInformation().execute();
    }

    private class GetProductInformation extends AsyncTask<Integer, Void, Object> {

        @Override
        protected Object doInBackground(Integer... integers) {



            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://104.248.61.12/products?id=" + productId)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();

                assert response.body() != null;
                JSONObject jObject = new JSONObject(response.body().string());
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

                return product;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Product product = (Product) o;

            storeName.setText(product.getShop().getName());
            productName.setText(product.getName());
            productAvailability.setText("Only 150 Available"); //TODO Add real value
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getPrice().getFormatted());

            setImage(product.getPicture().get(0).getUrls().getBig(), productImage);
            setImage(product.getShop().getLogo().getUrls().getMedium(), storeLogo);
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
