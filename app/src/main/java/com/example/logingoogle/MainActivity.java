package com.example.logingoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.logingoogle.back_firebase.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database = new FirebaseDatabase(this);

        startActivity(new Intent(this, RegisterUserActivity.class));
        finish();
    }
}
