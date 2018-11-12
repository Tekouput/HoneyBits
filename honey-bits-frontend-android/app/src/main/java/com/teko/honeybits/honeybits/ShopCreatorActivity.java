package com.teko.honeybits.honeybits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.support.FileChooser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShopCreatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyAnn9gPJq41D36O0RIsrvkBs2351htbpKg";

    private ImageView storeImage;
    private Uri storeImageUri;

    private ImageView storeLogo;
    private Uri storeLogoUri;

    TextView title;
    TextView description;
    TextView policy;
    InputStream image;
    InputStream logo;

    Context context;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_creator);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        policy = findViewById(R.id.policy);
        context = this;
        parentLayout = ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        setTitle("Stores creator");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_title);
        ((TextView) findViewById(R.id.tvTitle)).setText("Stores creator");

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        findViewById(R.id.removable_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(ShopCreatorActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.removable_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_REQUEST);
            }
        });

        findViewById(R.id.removable_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOGO_PICKER_REQUEST);
            }
        });

        storeImage = findViewById(R.id.store_image);
        storeLogo = findViewById(R.id.logo_image);

        findViewById(R.id.create_store_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterUser().execute();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(17);
        gmap.getUiSettings().setAllGesturesEnabled(false);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int IMAGE_PICKER_REQUEST = 2;
    private static final int LOGO_PICKER_REQUEST = 3;

    private static Place place;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    place = PlacePicker.getPlace(this, data);
                    gmap.clear();
                    gmap.addMarker(new MarkerOptions().position(place.getLatLng()));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                }
                break;
            case IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (data.getData() != null) {

                        Uri mImageUri = data.getData();
                        storeImageUri = mImageUri;

                        Bitmap bmp = null;
                        try {
                            image = getContentResolver().openInputStream(mImageUri);
                            bmp = BitmapFactory.decodeStream(image);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        storeImage.setImageBitmap(bmp);
                        storeImageUri = getImageUri(context, bmp);
                    }
                }
                break;
            case LOGO_PICKER_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getData() != null) {

                        Uri mImageUri = data.getData();

                        Bitmap bmp = null;
                        try {
                            logo = getContentResolver().openInputStream(mImageUri);
                            bmp = BitmapFactory.decodeStream(logo);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        storeLogo.setImageBitmap(bmp);
                        storeLogoUri = getImageUri(context, bmp);

                    }
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public class RegisterUser extends AsyncTask<Object, Void, Object> {

        public byte[] readBytes(InputStream inputStream) throws IOException {
            // this dynamically extends to take the bytes you read
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            // and then we can return your byte array.
            return byteBuffer.toByteArray();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("image/*");
            RequestBody body = null;
            File targetFileImage = new File(FileChooser.getPath(context, storeImageUri));
            File targetFileLogo = new File(FileChooser.getPath(context, storeLogoUri));
            try {
                body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", title.getText().toString())
                        .addFormDataPart("description", description.getText().toString())
                        .addFormDataPart("latitude", place.getLatLng().latitude + "")
                        .addFormDataPart("longitude", place.getLatLng().longitude + "")
                        .addFormDataPart("place_id", place.getId())
                        .addFormDataPart("policy", policy.getText().toString())
                        .addFormDataPart("shop_picture",
                                FileChooser.getPath(context, storeImageUri),
                                RequestBody.create(mediaType, targetFileImage)
                        )
                        .addFormDataPart("shop_logo",
                                FileChooser.getPath(context, storeLogoUri),
                                RequestBody.create(mediaType, targetFileLogo))
                        .build();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            Request request = new Request.Builder()
                    .url("http://104.248.61.12/shops")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", new LoginHandler(context).getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Response response = (Response) o;
            if (response.code() == 201) {
                Intent intent = new Intent(context, CreatorActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(parentLayout, "User already exist", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", null)
                        .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        }
    }
}
