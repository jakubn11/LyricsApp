package com.example.lyricsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText profileUsername, profilePassword,profileEmail;
    private Button profileChange;
    private DatabaseHelper databaseHelper;
    private TextView test;
    private String username;
    int SELECT_PHOTO= 1;
    Uri uri;
    String path;
    File f;

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

//        databaseHelper = new DatabaseHelper(this);
//        databaseHelper.getWritableDatabase();
//        databaseHelper.openDataBase();
//        Uzivatel test = databaseHelper.getUserAll("x");
//        databaseHelper.close();

    }

    public void updateUzivatel(View v) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = extras.getString("USERNAME");

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        databaseHelper.openDataBase();
        Uzivatel newUzivatel = new Uzivatel();
        newUzivatel.setPrezdivka(profileUsername.getText().toString());
        newUzivatel.setEmail(profileEmail.getText().toString());
        newUzivatel.setHeslo(profilePassword.getText().toString());
        newUzivatel.setProfilovka(imageViewToByte(profileImage));
        databaseHelper.updateUser(username, newUzivatel);
        databaseHelper.close();

        String newUsername = profileUsername.getText().toString();

        Intent startLogin = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(startLogin);

        Toast.makeText(ProfileActivity.this, "Úspěšně změněno", Toast.LENGTH_LONG).show();
        profileUsername.getText().clear();
        profileEmail.getText().clear();
        profilePassword.getText().clear();
        profileImage.setImageDrawable(null);
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void chooseImage(View v) {
        Intent imagePick = new Intent(Intent.ACTION_PICK);
        imagePick.setType("image/*");
        startActivityForResult(imagePick, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            path = uri.getPath();
            f = new File(path);
            Log.v("cesta k obrázku", f.getPath());
            try {
                InputStream inputstream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
                profileImage.setImageBitmap(bitmap);
                Toast.makeText(this,"Profilová fotka vybrána", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}