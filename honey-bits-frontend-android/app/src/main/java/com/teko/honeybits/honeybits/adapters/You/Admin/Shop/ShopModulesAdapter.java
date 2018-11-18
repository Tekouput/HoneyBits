package com.teko.honeybits.honeybits.adapters.You.Admin.Shop;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.fragments.you.admin.shop.PoliciesSettingsAdmin;
import com.teko.honeybits.honeybits.fragments.you.admin.shop.ProductsAdmin;
import com.teko.honeybits.honeybits.fragments.you.admin.shop.UpdateAdmin;

public class ShopModulesAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String shopId;

    public ShopModulesAdapter(Context context, FragmentManager fm, String shopIp) {
        super(fm);
        mContext = context;
        this.shopId = shopIp;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ProductsAdmin productsAdmin = new ProductsAdmin();
            productsAdmin.setShopId(shopId);
            return productsAdmin;
        } else if (position == 1){
            return new UpdateAdmin();
        } else {
            return new PoliciesSettingsAdmin();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.products);
            case 1:
                return mContext.getString(R.string.update_information);
            case 2:
                return mContext.getString(R.string.policies_settins);
        }

        return "NaN";
    }

}