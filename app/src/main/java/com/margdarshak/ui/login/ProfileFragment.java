package com.margdarshak.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationView;
import com.margdarshak.R;
import com.margdarshak.util.CircleTransformation;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;

    private TextView welcomeTextView;
    private Chip logoutChip;

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
        final NavController navController = Navigation.findNavController(view);
        // Google sign-in config
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        welcomeTextView = view.findViewById(R.id.welcome_text_view);
        logoutChip = view.findViewById(R.id.logout);

        logoutChip.setOnClickListener(view1 -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), task -> {
                        removeUserDetailsFromUI();
                        navController.popBackStack();
                        navController.navigate(R.id.navigation_login);
                    });
        });
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null){
            showWelcomeMessage(account.getDisplayName());
        }

        Log.d(TAG, "is element shown? " + welcomeTextView.isShown());
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

    private void removeUserDetailsFromUI() {
        Toast.makeText(getContext(), "Logout successful", Toast.LENGTH_LONG).show();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.username)).setText(R.string.nav_header_title);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.usermail)).setText(R.string.nav_header_subtitle);
        //Picasso.get().load(model.getPhotoURI()).transform(new CircleTransformation()).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userpicture));
        ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userpicture)).setImageResource(R.mipmap.ic_launcher_round);
    }

    private void showWelcomeMessage(String userName) {
        welcomeTextView.setText(getString(R.string.welcome, userName));
    }
}
