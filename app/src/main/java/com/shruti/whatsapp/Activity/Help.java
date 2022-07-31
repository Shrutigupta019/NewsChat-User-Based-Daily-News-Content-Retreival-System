package com.shruti.whatsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shruti.whatsapp.R;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setTitle("Help");
    }
}