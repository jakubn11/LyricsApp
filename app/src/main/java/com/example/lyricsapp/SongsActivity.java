package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lyricsapp.database.DatabaseHelper;

import java.io.IOException;

public class SongsActivity extends AppCompatActivity {
    private TextView testTextView;
    private DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        this.testTextView = findViewById(R.id.testTextView);

        myDbHelper = new DatabaseHelper(this);
        myDbHelper.createDataBase();
        myDbHelper.openDataBase();

        String text = myDbHelper.getUserNameFromDB();
        testTextView.setText(text);

        myDbHelper.close();

    }
}