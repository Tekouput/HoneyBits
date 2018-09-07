package com.teko.honeybits.honeybits.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;

public class GetImage extends AsyncTask<Request, Void, Bitmap> {

    private OnResultReadyListener<Bitmap> listener;

    public void registerOnResultReadyListener(OnResultReadyListener<Bitmap> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Request... requests) {

        Bitmap bmp  = null;
        try {
            URL url = new URL(requests[0].url);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (listener != null){
            listener.onResultReady(bitmap);
        }
    }
}
