package com.teko.honeybits.honeybits.fragments;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.teko.honeybits.honeybits.API.Getters.GetProducts;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.Search.SearchProductAdapter;
import com.teko.honeybits.honeybits.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    enum ResourcesType {
        Products,
        Stores
    }

    public ResourcesType currentType = ResourcesType.Products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        final Context context = container.getContext();
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.search_results);

        Button productDel = view.findViewById(R.id.products_del);
        Button storeDel = view.findViewById(R.id.stores_del);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final SearchProductAdapter productAdapter = new SearchProductAdapter(context);

        OnStateChangedListener stateChangedListener = new OnStateChangedListener() {
            @Override
            public void onStateChanges(ResourcesType type) {
                currentType = type;
                switch (type){
                    case Stores:
                        productAdapter.clearProducts();
                        productAdapter.notifyDataSetChanged();
                        break;
                    case Products:
                        productAdapter.clearProducts();
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };


        SwitchableListener productDelListener = new SwitchableListener(storeDel, context, ResourcesType.Products);
        SwitchableListener storeDelListener = new SwitchableListener(productDel, context, ResourcesType.Stores);

        productDelListener.setOnStateChangedListener(stateChangedListener);
        storeDelListener.setOnStateChangedListener(stateChangedListener);

        productDel.setOnClickListener(productDelListener);
        storeDel.setOnClickListener(storeDelListener);

        final SearchView searchField = view.findViewById(R.id.search_field);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchViewBehavior((SearchView) v, view, recyclerView);
            }
        });

        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Map<String, Object> params = new HashMap<>();
                Map<String, String> headers = new HashMap<>();
                Request requestPopular = new Request("/products/products_by_name?name="+query, params, headers);

                GetProducts getProductsPopular = new GetProducts();
                getProductsPopular.registerOnResultReadyListener(new OnResultReadyListener<Product[]>() {
                    @Override
                    public void onResultReady(Product[] object) {
                        productAdapter.clearProducts();
                        productAdapter.setProducts(new ArrayList<>(Arrays.asList(object)));
                        productAdapter.notifyDataSetChanged();
                    }
                });
                getProductsPopular.execute(requestPopular);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        productDel.performClick();

        return view;
    }

    private class SwitchableListener implements View.OnClickListener{

        Button counterPart;
        Context context;
        ResourcesType type;

        public SwitchableListener(Button counterPart, Context context, ResourcesType type){
            super();
            this.counterPart = counterPart;
            this.context = context;
            this.type = type;
        }

        OnStateChangedListener onStateChangedListener;

        public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
            this.onStateChangedListener = onStateChangedListener;
        }

        @Override
        public void onClick(View v) {
            setSelected((Button) v, context);
            unSelect(counterPart, context);
            onStateChangedListener.onStateChanges(type);
        }
    }

    private interface OnStateChangedListener {
        void onStateChanges(ResourcesType rt);
    }

    private void setSelected(Button button, Context context){
        button.setBackgroundResource(android.R.color.transparent);
        button.setTextColor(context.getResources().getColor(android.R.color.white));
    }

    private void unSelect(Button button, Context context){
        button.setBackgroundResource(R.drawable.transition_background_button_selected);
        button.setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    private void setSearchViewBehavior(SearchView v, View view, final RecyclerView recyclerView) {

        final int selector_height = Math.round(getResources().getDimension(R.dimen.selector_height));
        final int padding_expanded = Math.round(getResources().getDimension(R.dimen.selector_padding_expanded));
        final int padding_collapsed = Math.round(getResources().getDimension(R.dimen.selector_padding_normal));

        View searchBack = view.findViewById(R.id.search_back);
        final TransitionDrawable transition = (TransitionDrawable) searchBack.getBackground();
        transition.startTransition(200);
        v.setIconified(false);

        final View selectorContainer = view.findViewById(R.id.selector_container);
        TransitionManager.beginDelayedTransition(((ViewGroup)selectorContainer), new TransitionSet()
                .addTransition(new ChangeBounds()));

        ViewGroup.LayoutParams a = selectorContainer.getLayoutParams();
        a.height = selector_height;

        recyclerView.setPadding(recyclerView.getPaddingStart(),
                padding_expanded,
                recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        selectorContainer.setLayoutParams(a);

        v.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                transition.reverseTransition(200);

                ViewGroup.LayoutParams a = selectorContainer.getLayoutParams();
                a.height = 0;
                selectorContainer.setLayoutParams(a);

                recyclerView.setPadding(recyclerView.getPaddingStart(),
                        padding_collapsed,
                        recyclerView.getPaddingRight(),
                        recyclerView.getPaddingBottom());
                return false;
            }
        });
    }
}
