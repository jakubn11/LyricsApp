package com.example.lyricsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class RegisterActivity extends AppCompatActivity {
    int SELECT_PHOTO= 1;
    Uri uri;
    String path;
    File f;
    private EditText usernameRegister, emailRegister, passwordRegister, passwordConfirmRegister;
    private Button imagePicker, registerBtn;
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
        imagePicker = findViewById(R.id.imagePickerRegisterBtn);
        registerBtn = findViewById(R.id.registerBtn);
        imageView = findViewById(R.id.imageViewRegister);
    }

    private Boolean validateUsername() {
        String username = usernameRegister.getText().toString();

        if (username.isEmpty()) {
            usernameRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (username.length() >= 15) {
            usernameRegister.setError("Uživatelské jméno může obsahovat maximálně 15 znaků");
            return false;
        } else if (username.contains(" ")) {
            usernameRegister.setError("Uživatelské jméno musí být bez mezer");
            return false;
        } else {
            usernameRegister.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = passwordRegister.getText().toString();

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
        String confirmPassword = passwordConfirmRegister.getText().toString();
        String password = passwordRegister.getText().toString();

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
        String email = emailRegister.getText().toString();
        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            emailRegister.setError("Řádek nesmí být prázdný");
            return false;
        } else if (!email.matches(emailValidation)) {
            emailRegister.setError("Neplatná emailová adresa");
            return false;
        } else {
            emailRegister.setError(null);
            return true;
        }
    }

    private Boolean validateImage() {
        if (f == null) {
            Toast.makeText(RegisterActivity.this, "Obrázek nebyl vybrán", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void insertUzivatel(View v) {
        if(!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword() | !validateImage())
        {
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        databaseHelper.openDataBase();
        Uzivatel newUzivatel = new Uzivatel();
        newUzivatel.setPrezdivka(usernameRegister.getText().toString());
        newUzivatel.setEmail(emailRegister.getText().toString());
        newUzivatel.setProfilovka(imageViewToByte(imageView));
        newUzivatel.setHeslo(passwordRegister.getText().toString());
        databaseHelper.vlozeniUzivatele(newUzivatel);
        databaseHelper.close();
        Toast.makeText(RegisterActivity.this, "Vloženo", Toast.LENGTH_LONG).show();
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void selectImage(View v) {
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
                 imageView.setImageBitmap(bitmap);
                 Toast.makeText(this,"Profilová fotka vybrána", Toast.LENGTH_SHORT).show();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
            } catch (IOException e) {
                 e.printStackTrace();
            }
        }
    }
}