package com.example.schoolhub_android.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schoolhub_android.Common.Common;
import com.example.schoolhub_android.Modal.School.School;
import com.example.schoolhub_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    EditText et_school_key;
    Button b_submit;

    SharedPreferences sharedPreferences;
    String school_key, key, name, address, city;
    SharedPreferences.Editor editor;

    School school;

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE);
        key = sharedPreferences.getString("key", "0");
        name = sharedPreferences.getString("name", "0");
        address = sharedPreferences.getString("address", "0");
        city = sharedPreferences.getString("city", "0");
        if(!key.equals("0")) {
            StartNotificationActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_school_key = findViewById(R.id.et_school_key);
        b_submit = findViewById(R.id.b_submit);
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()) {
                    school_key = et_school_key.getText().toString();
                    FirebaseEventListener();
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void FirebaseEventListener() {
        Common.mSchoolsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    school = snapshot.getValue(School.class);
                    assert school != null;
                    if(school_key.equals(school.getKey())) {
                        editor = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
                        editor.putString("key", school.getKey());
                        editor.putString("name", school.getName());
                        editor.putString("address", school.getAddress());
                        editor.putString("city", school.getCity());
                        editor.apply();
                        StartNotificationActivity();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase onCancelled: ", databaseError.toString());
            }
        });
    }

    public void StartNotificationActivity() {
        Intent i = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(i);
        finish();
    }
}