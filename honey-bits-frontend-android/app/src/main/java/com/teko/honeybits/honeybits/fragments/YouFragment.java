package com.teko.honeybits.honeybits.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;
import com.teko.honeybits.honeybits.API.Getters.GetImage;
import com.teko.honeybits.honeybits.API.Getters.GetProducts;
import com.teko.honeybits.honeybits.API.Getters.GetUser;
import com.teko.honeybits.honeybits.API.OnResultReadyListener;
import com.teko.honeybits.honeybits.API.Request;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.home.ProductAdapter;
import com.teko.honeybits.honeybits.listeners.ImageReadyListener;
import com.teko.honeybits.honeybits.listeners.ProductsReadyListener;
import com.teko.honeybits.honeybits.models.User;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class YouFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert container != null;
        final Context context = container.getContext();

        final View view = inflater.inflate(R.layout.fragment_you, container, false);
        LoginHandler loginHandler = new LoginHandler(context);

        String token = loginHandler.getToken();

        if(token != null){
            setUpRecyclers(context, view, token);
            GetUser getUser = new GetUser();
            getUser.registerOnResultReadyListener(new OnResultReadyListener<User>() {
                @Override
                public void onResultReady(User user) {
                    ImageView avatar = view.findViewById(R.id.user_avatar);
                    Map<String, Object> params = new HashMap<>();
                    Map<String, String> headers = new HashMap<>();
                    Request request = new Request(user.getProfile_pic().getUrl(), params, headers);
                    GetImage getImage = new GetImage();

                    ImageReadyListener imageReadyListener = new ImageReadyListener(avatar);
                    getImage.registerOnResultReadyListener(imageReadyListener);
                    getImage.execute(request);

                    TextView name = view.findViewById(R.id.name);
                    String nameText = user.getFirst_name() + " " + user.getLast_name();
                    name.setText(nameText);

                    TextView followers = view.findViewById(R.id.followers);
                    TextView following = view.findViewById(R.id.following);
                    TextView favorites = view.findViewById(R.id.favorites);

                    followers.setText(user.getFollowers() + "");
                    following.setText(user.getFollowing() + "");
                    favorites.setText(user.getFavorites() + "");
                }
            });
            getUser.execute();
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
