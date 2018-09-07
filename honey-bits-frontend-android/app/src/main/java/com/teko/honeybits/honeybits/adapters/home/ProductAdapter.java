package com.teko.honeybits.honeybits.adapters.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Product[] products = new Product[0];

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View productView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_product, viewGroup, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        productViewHolder.name.setText(products[i].getName());
        productViewHolder.storeName.setText(products[i].getShop().getName());

        if (products[i].getPicture().size() > 0) {
            Map<String, String> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Request request = new Request(products[i].getPicture().get(0).getUrls().getBig(), "GET", params, headers);
            GetImage getImage = new GetImage();

            ImageReadyListener imageReadyListener = new ImageReadyListener(productViewHolder.image);
            getImage.registerOnResultReadyListener(imageReadyListener);
            getImage.execute(request);
        }

        productViewHolder.price.setText(products[i].getPrice().getFormatted());

    }

    public Product[] getProducts() {
        return products;
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

        public ProductViewHolder(View v) {
            super(v);
            productView = v;

            image = v.findViewById(R.id.productImage);
            name = v.findViewById(R.id.productName);
            storeName = v.findViewById(R.id.shopName);
            price = v.findViewById(R.id.productPrice);
        }

    }

}
