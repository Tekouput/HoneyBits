package com.teko.honeybits.honeybits.listeners;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.models.Product;

public class ProductsReadyListener implements OnResultReadyListener<Product[]> {

    private ProductAdapter productAdapter;

    public ProductsReadyListener(ProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    @Override
    public void onResultReady(Product[] object) {
        productAdapter.setProducts(object);
        productAdapter.notifyDataSetChanged();
    }
}