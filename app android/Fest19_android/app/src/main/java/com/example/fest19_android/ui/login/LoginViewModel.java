package com.example.fest19_android.ui.login;

import android.util.Patterns;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.fest19_android.R;
import com.example.fest19_android.data.LoginDataSource;
import com.example.fest19_android.data.LoginRepository;
import com.example.fest19_android.data.Result;
import com.example.fest19_android.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LiveData<LoginResult> loginResult;
    private LoginRepository loginRepository;

    public LoginViewModel() {
        super();
        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loginResult = Transformations.map(loginRepository.getResult(), new Function<Result, LoginResult>() {
            @Override
            public LoginResult apply(Result res) {
                return res instanceof Result.Success ? new LoginResult(new LoggedInUserView(((Result.Success<LoggedInUser>) res).getData().getUsername())) : new LoginResult(R.string.login_failed);
            }
        });
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username, final String password) {
        // can be launched in a separate asynchronous job

        loginRepository.login(username, password);
    }

    void restoreLogin(final String username, final String authToken) {
        // can be launched in a separate asynchronous job

        loginRepository.restoreLogin(username, authToken);
    }

    void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
