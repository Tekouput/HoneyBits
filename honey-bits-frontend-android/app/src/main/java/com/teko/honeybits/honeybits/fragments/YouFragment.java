package com.teko.honeybits.honeybits.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.Getters.GetProducts;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.listeners.ProductsReadyListener;

import java.util.HashMap;
import java.util.Map;

public class YouFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert container != null;
        final Context context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_you, container, false);
        LoginHandler loginHandler = new LoginHandler(context);

        String token = loginHandler.getToken();

        if(token != null){
            setUpRecyclers(context, view, token);
            
        }
        
        return view;
    }

    private void setUpRecyclers(Context context, View view, String token) {
        RecyclerView popularProductsList = view.findViewById(R.id.popular_products);
        popularProductsList.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(context, 2);

        popularProductsList.setLayoutManager(gridLayoutManager);

        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        Request requestPopular = new Request("products/popular", params, headers);
        ProductAdapter popularProductAdapter = new ProductAdapter(ProductAdapter.LayoutDirection.VERTICAL, context);
        ProductsReadyListener popularListener = new ProductsReadyListener(popularProductAdapter);
        GetProducts getProductsPopular = new GetProducts();
        getProductsPopular.registerOnResultReadyListener(popularListener);
        getProductsPopular.execute(requestPopular);
        popularProductsList.setAdapter(popularProductAdapter);
    }
}
