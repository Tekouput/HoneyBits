package com.teko.honeybits.honeybits.adapters.Cart;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.ProductActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.Search.SearchProductAdapter;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products = new ArrayList<>();
    private Context context;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void clearProducts() {
        products = new ArrayList<>();
    }

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_product_cart, viewGroup, false);
        return new ProductAdapter.ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder p, int i) {
        p.title.setText(products.get(i).getName());
        p.store.setText(products.get(i).getShop().getName());
        p.price.setText(products.get(i).getPrice().getFormatted());
        p.id = products.get(i).getId();

        if (products.get(i).getPicture().size() > 0) {
            Map<String, Object> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Request request = new Request(products.get(i).getPicture().get(0).getUrls().getBig(), params, headers);
            GetImage getImage = new GetImage();

            ImageReadyListener imageReadyListener = new ImageReadyListener(p.image);
            getImage.registerOnResultReadyListener(imageReadyListener);
            getImage.execute(request);
        } else {
            p.image.setImageDrawable(context.getResources().getDrawable(R.drawable.image_holder));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title, store, price;
        ImageView image;
        String id;

        ProductViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.product_name);
            store = view.findViewById(R.id.product_store);
            price = view.findViewById(R.id.product_price);
            image = view.findViewById(R.id.product_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("PRODUCT_ID", id);
                    context.startActivity(intent);
                }
            });

        }
    }

}
