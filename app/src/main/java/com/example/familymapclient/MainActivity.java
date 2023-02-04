package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment==null) {
            fragment = new LoginFragment();
        } else {
            fragment = new MapFragment();
        }
        manager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

    }

    public void switchFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        fragment = new MapFragment();
        manager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}