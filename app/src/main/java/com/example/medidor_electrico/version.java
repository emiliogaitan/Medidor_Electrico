package com.example.medidor_electrico;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class version extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        ActionBar bar = getSupportActionBar();
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }

    }
}
