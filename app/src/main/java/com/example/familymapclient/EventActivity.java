package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.familymapclient.Fragments.MapFragment;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class EventActivity extends AppCompatActivity {

    private Activity activity = this;
    private DataCache cache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Family Map");

        String eventID = intent.getStringExtra("eventID");
        Event event = cache.getEvent(eventID);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.activity_map_fragment_container);
        fragment = new MapFragment(eventID);
        manager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    //Up button here
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EventActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return EventActivity.super.onOptionsItemSelected(item);
    }
}