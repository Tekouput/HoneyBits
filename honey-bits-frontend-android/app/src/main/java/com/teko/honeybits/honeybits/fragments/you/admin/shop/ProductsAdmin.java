package com.teko.honeybits.honeybits.fragments.you.admin.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.API.Getters.GetProducts;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.ProductCreatorActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.listeners.ProductsReadyListener;

import java.util.HashMap;
import java.util.Map;

public class ProductsAdmin extends Fragment {

    private String shopId;

    public ProductsAdmin() {
        // Required empty public constructor
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_admin, container, false);
        final Context context = getContext();

        FloatingActionButton button = rootView.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductCreatorActivity.class);
                intent.putExtra("SHORE_ID", shopId);
                startActivity(intent);
            }
        });

        RecyclerView productHolder = rootView.findViewById(R.id.product_holder);
        productHolder.setLayoutManager(new LinearLayoutManager(context));


        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        Request requestPopular = new Request("shops/products?id=" + shopId, params, headers);
        ProductAdapter popularProductAdapter = new ProductAdapter(ProductAdapter.LayoutDirection.VERTICAL, context, ProductAdapter.Style.ADMIN);
        ProductsReadyListener popularListener = new ProductsReadyListener(popularProductAdapter);
        GetProducts getProductsPopular = new GetProducts();
        getProductsPopular.registerOnResultReadyListener(popularListener);
        getProductsPopular.execute(requestPopular);
        productHolder.setAdapter(popularProductAdapter);

        return rootView;
    }
}
