package com.teko.honeybits.honeybits.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.LoginActivity;
import com.teko.honeybits.honeybits.ProductActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public enum LayoutDirection {
        HORIZONTAL,
        VERTICAL
    }

    public enum Style {
        ADMIN,
        USER
    }

    private LayoutDirection layoutDirection;
    private Style style;
    private Product[] products = new Product[0];
    private Context context;

    public ProductAdapter(LayoutDirection layoutDirection, Context context) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.style = null;
    }

    public ProductAdapter(LayoutDirection layoutDirection, Context context, Style style) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.style = style;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View productView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_product, viewGroup, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {

        if(style == Style.ADMIN) {
            productViewHolder.favoriteButton.setImageDrawable(context.getDrawable(R.drawable.ic_close_red_24dp));
        }

        if(layoutDirection == LayoutDirection.HORIZONTAL) {
            int width = (int) context.getResources().getDimension(R.dimen.width_horizontal_dimensions_product);
            int height = (int) context.getResources().getDimension(R.dimen.height_horizontal_dimensions_product);
            int margin = (int) context.getResources().getDimension(R.dimen.card_margin_product);

            RecyclerView.LayoutParams distribution = new RecyclerView.LayoutParams(width, height);
            distribution.setMargins(margin, margin, margin, margin);

            productViewHolder.productView.setLayoutParams(distribution);
        }

        productViewHolder.name.setText(products[i].getName());
        productViewHolder.context = context;
        productViewHolder.id = products[i].getId();
        productViewHolder.storeName.setText(products[i].getShop().getName());

        if (products[i].getPicture().size() > 0) {
            Map<String, Object> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Request request = new Request(products[i].getPicture().get(0).getUrls().getBig(), params, headers);
            GetImage getImage = new GetImage();

            ImageReadyListener imageReadyListener = new ImageReadyListener(productViewHolder.image);
            getImage.registerOnResultReadyListener(imageReadyListener);
            getImage.execute(request);
        }

        productViewHolder.price.setText(products[i].getPrice().getFormatted());

    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public View productView;
        public ImageView image;
        public TextView name;
        public TextView storeName;
        public TextView price;
        public Context context;
        public String id;
        public ImageButton favoriteButton;

        public ProductViewHolder(View v) {
            super(v);
            productView = v;

            image = v.findViewById(R.id.productImage);
            name = v.findViewById(R.id.productName);
            storeName = v.findViewById(R.id.shopName);
            price = v.findViewById(R.id.productPrice);
            favoriteButton = v.findViewById(R.id.imageButton);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(name.getText().toString());
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("PRODUCT_ID", id);
                    context.startActivity(intent);
                }
            });
        }

    }

}
