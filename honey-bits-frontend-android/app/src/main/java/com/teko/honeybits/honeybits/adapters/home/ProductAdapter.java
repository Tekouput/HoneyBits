package com.teko.honeybits.honeybits.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.LoginActivity;
import com.teko.honeybits.honeybits.ProductActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public  ProductAdapter productAdapter;

    public ProductAdapter(LayoutDirection layoutDirection, Context context) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.style = null;
        this.productAdapter = this;
    }

    public ProductAdapter(LayoutDirection layoutDirection, Context context, Style style) {
        this.layoutDirection = layoutDirection;
        this.context = context;
        this.style = style;
        this.productAdapter = this;
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
        productViewHolder.isFavorite = products[i].isFavorite();

        if (products[i].isFavorite()){
            productViewHolder.favoriteButton.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_red_24dp));
        }

        productViewHolder.product = products[i];
        productViewHolder.position = i;
        productViewHolder.productAdapter = productAdapter;

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
        boolean isFavorite = false;
        public ImageButton favoriteButton;

        public Product product;
        public int position;
        public ProductAdapter productAdapter;

        public ProductViewHolder(View v) {
            super(v);
            productView = v;

            image = v.findViewById(R.id.productImage);
            name = v.findViewById(R.id.productName);
            storeName = v.findViewById(R.id.shopName);
            price = v.findViewById(R.id.productPrice);
            favoriteButton = v.findViewById(R.id.imageButton);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = new LoginHandler(context).getToken();
                    if (token != null) {
                        SupportFavoriteArguments supportFavoriteArguments = new SupportFavoriteArguments();
                        supportFavoriteArguments.addition = !isFavorite;
                        supportFavoriteArguments.context = context;
                        supportFavoriteArguments.id = id;
                        supportFavoriteArguments.product = product;
                        supportFavoriteArguments.position = position;
                        supportFavoriteArguments.productAdapter = productAdapter;

                        new FavoriteHandler().execute(supportFavoriteArguments);
                    }
                }
            });

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

    public static class SupportFavoriteArguments {
        Context context;
        String id;
        boolean addition;
        Product product;
        int position;
        ProductAdapter productAdapter;

    }

    public  static class FavoriteHandler extends AsyncTask<SupportFavoriteArguments, Void, SupportFavoriteArguments> {

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
                        .url("http://104.248.61.12/products/favorite")
                        .post(body)
                        .addHeader("Authorization", new LoginHandler(supportFavoriteArgument.context).getToken())
                        .build();
            } else {
                request = new okhttp3.Request.Builder()
                        .url("http://104.248.61.12/products/favorite")
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
            supportFavoriteArguments.product.isFavorite = supportFavoriteArguments.addition;
            supportFavoriteArguments.productAdapter.notifyItemChanged(supportFavoriteArguments.position);
        }
    }

}
