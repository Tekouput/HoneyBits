package com.teko.honeybits.honeybits.adapters.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public ShopAdapter(LayoutDirection layoutDirection, Context context) {
        this.layoutDirection = layoutDirection;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View shopView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_shop, viewGroup, false);
        return new ShopViewHolder(shopView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int i) {

        if(layoutDirection == LayoutDirection.HORIZONTAL) {
            int width = (int) context.getResources().getDimension(R.dimen.width_horizontal_dimensions_shop);
            int height = (int) context.getResources().getDimension(R.dimen.height_horizontal_dimensions_shop);
            int margin = (int) context.getResources().getDimension(R.dimen.card_margin_product);

            RecyclerView.LayoutParams distribution = new RecyclerView.LayoutParams(width, height);
            distribution.setMargins(margin, margin, margin, margin);

            shopViewHolder.shopView.setLayoutParams(distribution);
        }

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

        public ShopViewHolder(View v) {
            super(v);
            shopView = v;

            image = v.findViewById(R.id.image_holder);
            logo = v.findViewById(R.id.logo_holder);
            name = v.findViewById(R.id.name);
            ratingBar = v.findViewById(R.id.rating_bar);
        }

    }

}
