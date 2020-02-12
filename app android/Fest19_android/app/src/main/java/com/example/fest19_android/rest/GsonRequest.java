package com.example.fest19_android.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.fest19_android.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends JsonRequest<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;

    /**
     * Make a GET / POST request and return a parsed object of type T from JSON.
     *
     * @param method the HTTP method to use
     * @param url URL of the request to make
     * @param jsonRequest A {@link JSONObject} to post with the request. Null indicates no
     *     parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     * @param clazz Relevant class object, for Gson's fromJson
     * @param headers Map of request headers
     */
    public GsonRequest(int method, String url, @Nullable JSONObject jsonRequest,
                       Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener,
                       Class<T> clazz, Map<String, String> headers) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        this.clazz = clazz;
        this.headers = headers;
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

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d(LoginActivity.TAG, "GsonRequest response json: "+json);
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
