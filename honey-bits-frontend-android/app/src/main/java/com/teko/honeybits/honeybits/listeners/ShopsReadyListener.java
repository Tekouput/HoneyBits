package com.teko.honeybits.honeybits.listeners;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.adapters.home.ShopAdapter;
import com.teko.honeybits.honeybits.models.Shop;

public class ShopsReadyListener implements OnResultReadyListener<Shop[]> {

    private ShopAdapter shopAdapter;

    public ShopsReadyListener(ShopAdapter shopAdapter) {
        this.shopAdapter = shopAdapter;
    }

    @Override
    public void onResultReady(Shop[] object) {
        shopAdapter.setShops(object);
        shopAdapter.notifyDataSetChanged();
    }
}
