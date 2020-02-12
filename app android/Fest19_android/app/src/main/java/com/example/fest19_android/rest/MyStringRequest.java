package com.example.fest19_android.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class MyStringRequest extends StringRequest {

    public MyStringRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public MyStringRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @NonNull
    @Override
    public String toString() {
        try {
            return super.toString() + ", Method: " + getMethod() + ", Header: " + getHeaders();
        } catch (AuthFailureError authFailureError) {
            return authFailureError.toString();
        }
    }
}
