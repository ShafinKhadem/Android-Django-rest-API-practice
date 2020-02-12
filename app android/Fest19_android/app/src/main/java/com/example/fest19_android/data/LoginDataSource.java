package com.example.fest19_android.data;

import com.example.fest19_android.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private LoggedInUser user = null;

    public Result<LoggedInUser> login(final String username, String authToken) {
        try {
            user = new LoggedInUser(username, authToken);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {

    }
}
