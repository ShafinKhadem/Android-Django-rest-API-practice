package com.example.fest19_android.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.fest19_android.ui.login.LoginActivity;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton(Context context) {

        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        // memory leak happens if that activity gets destroyed.
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }


    // initialize from main activity, pass null when calling later.
    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleySingleton.class) {
                instance = new VolleySingleton(context);
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d(LoginActivity.TAG, "Volley request added: "+req.toString());
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
