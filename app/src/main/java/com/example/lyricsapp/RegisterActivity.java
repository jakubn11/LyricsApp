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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.lyricsapp.classes.AESCrypt;
import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private final int SELECT_PHOTO = 1;
    private String hashPass;
    private String allEmails;
    private String allUsernames;
    private File f;
    private TextInputLayout usernameRegister, emailRegister, passwordRegister, passwordConfirmRegister;
    private DatabaseHelper databaseHelper;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameRegister = findViewById(R.id.usernameRegisterEditText);
        emailRegister = findViewById(R.id.emailRegisterEditText);
        passwordRegister = findViewById(R.id.passwordRegisterEditText);
        passwordConfirmRegister = findViewById(R.id.passwordConfirmRegisterEditText);
        ImageView chooseImageView = findViewById(R.id.chooseImageView);
        Button registerBtn = findViewById(R.id.registerBtn);
        imageView = findViewById(R.id.imageViewRegister);
        Toolbar toolbar = findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registrace");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Boolean validateUsername() {
        String username = Objects.requireNonNull(usernameRegister.getEditText()).getText().toString().trim();

        try {
            databaseHelper.openDataBase();
            Cursor userNames = databaseHelper.getUsersNames();

            while (userNames.moveToNext()) {
                allUsernames = userNames.getString(userNames.getColumnIndex("prezdivka"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseHelper.close();

        if (username.isEmpty()) {
            usernameRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (username.equals(allUsernames)) {
            usernameRegister.setError("Tato uživatelské jméno už je použito");
            return false;
        } else if (username.contains(" ")) {
            usernameRegister.setError("Uživatelské jméno musí být bez mezer");
            return false;
        } else if (username.length() > 15) {
            usernameRegister.setError("Maximální délka je 15 znaků");
            return false;
        } else {
            usernameRegister.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = passwordRegister.getEditText().getText().toString().trim();

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
            passwordRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!password.matches(passwordValidation)) {
            passwordRegister.setError("Heslo je příliš slabé");
            return false;
        } else if (password.contains(" ")) {
            passwordRegister.setError("Heslo musí být bez mezer");
            return false;
        } else {
            passwordRegister.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String confirmPassword = passwordConfirmRegister.getEditText().getText().toString().trim();
        String password = passwordRegister.getEditText().getText().toString().trim();

        if (confirmPassword.isEmpty()) {
            passwordConfirmRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!confirmPassword.equals(password)) {
            passwordConfirmRegister.setError("Hesla nejsou stená");
            return false;
        } else {
            passwordConfirmRegister.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String email = emailRegister.getEditText().getText().toString().trim();
        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        try {
            databaseHelper.openDataBase();
            Cursor emails = databaseHelper.getUsersEmails();

            while (emails.moveToNext()) {
                allEmails = emails.getString(emails.getColumnIndex("email"));
            }

            databaseHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (email.isEmpty()) {
            emailRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!email.matches(emailValidation)) {
            emailRegister.setError("Neplatná emailová adresa");
            return false;
        } else if (email.equals(allEmails)) {
            emailRegister.setError("Tento email už je použit");
            return false;
        } else {
            emailRegister.setError(null);
            return true;
        }
    }

    public void insertUzivatel(View v) {
        if (!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        AESCrypt crypt = new AESCrypt();
        String pass = Objects.requireNonNull(passwordRegister.getEditText()).getText().toString().trim();
        try {
            hashPass = AESCrypt.encrypt(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseHelper.getWritableDatabase();
        Uzivatel newUzivatel = new Uzivatel();
        if (f == null) {
            newUzivatel.setPrezdivka(Objects.requireNonNull(usernameRegister.getEditText()).getText().toString().trim());
            newUzivatel.setEmail(Objects.requireNonNull(emailRegister.getEditText()).getText().toString().trim());
            newUzivatel.setHeslo(hashPass);
            databaseHelper.openDataBase();
            databaseHelper.insertUserWithoutProfileImage(newUzivatel);
            databaseHelper.close();
        } else {
            newUzivatel.setPrezdivka(Objects.requireNonNull(usernameRegister.getEditText()).getText().toString().trim());
            newUzivatel.setEmail(Objects.requireNonNull(emailRegister.getEditText()).getText().toString().trim());
            newUzivatel.setProfilovka(imageViewToByte(imageView));
            newUzivatel.setHeslo(hashPass);
            databaseHelper.openDataBase();
            databaseHelper.insertUserWithProfileImage(newUzivatel);
            databaseHelper.close();
        }
        Toast.makeText(RegisterActivity.this, "Účet byl vytvořen", Toast.LENGTH_LONG).show();

        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void selectImage(View v) {
        Intent imagePick = new Intent(Intent.ACTION_PICK);
        imagePick.setType("image/*");
        startActivityForResult(imagePick, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = uri.getPath();
            f = new File(path);
            Log.v("cesta k obrázku", f.getPath());
            try {
                InputStream inputstream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(this, "Profilová fotka vybrána", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}