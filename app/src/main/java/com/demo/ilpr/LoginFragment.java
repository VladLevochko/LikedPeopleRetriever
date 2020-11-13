package com.demo.ilpr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.instagram4j.instagram4j.IGClient;


public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    private Button mIgLoginButton;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mIgLoginButton = view.findViewById(R.id.login_button);
        mIgLoginButton.setOnClickListener(this::loginWithInstagram);

        mUsernameEditText = view.findViewById(R.id.et_user_name);
        mPasswordEditText = view.findViewById(R.id.et_user_password);
    }

    private void loginWithInstagram(View v) {
        try {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            IGClient client = IGClient.builder()
                    .username(username)
                    .password(password)
                    .login();

            LikesViewModel model = new ViewModelProvider(getActivity()).get(LikesViewModel.class);

            Log.d(TAG, "login successful");

            model.setIgClient(client);

            ImagesProvider provider = new ImagesProvider(client);
            model.setImagesProvider(provider);

            openLikesFragment();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();

            e.printStackTrace();
        }
    }

    private void openLikesFragment() {
        LikesFragment fragment = new LikesFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(getId(), fragment)
                .commit();
    }
}
