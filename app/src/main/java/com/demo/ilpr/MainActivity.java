package com.demo.ilpr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LikesViewModel model = new ViewModelProvider(this).get(LikesViewModel.class);

//        if (!isLoggedIn()) {
            openLoginFragment();
//        } else {
//            openLikesFragment();
//        }

    }

    private void openLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    private void openLikesFragment() {
        LikesFragment fragment = new LikesFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

}
