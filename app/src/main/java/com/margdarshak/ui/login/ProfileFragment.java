package com.margdarshak.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.margdarshak.R;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private LoginViewModel loginViewModel;

    private TextView welcomeTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        loginViewModel = new LoginViewModelFactory().create(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcomeTextView = view.findViewById(R.id.welcome_text_view);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null){
            showWelcomeMessage(account.getDisplayName());
        }

        Log.d(TAG, "is element shown? " + welcomeTextView.isShown());
        final NavController navController = Navigation.findNavController(view);
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null || loginResult.getError() != null) {
                    navController.navigate(R.id.navigation_login);
                }
                if (loginResult.getSuccess() != null) {
                    showWelcomeMessage(loginResult.getSuccess().getUserName());
                }
            }
        });
    }

    private void showWelcomeMessage(String userName) {
        welcomeTextView.setText(getString(R.string.welcome, userName));
    }
}
