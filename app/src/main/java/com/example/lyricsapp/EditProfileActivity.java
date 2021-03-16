package com.example.lyricsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextInputLayout profileUsername, profilePassword, profilePasswordConfirm, profileEmail;
    private DatabaseHelper databaseHelper;
    private String user;
    private Toolbar toolbar;
    int SELECT_PHOTO = 1;
    Uri uri;
    String path;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profileImage = findViewById(R.id.imageViewEditProfile);
        profileUsername = findViewById(R.id.profileUsernameEdit);
        profileEmail = findViewById(R.id.profileEmailEdit);
        profilePassword = findViewById(R.id.profilePasswordEdit);
        profilePasswordConfirm = findViewById(R.id.profilePasswordConfirm);
        toolbar = findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Úprava profilu");


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        user = extras.getString("USERNAME_FROM_PROFILE");

        databaseHelper.openDataBase();
        Cursor data = databaseHelper.getUserHeader(user);
        data.moveToFirst();

        profileUsername.getEditText().setText(data.getString(data.getColumnIndex("prezdivka")));
        profileEmail.getEditText().setText(data.getString(data.getColumnIndex("email")));
        profilePassword.getEditText().setText(data.getString(data.getColumnIndex("heslo")));
        byte[] blob = data.getBlob(data.getColumnIndex("profilovka"));
        if (blob == null) {
            Log.i("PROFILE PHOTO","V databázi není pro tento účet profilová fotka");
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            profileImage.setImageBitmap(bitmap);
        }
        databaseHelper.close();
    }

    private Boolean validateUsername() {
        String username = profileUsername.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            profileUsername.setError("Řádek nesmí být prázdný");
            return false;
        } else if (username.contains(" ")) {
            profileUsername.setError("Uživatelské jméno musí být bez mezer");
            return false;
        }else if (username.length() >= 15) {
            profileUsername.setError("Maximální délka je 15 znaků");
            return false;
        }  else {
            profileUsername.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = profilePassword.getEditText().getText().toString().trim();

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
        String confirmPassword = profilePasswordConfirm.getEditText().getText().toString().trim();
        String password = profilePasswordConfirm.getEditText().getText().toString().trim();

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
        String email = profileEmail.getEditText().getText().toString().trim();
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

    public void updateUzivatel(View v) {
        if (!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        databaseHelper.getWritableDatabase();
        Uzivatel newUzivatel = new Uzivatel();
        if (f == null) {
            newUzivatel.setPrezdivka(profileUsername.getEditText().getText().toString().trim());
            newUzivatel.setEmail(profileEmail.getEditText().getText().toString().trim());
            newUzivatel.setHeslo(profilePassword.getEditText().getText().toString().trim());
            databaseHelper.openDataBase();
            databaseHelper.updateUserWithoutProfileImage(user, newUzivatel);
            databaseHelper.close();
        } else {
            newUzivatel.setPrezdivka(profileUsername.getEditText().getText().toString().trim());
            newUzivatel.setEmail(profileEmail.getEditText().getText().toString().trim());
            newUzivatel.setHeslo(profilePassword.getEditText().getText().toString().trim());
            newUzivatel.setProfilovka(imageViewToByte(profileImage));
            databaseHelper.openDataBase();
            databaseHelper.updateUserWithProfileImage(user, newUzivatel);
            databaseHelper.close();
        }
        Intent logout = new Intent(getApplicationContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logout);

        Toast.makeText(EditProfileActivity.this, "Úspěšně změněno", Toast.LENGTH_LONG).show();
        profileUsername.getEditText().getText().clear();
        profileEmail.getEditText().getText().clear();
        profilePassword.getEditText().getText().clear();
        profilePasswordConfirm.getEditText().getText().clear();
        profileImage.setImageDrawable(null);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
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
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            path = uri.getPath();
            f = new File(path);
            Log.v("cesta k obrázku", f.getPath());
            try {
                InputStream inputstream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
                profileImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Profilová fotka vybrána", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}