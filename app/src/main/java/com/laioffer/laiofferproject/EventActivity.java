package com.laioffer.laiofferproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {

    ReportEventFragment reportFragment;
    String username;
    TextView usernameTextView;
    ShowEventFragment showEventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //get Username from MainActivity
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        usernameTextView = (TextView) findViewById(R.id.text_user);
        usernameTextView.setText("Welcome, " + username);

        if (reportFragment == null) {
            reportFragment = new ReportEventFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ReportEventFragment()).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profile:
                                getSupportFragmentManager().beginTransaction().replace(                 // 可以理解为跳转页面；
                                        R.id.fragment_container, new ReportEventFragment()
                                ).commit();
                                break;
                            case R.id.action_events:
                                getSupportFragmentManager().beginTransaction().replace(
                                        R.id.fragment_container, showEventFragment == null ? new ShowEventFragment() : showEventFragment
                                ).commit();
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    public String getUsername() {
        return username;
    }
}
