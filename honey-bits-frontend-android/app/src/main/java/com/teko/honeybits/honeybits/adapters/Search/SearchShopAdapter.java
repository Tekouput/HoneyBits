package com.teko.honeybits.honeybits.adapters.Search;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.models.Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.ShopViewHolder> {

    private ArrayList<Shop> shops = new ArrayList<>();
    private Context context;

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public void setShops(final ArrayList<Shop> shops) {

        // Distance
        for (int i = 0; i < shops.size(); i++){
            Shop shop = shops.get(i);
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng myLoc = new LatLng(latitude, longitude);
            LatLng storeLoc = new LatLng(shop.getLocation().getLatitude(), shop.getLocation().getLongitude());
            shop.distance = (int) CalculationByDistance(myLoc, storeLoc);
        }

        Collections.sort(shops, new Comparator<Shop>() {
            @Override
            public int compare(Shop o1, Shop o2) {
                return Double.compare(o2.distance, o1.distance) * -1;
            }
        });

        this.shops = shops;
    }

    public void clearShops() {
        this.shops = new ArrayList<>();
    }

    public SearchShopAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_search_shop, viewGroup, false);
        return new SearchShopAdapter.ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int i) {
        Shop shop = shops.get(i);
        shopViewHolder.name.setText(shop.getName());

        String distance = shop.distance + " KM";
        shopViewHolder.distanceFromMe.setText(distance);

        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Request request = new Request(shop.getPicture().getUrls().getBig(), params, headers);
        GetImage getImage = new GetImage();

        ImageReadyListener imageReadyListener = new ImageReadyListener(shopViewHolder.image);
        getImage.registerOnResultReadyListener(imageReadyListener);
        getImage.execute(request);

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView name, distanceFromMe;
        ImageView image;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.store_name);
            distanceFromMe = itemView.findViewById(R.id.distance_from_me);
            image = itemView.findViewById(R.id.store_image);

        }
    }

    class LatLng {

        public double latitude;
        public double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
    }

}
