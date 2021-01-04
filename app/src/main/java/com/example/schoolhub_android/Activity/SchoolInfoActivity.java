package com.example.schoolhub_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolhub_android.Common.Common;
import com.example.schoolhub_android.R;

public class SchoolInfoActivity extends AppCompatActivity {

    TextView tv_school_key, tv_school_name, tv_school_address_city;
    Button btn_share;

    SharedPreferences sharedPreferences;
    String school_key, school_name, school_address, school_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);

        sharedPreferences = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE);
        school_key = sharedPreferences.getString("key", null);
        school_name = sharedPreferences.getString("name", null);
        school_address = sharedPreferences.getString("address", null);
        school_city = sharedPreferences.getString("city", null);

        tv_school_key = findViewById(R.id.tv_school_key);
        btn_share = findViewById(R.id.btn_share);
        tv_school_name = findViewById(R.id.tv_school_name);
        tv_school_address_city = findViewById(R.id.tv_school_address_city);

        tv_school_key.setText(school_key);
        tv_school_name.setText(school_name);
        tv_school_address_city.setText(getString(R.string.tv_school_address_city, school_address, school_city));

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject, school_name, school_address, school_city) );
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.body, school_key));
                startActivity(Intent.createChooser(i, getString(R.string.title)));
            }
        });
    }
}