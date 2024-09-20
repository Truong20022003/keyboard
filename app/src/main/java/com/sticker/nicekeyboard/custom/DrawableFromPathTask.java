package com.sticker.nicekeyboard.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DrawableFromPathTask extends AsyncTask<String, Void, Drawable> {

    private OnDrawableLoadListener listener;

    public interface OnDrawableLoadListener {
        void onDrawableLoaded(Drawable drawable);
        void onDrawableLoadError(String errorMessage);
    }

    public DrawableFromPathTask(OnDrawableLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Drawable doInBackground(String... paths) {
        String path = paths[0];
        Drawable drawable = null;

        try {
            if (path.startsWith("http")) {
                // Load drawable from internet URL
                URL imageUrl = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                drawable = new BitmapDrawable(bitmap);
            } else {
                // Load drawable from local file path
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                drawable = new BitmapDrawable(bitmap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (drawable != null) {
            listener.onDrawableLoaded(drawable);
        } else {
            listener.onDrawableLoadError("Failed to load drawable");
        }
    }
}