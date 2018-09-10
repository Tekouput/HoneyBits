package com.teko.honeybits.honeybits.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.API.Getters.GetProducts;
import com.teko.honeybits.honeybits.API.Getters.GetShops;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.adapters.home.ShopAdapter;
import com.teko.honeybits.honeybits.listeners.ProductsReadyListener;
import com.teko.honeybits.honeybits.listeners.ShopsReadyListener;

import java.util.HashMap;
import java.util.Map;

public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;

        View view = inflater.inflate(R.layout.fragment_home_favorites, container, false);
        Context context = container.getContext();
        RecyclerView.LayoutManager linearLayoutProducts = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager linearLayoutShops = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);

        RecyclerView editorsProducts = view.findViewById(R.id.editors_products);
        editorsProducts.setHasFixedSize(true);
        editorsProducts.setLayoutManager(linearLayoutProducts);

        RecyclerView editorsStores = view.findViewById(R.id.editors_stores);
        editorsStores.setHasFixedSize(true);
        editorsStores.setLayoutManager(linearLayoutShops);

        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        Request requestEditorsProducts = new Request("products/latest", "GET", params, headers);
        ProductAdapter editorsProductAdapter = new ProductAdapter(ProductAdapter.LayoutDirection.HORIZONTAL, context);
        ProductsReadyListener editorsProductListener = new ProductsReadyListener(editorsProductAdapter);
        GetProducts getProductsEditors = new GetProducts();
        getProductsEditors.registerOnResultReadyListener(editorsProductListener);
        getProductsEditors.execute(requestEditorsProducts);
        editorsProducts.setAdapter(editorsProductAdapter);

        Request requestEditorsShops = new Request("shops/favorites", "GET", params, headers);
        ShopAdapter editorsShopAdapter = new ShopAdapter(ShopAdapter.LayoutDirection.HORIZONTAL, context);
        ShopsReadyListener editorsShopListener = new ShopsReadyListener(editorsShopAdapter);
        GetShops getShopEditors = new GetShops();
        getShopEditors.registerOnResultReadyListener(editorsShopListener);
        getShopEditors.execute(requestEditorsShops);
        editorsStores.setAdapter(editorsShopAdapter);

        return view;
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FeedFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
