package com.teko.honeybits.honeybits.adapters.You.Admin.Shop.Products;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teko.honeybits.honeybits.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageCardView> {

    Activity activity;
    int IMAGE_PICKER_REQUEST;
    ArrayList<Uri> mArrayUri;

    public ImagesAdapter(int IMAGE_PICKER_REQUEST, Activity activity) {
        this.activity = activity;
        this.IMAGE_PICKER_REQUEST = IMAGE_PICKER_REQUEST;
    }

    @NonNull
    @Override
    public ImageCardView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View imageView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_image_card, viewGroup, false);
        return new ImageCardView(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageCardView imageCardView, int i) {
        if (i == mArrayUri.size()) {
            imageCardView.last = true;
        } else {
            Uri mImageUri = mArrayUri.get(i);

            Bitmap bmp = null;
            try {
                InputStream image = activity.getContentResolver().openInputStream(mImageUri);
                bmp = BitmapFactory.decodeStream(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            imageCardView.imageView.setImageBitmap(bmp);
            imageCardView.source = getImageUri(activity.getBaseContext(), bmp);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size() + 1;
    }

    public void addItemList(ArrayList<Uri> mArrayUri) {
        this.mArrayUri = mArrayUri;
    }

    public class ImageCardView extends RecyclerView.ViewHolder {

        public View view;
        public ImageView imageView;
        public Uri source;
        public boolean last;

        public ImageCardView(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageView = itemView.findViewById(R.id.image_holder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (last) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_REQUEST);
                    }
                }
            });

        }
    }
}
