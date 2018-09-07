package com.teko.honeybits.honeybits.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.API.GetProducts;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.models.Product;

import java.util.HashMap;
import java.util.Map;

public class FeedFragment extends Fragment implements OnResultReadyListener {

    private RecyclerView popularProductsList;
    private ProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);
        popularProductsList = view.findViewById(R.id.popular_products);
        popularProductsList.setHasFixedSize(false);
        layoutManager = new GridLayoutManager(container.getContext(), 2);

        popularProductsList.setLayoutManager(layoutManager);

        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Request request = new Request("products/popular", "GET", params, headers);

        OnResultReadyListener listener = this;
        GetProducts getProducts = new GetProducts();
        getProducts.registerOnResultReadyListener(listener);
        getProducts.execute(request);

        productAdapter = new ProductAdapter();
        popularProductsList.setAdapter(productAdapter);
        return view;
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

    @Override
    public void onResultReady(Object object) {
        productAdapter.setProducts((Product[]) object);
        productAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
