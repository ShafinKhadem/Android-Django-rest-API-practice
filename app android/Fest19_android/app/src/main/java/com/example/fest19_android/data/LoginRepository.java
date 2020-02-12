package com.example.fest19_android.data;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fest19_android.data.model.LoggedInUser;
import com.example.fest19_android.rest.MyJsonObjectRequest;
import com.example.fest19_android.rest.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.fest19_android.ui.login.LoginActivity.API_BASE;
import static com.example.fest19_android.ui.login.LoginActivity.TAG;
import static java.lang.Thread.currentThread;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    private MutableLiveData<Result> result = new MutableLiveData<>();
    private final Map<String, String> headers = new HashMap<>();

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            synchronized (LoginRepository.class) {
                instance = new LoginRepository(dataSource);
            }
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    private void checkThread() {
        Log.d(TAG, Looper.myLooper()==Looper.getMainLooper() ? "On UI thread" : "On thread: "+currentThread().getName());
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        headers.put("Authorization", "Token " + user.getAuthToken());
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    public LiveData<Result> getResult() {
        return result;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void login(final String username, String password) {
        // handle login

        final String url = API_BASE+"auth/token/login/";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        String authToken = response.optString("auth_token");
                        if (!authToken.isEmpty()) {
                            Result<LoggedInUser> res = dataSource.login(username, authToken);
                            if (res instanceof Result.Success) {
                                setLoggedInUser(((Result.Success<LoggedInUser>) res).getData());
                                result.setValue(res);
                            } else {
                                result.setValue(new Result.Error(new IOException("Error logging in")));
                            }
                        } else {
                            result.setValue(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "login didn't work, LOL!", error);
                        result.setValue(new Result.Error(error));
                    }
                });
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }

    public void restoreLogin(final String username, final String authToken) {
        final String url = API_BASE+"auth/users/me/";

        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        if (response.optString("username").equals(username)) {
                            Result<LoggedInUser> res = dataSource.login(username, authToken);
                            if (res instanceof Result.Success) {
                                setLoggedInUser(((Result.Success<LoggedInUser>) res).getData());
                                result.setValue(res);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "initial login didn't work, LOL! + headers", error);
                        result.setValue(new Result.Error(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }

    public void logout() {
        final String url = API_BASE+"logout/";

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "logout response: "+response.toString());
                        if (response.optString("Logout_status").equals("OK")) {
                            dataSource.logout();
                            user = null;
                            headers.clear();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "logout didn't work, LOL!", error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }
}
