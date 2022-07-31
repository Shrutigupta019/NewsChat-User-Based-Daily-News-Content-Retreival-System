package com.shruti.whatsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shruti.whatsapp.R;

public class InviteFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        getSupportActionBar().setTitle("Invite a Friend");
    }
}