package com.example.lyricsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lyricsapp.classes.AESCrypt;
import com.example.lyricsapp.classes.User;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextInputLayout profileUsername, profilePassword, profilePasswordConfirm, profileEmail;
    private DatabaseHelper databaseHelper;
    private String hashPass, user;
    private int userId;
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
        Toolbar toolbar = findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Úprava profilu");


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        user = extras.getString("USERNAME_FROM_PROFILE");
        databaseHelper.openDataBase();
        Cursor data = databaseHelper.getUserHeader(user);
        data.moveToFirst();

        Objects.requireNonNull(profileUsername.getEditText()).setText(data.getString(data.getColumnIndex("prezdivka")));
        Objects.requireNonNull(profileEmail.getEditText()).setText(data.getString(data.getColumnIndex("email")));
        databaseHelper.close();
    }

    private Boolean validateUsername() {
        String username = Objects.requireNonNull(profileUsername.getEditText()).getText().toString().trim();

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
        String password = Objects.requireNonNull(profilePassword.getEditText()).getText().toString().trim();

//        String passwordValidation = "^" +
////                "(?=.*[0-9])" +              //at least 1 digit
////                "(?=.*[a-z])" +              //at least 1 lower case letter
////                "(?=.*[A-Z])" +              //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +           //any letter
//                "(?=.*[@#$%^&+=])" +         //at least 1 special character
//                "(?=\\S+$)" +                //no white spaces
//                ".{8,}" +                    //at least 4 characters
//                "$";

        if (password.isEmpty()) {
            profilePassword.setError("Řádek nesmí být prázdný");
            return false;
        } else if (password.contains(" ")) {
            profilePassword.setError("Heslo musí být bez mezer");
            return false;
        } else if (password.length() < 8) {
            profilePassword.setError("Minimální délka hesla je 8 znaků");
            return false;
        } else {
            profilePassword.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String confirmPassword = Objects.requireNonNull(profilePasswordConfirm.getEditText()).getText().toString().trim();
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
        String email = Objects.requireNonNull(profileEmail.getEditText()).getText().toString().trim();
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

        AESCrypt crypt = new AESCrypt();
        String pass = Objects.requireNonNull(profilePassword.getEditText()).getText().toString().trim();
        try {
            hashPass = AESCrypt.encrypt(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseHelper.getWritableDatabase();
        User newUser = new User();
        if (f == null) {
            newUser.setPrezdivka(Objects.requireNonNull(profileUsername.getEditText()).getText().toString().trim());
            newUser.setEmail(Objects.requireNonNull(profileEmail.getEditText()).getText().toString().trim());
            newUser.setHeslo(hashPass);
            databaseHelper.openDataBase();
            databaseHelper.updateUserWithoutProfileImage(user, newUser);
            databaseHelper.close();
        } else {
            newUser.setPrezdivka(Objects.requireNonNull(profileUsername.getEditText()).getText().toString().trim());
            newUser.setEmail(Objects.requireNonNull(profileEmail.getEditText()).getText().toString().trim());
            newUser.setHeslo(hashPass);
            newUser.setProfilovka(imageViewToByte(profileImage));
            databaseHelper.openDataBase();
            databaseHelper.updateUserWithProfileImage(user, newUser);
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
        Objects.requireNonNull(profilePasswordConfirm.getEditText()).getText().clear();
        profileImage.setImageDrawable(null);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
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
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}