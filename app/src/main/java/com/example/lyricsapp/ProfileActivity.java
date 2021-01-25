package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText profileUsername, profilePassword,profileEmail;
    private Button profileChange;
    private DatabaseHelper databaseHelper;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        profileUsername = findViewById(R.id.profileUsername);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileChange = findViewById(R.id.profileChange);
        test = findViewById(R.id.textView2);
    }

    public void updateUzivatel(View v) {
//        databaseHelper = new DatabaseHelper(this);
//        databaseHelper.getWritableDatabase();
//        databaseHelper.openDataBase();
//        Uzivatel newUzivatel = new Uziv   atel();
//        Uzivatel uzivatel = new Uzivatel();
//        newUzivatel.setPrezdivka(profileUsername.getText().toString());
//        newUzivatel.setEmail(profileEmail.getText().toString());
//        newUzivatel.setHeslo(profilePassword.getText().toString());
//        databaseHelper.updateUzivatel(uzivatel, newUzivatel);
//        databaseHelper.close();
        String username = getIntent().getExtras().getString("username");
        test.setText(username);
    }
}