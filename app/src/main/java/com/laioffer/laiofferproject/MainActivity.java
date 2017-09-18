package com.laioffer.laiofferproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //EventFragment listFragment;
    //CommentFragment gridFragment;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSubmitButton;
    private Button mRegisterButton;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //create an father Instance which used to call the resource belongs to father.
        setContentView(R.layout.activity_main);
//        ListView eventListView = (ListView) findViewById(R.id.event_list);
//
//        EventAdapter adapter = new EventAdapter(this);
//        eventListView.setAdapter(adapter);
//
//        Log.e("Life Cycle test", "We are at onCreate()");
//        if (findViewById(R.id.fragment_container) != null) {
//            Fragment fragment= isTable() ? new CommentFragment() : new EventFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
//        }
        //add List view
//        if (isTable()) {
//            listFragment = new EventFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.event_container, listFragment).commit();
//        }
//        //add Grid view
//        gridFragment = new CommentFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.comment_container, gridFragment).commit();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsernameEditText = (EditText) findViewById(R.id.editTextLogin);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPassword);
        mRegisterButton = (Button) findViewById(R.id.register);
        mSubmitButton = (Button) findViewById(R.id.submit);
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();
                final User user = new User(username, password, System.currentTimeMillis());
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username)) {
                            Toast.makeText(getBaseContext(),"Username is already exist, please change another one", Toast.LENGTH_SHORT).show();
                        } else if(!username.equals("") && !password.equals("")) {
                            mDatabase.child("users").child(user.getUsername()).setValue(user);
                            Toast.makeText(getBaseContext(),"Successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username) && (password.equals(dataSnapshot.child(username).child("password").getValue()))) {
                            Log.i("Your log", "Login successfully");
                            Intent myIntent = new Intent(MainActivity.this, EventActivity.class);
                            myIntent.putExtra("Username", username);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getBaseContext(), "Please Login Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private boolean isTable() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_LAYOUTDIR_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

//    @Override
//    public void onItemSelected(int position) {
//        gridFragment.onItemSelected(position);
//    }
//
//    @Override
//    public void onItemClicked(int position) {
//        listFragment.onItemClicked(position);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life Cycle test", "We are at onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Life Cycle test", "We are at onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Life Cycle test", "We are at onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Life Cycle test", "We are at onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Life Cycle test", "We are at onDestroy ()");
    }

}
