package com.teko.honeybits.honeybits.adapters.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Shop;

import java.util.HashMap;
import java.util.Map;

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

    public ShopAdapter(LayoutDirection layoutDirection, Context context, Activity activity) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.activity = activity;
    }

    public ShopAdapter(LayoutDirection layoutDirection, Context context, int resourceLayout, Activity activity) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.activity = activity;
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

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public View shopView;
        public ImageView image;
        public ImageView logo;
        public TextView name;
        public RatingBar ratingBar;
        public Shop store;

        public ShopViewHolder(View v) {
            super(v);
            shopView = v;

            image = v.findViewById(R.id.image_holder);
            logo = v.findViewById(R.id.logo_holder);
            name = v.findViewById(R.id.name);
            ratingBar = v.findViewById(R.id.rating_bar);
        }

        public ShopViewHolder(View v, final Activity activity, final Context context) {
            super(v);
            shopView = v;

            image = v.findViewById(R.id.image_holder);
            logo = v.findViewById(R.id.logo_holder);
            name = v.findViewById(R.id.name);
            ratingBar = v.findViewById(R.id.rating_bar);

            shopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, activity.getClass());
                    intent.putExtra("STORE_ID", store.getId());
                    context.startActivity(intent);
                }
            });
        }


    }

}