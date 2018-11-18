package com.teko.honeybits.honeybits;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.adapters.You.Admin.Shop.Products.ImagesAdapter;
import com.teko.honeybits.honeybits.models.Category;
import com.teko.honeybits.honeybits.support.CategoryCompletionView;
import com.teko.honeybits.honeybits.support.FileChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductCreatorActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER_REQUEST = 702;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    ImagesAdapter imagesAdapter;

    EditText name;
    EditText description;
    EditText price;
    CategoryCompletionView categoriesText;
    int shopId;
    int productId;

    Context context;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_creator);

        setTitle("Product Creator");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);
        TextView title = findViewById(R.id.tvTitle);
        title.setText("Product Creator");

        RecyclerView recyclerView = findViewById(R.id.image_view_selector);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesAdapter = new ImagesAdapter(IMAGE_PICKER_REQUEST, this);
        imagesAdapter.addItemList(mArrayUri);
        recyclerView.setAdapter(imagesAdapter);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        categoriesText = findViewById(R.id.editText);
        shopId = Integer.parseInt(getIntent().getStringExtra("SHORE_ID"));
        context = this;
        parentLayout = ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        findViewById(R.id.create_product_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateProduct().execute();
            }
        });
    }

    private class CreateProduct extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... objects) {


            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            JSONObject object = new JSONObject();
            JSONObject product = new JSONObject();
            JSONArray categories = new JSONArray();
            try {
                // PRODUCT
                product.put("name", name.getText().toString());
                product.put("description", description.getText().toString());
                product.put("price", Double.parseDouble(price.getText().toString()));
                product.put("rating", 0);
                product.put("shop_id", shopId);

                //CATEGORIES
                List<Category> categoriesStrings = categoriesText.getObjects();
                for (Category category : categoriesStrings) {
                    categories.put(category.getName());
                }

                //OBJECT
                object.put("product", product);
                object.put("categories", categories);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(mediaType, object.toString());
            Request request = new Request.Builder()
                    .url("http://104.248.61.12/products")
                    .post(body)
                    .addHeader("Authorization", new LoginHandler(context).getToken())
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();

                String jsonData = response.body().string();
                JSONObject jsonResponse = new JSONObject(jsonData);
                productId = jsonResponse.getInt("id");
                return response;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Response response = (Response) o;
            if (response.code() == 201) {

                new ProductImageUploader().execute(mArrayUri.toArray(new Uri[mArrayUri.size()]));
                ProgressDialog dialog = ProgressDialog.show(ProductCreatorActivity.this, "",
                        "Uploading. Please wait...", true);
                dialog.show();

            } else {
                Snackbar.make(parentLayout, "There is something wrong with the stor information", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", null)
                        .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        }
    }

    private class ProductImageUploader extends AsyncTask<Uri, Void, Object> {

        @Override
        protected Object doInBackground(Uri... uris) {

            int code = 200;

            for (Uri uri : uris) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("image/*");
                RequestBody body = null;
                File image = new File(FileChooser.getPath(context, uri));
                try {
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("product_id", String.valueOf(productId))
                            .addFormDataPart("image",
                                    FileChooser.getPath(context, uri),
                                    RequestBody.create(mediaType, image)
                            )
                            .build();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Request request = new Request.Builder()
                        .url("http://104.248.61.12/products/image")
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", new LoginHandler(context).getToken())
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() != 200) {
                        code = response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return code;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            int code = (int) o;
            System.out.println("########################## =>" + code);
            if (code == 200) {
                Intent intent = new Intent(context, ShopAdminActivity.class);
                intent.putExtra("STORE_ID", shopId + "");
                startActivity(intent);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_PICKER_REQUEST:

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    mArrayUri.add(mImageUri);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                        }
                    }
                }

                imagesAdapter.notifyDataSetChanged();
                break;
        }
    }
}
