package com.example.fest19_android.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class MyJsonArrayRequest extends JsonArrayRequest {
    public MyJsonArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public MyJsonArrayRequest(String url, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
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
