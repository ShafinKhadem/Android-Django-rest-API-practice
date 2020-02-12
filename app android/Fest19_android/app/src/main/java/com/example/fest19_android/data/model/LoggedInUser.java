package com.example.fest19_android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private String authToken;

    public LoggedInUser(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
