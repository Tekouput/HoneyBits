package com.teko.honeybits.honeybits.fragments.you.admin.shop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.ProductCreatorActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.ShopCreatorActivity;

public class ProductsAdmin extends Fragment {
    public ProductsAdmin() {
        // Required empty public constructor
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
                startActivity(intent);
            }
        });

        return rootView;
    }
}
