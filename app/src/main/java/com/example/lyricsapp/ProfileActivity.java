package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView profileImage;
    private EditText profileUsername, profilePassword, profilePasswordConfirm, profileEmail;
    private Button profileChange;
    private TextView changeProfileImage;
    private DatabaseHelper databaseHelper;
    private String username;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
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
        profilePasswordConfirm = findViewById(R.id.profilePasswordConfirm);
        changeProfileImage = findViewById(R.id.changeProfileImageTextView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle2 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle2);
        toggle2.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

//        databaseHelper = new DatabaseHelper(this);
//        databaseHelper.getReadableDatabase();
//        databaseHelper.openDataBase();
//        Uzivatel uzivatel = new Uzivatel();
//        databaseHelper.getPassword("a");
//        profileEmail.setText(uzivatel.getHeslo());
//        databaseHelper.close();
    }

    private Boolean validateUsername() {
        String username = profileUsername.getText().toString();

        if (username.isEmpty()) {
            profileUsername.setError("Řádek nesmí být prázdný");
            return false;
        } else if (username.length() >= 15) {
            profileUsername.setError("Uživatelské jméno může obsahovat maximálně 15 znaků");
            return false;
        } else if (username.contains(" ")) {
            profileUsername.setError("Uživatelské jméno musí být bez mezer");
            return false;
        } else {
            profileUsername.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = profilePassword.getText().toString();

        String passwordValidation = "^" +
//                "(?=.*[0-9])" +              //at least 1 digit
//                "(?=.*[a-z])" +              //at least 1 lower case letter
//                "(?=.*[A-Z])" +              //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +           //any letter
                "(?=.*[@#$%^&+=])" +         //at least 1 special character
                "(?=\\S+$)" +                //no white spaces
                ".{8,}" +                    //at least 4 characters
                "$";

        if (password.isEmpty()) {
            profilePassword.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!password.matches(passwordValidation)) {
            profilePassword.setError("Heslo je příliš slabé");
            return false;
        } else if (password.contains(" ")) {
            profilePassword.setError("Heslo musí být bez mezer");
            return false;
        } else {
            profilePassword.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String confirmPassword = profilePasswordConfirm.getText().toString();
        String password = profilePasswordConfirm.getText().toString();

        if (confirmPassword.isEmpty()) {
            profilePasswordConfirm.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!confirmPassword.equals(password)) {
            profilePasswordConfirm.setError("Hesla nejsou stená");
            return false;
        } else {
            profilePasswordConfirm.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String email = profileEmail.getText().toString();
        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            profileEmail.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!email.matches(emailValidation)) {
            profileEmail.setError("Neplatná emailová adresa");
            return false;
        } else {
            profileEmail.setError(null);
            return true;
        }
    }

    private Boolean validateImage() {
        if (f == null) {
            Toast.makeText(ProfileActivity.this, "Obrázek nebyl vybrán", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void updateUzivatel(View v) {
        if(!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword() | !validateImage())
        {
            return;
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = extras.getString("USERNAME2");

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

        Intent startLogin = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(startLogin);

        Toast.makeText(ProfileActivity.this, "Úspěšně změněno", Toast.LENGTH_LONG).show();
        profileUsername.getText().clear();
        profileEmail.getText().clear();
        profilePassword.getText().clear();
        profilePasswordConfirm.getText().clear();
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

    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_all_songs:
                Intent songs = new Intent(getApplicationContext(), SongsActivity.class);
                startActivity(songs);
                break;
            case R.id.nav_fav_songs:
                Intent fav_songs = new Intent(getApplicationContext(), FavouriteSongsActivity.class);
                startActivity(fav_songs);
                break;
            case R.id.nav_search:
                Toast.makeText(getApplicationContext(), "Vyhledávání", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String username = extras.getString("USERNAME");
                profile.putExtra("USERNAME2", username);
                startActivity(profile);
                break;
            case R.id.nav_logout:
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}