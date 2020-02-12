package com.example.fest19_android.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MyJsonObjectRequest extends JsonObjectRequest {
    public MyJsonObjectRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public MyJsonObjectRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @NonNull
    @Override
    public String toString() {
        byte[] body = getBody();
        try {
            return super.toString() + ", Method: " + getMethod() +", body: " + (body==null ? "null" : new String(body, PROTOCOL_CHARSET)) + ", Header: " + getHeaders();
        } catch (AuthFailureError | UnsupportedEncodingException e) {
            return e.toString();
        }
    }
}
