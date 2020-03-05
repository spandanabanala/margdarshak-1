package com.margdarshak.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.net.Uri;
import android.util.Patterns;

import com.margdarshak.ui.data.LoginRepository;
import com.margdarshak.ui.data.Result;
import com.margdarshak.ui.data.model.LoggedInUser;
import com.margdarshak.R;

public class LoginViewModel extends ViewModel {

    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
    /*final MutableLiveData<AuthenticationState> authenticationState =
            new MutableLiveData<>();
    String username;*/

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    void setSuccessResult(String userName, String userEmail, Uri photoURI){
        LoginResult lr = new LoginResult(new LoggedInUserView(userName, userEmail, photoURI));
        this.loginResult.setValue(lr);
    }

    void setErrorResult(Integer errorCode){
        LoginResult lr = new LoginResult(errorCode);
        this.loginResult.setValue(lr);
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), null, null)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
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
