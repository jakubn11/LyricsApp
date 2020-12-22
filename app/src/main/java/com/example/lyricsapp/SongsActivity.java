package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class SongsActivity extends AppCompatActivity {
    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        this.testTextView = findViewById(R.id.testTextView);

        testTextView.setText("ahoj");



    }
}