package com.teko.honeybits.honeybits.adapters.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.ProductActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.ShopActivity;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;
import com.teko.honeybits.honeybits.models.Shop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    public enum LayoutDirection {
        HORIZONTAL,
        VERTICAL
    }

    private ShopAdapter.LayoutDirection layoutDirection;
    private Shop[] shops = new Shop[0];
    private Context context;
    private int resourceLayout = -1;
    private Activity activity;
    public ShopAdapter shopAdapter;
    public Context containerContext;

    public ShopAdapter(LayoutDirection layoutDirection, Context context, Activity activity, Context container) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.activity = activity;
        this.shopAdapter = this;
        this.containerContext = container;
    }

    public ShopAdapter(LayoutDirection layoutDirection, Context context, int resourceLayout, Activity activity, Context container) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.activity = activity;
        this.shopAdapter = this;
        this.containerContext = container;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View shopView;
        if (resourceLayout != -1){
            shopView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(resourceLayout, viewGroup, false);
        } else {
            shopView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_shop, viewGroup, false);
        }

        if (activity != null){
            return new ShopViewHolder(shopView, activity, context);
        }
        return new ShopViewHolder(shopView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int i) {

        if(layoutDirection == LayoutDirection.HORIZONTAL && resourceLayout == -1) {
            int width = (int) context.getResources().getDimension(R.dimen.width_horizontal_dimensions_shop);
            int height = (int) context.getResources().getDimension(R.dimen.height_horizontal_dimensions_shop);
            int margin = (int) context.getResources().getDimension(R.dimen.card_margin_product);

            RecyclerView.LayoutParams distribution = new RecyclerView.LayoutParams(width, height);
            distribution.setMargins(margin, margin, margin, margin);

            shopViewHolder.shopView.setLayoutParams(distribution);
        }

        shopViewHolder.store = shops[i];
        shopViewHolder.name.setText(shops[i].getName());
        if (shops[i].getRating() != null)
            shopViewHolder.ratingBar.setRating(Float.parseFloat(shops[i].getRating()));

        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Request requestImage = new Request(shops[i].getPicture().getUrls().getBig(), params, headers);
        Request requestLogo = new Request(shops[i].getLogo().getUrls().getBig(), params, headers);
        GetImage getImage = new GetImage();
        GetImage getLogo = new GetImage();

        if (shopViewHolder.store.isFavorite()) {
            shopViewHolder.favoriteButton.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_red_24dp));
        }

        shopViewHolder.context = context;
        shopViewHolder.id = shops[i].getId();
        shopViewHolder.position = i;
        shopViewHolder.shop = shops[i];
        shopViewHolder.shopAdapter = shopAdapter;
        shopViewHolder.containerContext = containerContext;

        ImageReadyListener imageReadyListener = new ImageReadyListener(shopViewHolder.image);
        ImageReadyListener logoReadyListener = new ImageReadyListener(shopViewHolder.logo);
        getImage.registerOnResultReadyListener(imageReadyListener);
        getLogo.registerOnResultReadyListener(logoReadyListener);
        getImage.execute(requestImage);
        getLogo.execute(requestLogo);
    }

    public void setShops(Shop[] shops) {
        this.shops = shops;
    }

    @Override
    public int getItemCount() {
        return shops.length;
    }

    public static class SupportFavoriteArguments {
        Context context;
        String id;
        boolean addition;
        Shop shop;
        int position;
        ShopAdapter shopAdapter;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public View shopView;
        public ImageView image;
        public ImageView logo;
        public TextView name;
        public RatingBar ratingBar;
        public Shop store;
        public ImageButton favoriteButton;

        public Context context;
        public Context containerContext;
        public String id;
        public int position;
        public Shop shop;
        public ShopAdapter shopAdapter;

        public ShopViewHolder(View v) {
            super(v);
            shopView = v;

            image = v.findViewById(R.id.image_holder);
            logo = v.findViewById(R.id.logo_holder);
            name = v.findViewById(R.id.name);
            ratingBar = v.findViewById(R.id.rating_bar);
            favoriteButton = v.findViewById(R.id.favorite_button);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = new LoginHandler(containerContext).getToken();
                    if (token != null) {
                        SupportFavoriteArguments supportFavoriteArguments = new SupportFavoriteArguments();
                        supportFavoriteArguments.addition = !shop.isFavorite();
                        supportFavoriteArguments.context = context;
                        supportFavoriteArguments.id = id;
                        supportFavoriteArguments.position = position;
                        supportFavoriteArguments.shop = shop;
                        supportFavoriteArguments.shopAdapter = shopAdapter;

                        new FavoriteHandler().execute(supportFavoriteArguments);
                    }
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopActivity.class);
                    intent.putExtra("STORE_ID", id);
                    context.startActivity(intent);
                }
            });
        }

        public ShopViewHolder(View v, final Activity activity, final Context context) {
            super(v);
            shopView = v;

            image = v.findViewById(R.id.image_holder);
            logo = v.findViewById(R.id.logo_holder);
            name = v.findViewById(R.id.name);
            ratingBar = v.findViewById(R.id.rating_bar);
            favoriteButton = v.findViewById(R.id.favorite_button);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String token = new LoginHandler(context).getToken();
                    if (token != null) {
                        SupportFavoriteArguments supportFavoriteArguments = new SupportFavoriteArguments();
                        supportFavoriteArguments.addition = !shop.isFavorite();
                        supportFavoriteArguments.context = context;
                        supportFavoriteArguments.id = id;
                        supportFavoriteArguments.position = position;
                        supportFavoriteArguments.shop = shop;
                        supportFavoriteArguments.shopAdapter = shopAdapter;

                        new FavoriteHandler().execute(supportFavoriteArguments);
                    }
                }
            });

            shopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, activity.getClass());
                    intent.putExtra("STORE_ID", store.getId());
                    context.startActivity(intent);
                }
            });
        }

        static class FavoriteHandler extends AsyncTask<SupportFavoriteArguments, Void, SupportFavoriteArguments> {

            @Override
            protected SupportFavoriteArguments doInBackground(SupportFavoriteArguments... supportFavoriteArguments) {

                SupportFavoriteArguments supportFavoriteArgument = supportFavoriteArguments[0];

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", supportFavoriteArgument.id)
                        .build();

                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request;

                if (supportFavoriteArgument.addition){
                    request = new okhttp3.Request.Builder()
                            .url("http://104.248.61.12/favorite")
                            .post(body)
                            .addHeader("Authorization", new LoginHandler(supportFavoriteArgument.context).getToken())
                            .build();
                } else {
                    request = new okhttp3.Request.Builder()
                            .url("http://104.248.61.12/favorite")
                            .delete(body)
                            .addHeader("Authorization", new LoginHandler(supportFavoriteArgument.context).getToken())
                            .build();
                }

                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body().string());
                    if (response.code() == 200) {
                        return supportFavoriteArgument;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(SupportFavoriteArguments supportFavoriteArguments) {
                super.onPostExecute(supportFavoriteArguments);
                supportFavoriteArguments.shop.isFavorite = supportFavoriteArguments.addition;
                supportFavoriteArguments.shopAdapter.notifyItemChanged(supportFavoriteArguments.position);
            }
        }

    }

}