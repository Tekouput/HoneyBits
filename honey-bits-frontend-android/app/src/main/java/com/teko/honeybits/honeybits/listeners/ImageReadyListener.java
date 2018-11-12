package com.teko.honeybits.honeybits.listeners;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.teko.honeybits.honeybits.API.OnResultReadyListener;

public class ImageReadyListener implements OnResultReadyListener<Bitmap> {

    private ImageView imageView;

    public ImageReadyListener(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onResultReady(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}