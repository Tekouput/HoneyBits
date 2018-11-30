package com.teko.honeybits.honeybits.adapters.Cart;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.teko.honeybits.honeybits.MainActivity;
import com.teko.honeybits.honeybits.ProductActivity;
import com.teko.honeybits.honeybits.ProductCreatorActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.Search.SearchProductAdapter;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public ArrayList<Product> products = new ArrayList<>();
    private Context context;

    private Double amount = 0.0;
    private TextView amountText;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ProductAdapter productAdapter;


    public ProductAdapter(Context context, TextView amountText) {
        this.context = context;
        this.amountText = amountText;
        productAdapter = this;
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

        Double amount = Double.parseDouble(products.get(i).getPrice().getRaw()) * products.get(i).getAmount();
        this.amount = this.amount + amount;
        this.amountText.setText(String.format("$%10.2f", this.amount));

        p.price.setText(String.format("$%10.2f", amount));
        p.id = products.get(i).getId();
        p.amountOff.setText(String.valueOf(products.get(i).getAmount()));

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

    public void updateAmount(){
        this.amount = 0.0;
        for (Product product : products){
            Double amount = Double.parseDouble(product.getPrice().getRaw()) * product.getAmount();
            this.amount = this.amount + amount;
        }
        this.amountText.setText(String.format("$%10.2f", this.amount));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title, store, price, amountOff;
        ImageView image;
        ImageButton remove;
        String id;

        ProductViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.product_name);
            store = view.findViewById(R.id.product_store);
            price = view.findViewById(R.id.product_price);
            image = view.findViewById(R.id.product_image);
            amountOff = view.findViewById(R.id.amount_off);
            remove = view.findViewById(R.id.remove_product_button);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SupportObjects supportObjects = new SupportObjects();
                    ProgressDialog dialog = ProgressDialog.show(context, "",
                            "Uploading. Please wait...", true);
                    supportObjects.id = id;
                    supportObjects.context = context;
                    supportObjects.productAdapter = productAdapter;
                    supportObjects.dialog = dialog;
                    new RemoveFromCart().execute(supportObjects);
                    dialog.show();
                }
            });

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

    static class SupportObjects {

        public String id;
        public Context context;
        public ProductAdapter productAdapter;
        public Dialog dialog;

    }

    static class RemoveFromCart extends AsyncTask <SupportObjects, Void, SupportObjects> {

        @Override
        protected SupportObjects doInBackground(SupportObjects... supportObjects) {

            SupportObjects supportObject = supportObjects[0];

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", supportObject.id)
                    .build();

            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://104.248.61.12/carts/product")
                    .delete(body)
                    .addHeader("Authorization", new LoginHandler(supportObject.context).getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 204) {
                    return supportObject;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SupportObjects supportObjects) {
            super.onPostExecute(supportObjects);

            Product product = objectInArray(supportObjects.productAdapter.products, supportObjects.id);
            int position = supportObjects.productAdapter.products.indexOf(product);
            supportObjects.productAdapter.products.remove(product);
            supportObjects.dialog.dismiss();
            supportObjects.productAdapter.notifyItemRemoved(position);
            supportObjects.productAdapter.updateAmount();

        }

        private Product objectInArray(ArrayList<Product> arrayList, String id) {
            for (Product product : arrayList){
                if (product.getId().equals(id)) return product;
            }
            return null;
        }
    }

}
