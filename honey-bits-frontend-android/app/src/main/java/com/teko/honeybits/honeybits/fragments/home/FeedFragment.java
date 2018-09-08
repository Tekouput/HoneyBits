package com.teko.honeybits.honeybits.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.API.GetProducts;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.listeners.ProductsReadyListener;

import java.util.HashMap;
import java.util.Map;

public class FeedFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        Context context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);
        setUpRecyclers(container, context, view);

        return view;
    }

    private void setUpRecyclers(@NonNull ViewGroup container, Context context, View view) {
        RecyclerView popularProductsList = view.findViewById(R.id.popular_products);
        popularProductsList.setHasFixedSize(false);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(context, 2);

        popularProductsList.setLayoutManager(gridLayoutManager);

        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        Request requestPopular = new Request("products/popular", "GET", params, headers);
        ProductAdapter popularProductAdapter = new ProductAdapter(ProductAdapter.LayoutDirection.VERTICAL, context);
        ProductsReadyListener popularListener = new ProductsReadyListener(popularProductAdapter);
        GetProducts getProductsPopular = new GetProducts();
        getProductsPopular.registerOnResultReadyListener(popularListener);
        getProductsPopular.execute(requestPopular);
        popularProductsList.setAdapter(popularProductAdapter);

        RecyclerView latestProductsList = view.findViewById(R.id.latest_products);
        latestProductsList.setHasFixedSize(false);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        latestProductsList.setLayoutManager(linearLayoutManager);

        Request requestLatest = new Request("products/latest", "GET", params, headers);
        ProductAdapter latestProductAdapter = new ProductAdapter(ProductAdapter.LayoutDirection.HORIZONTAL, context);
        ProductsReadyListener latestListener = new ProductsReadyListener(latestProductAdapter);
        GetProducts getProductsLatest = new GetProducts();
        getProductsLatest.registerOnResultReadyListener(latestListener);
        getProductsLatest.execute(requestLatest);
        latestProductsList.setAdapter(latestProductAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
