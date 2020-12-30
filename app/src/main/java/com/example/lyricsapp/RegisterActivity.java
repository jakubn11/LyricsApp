package com.example.lyricsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    int SELECT_PHOTO= 1;
    Uri uri;
    private EditText usernameRegister, emailRegister, passwordRegister, passwordConfirmRegister;
    private Button imagePicker, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameRegister = (EditText) findViewById(R.id.usernameRegisterEditText);
        emailRegister = (EditText) findViewById(R.id.emailRegisterEditText);
        passwordRegister = (EditText) findViewById(R.id.passwordRegisterEditText);
        passwordConfirmRegister = (EditText) findViewById(R.id.passwordConfirmRegisterEditText);
        imagePicker = (Button) findViewById(R.id.imagePickerRegisterBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        //this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    }

        public void insertUzivatel(android.view.View v) {
           /* databaseAccess.open();
            Uzivatel newUzivatel = new Uzivatel();

            if(passwordRegister.getText().toString().equals(passwordConfirmRegister.getText().toString())) {
                newUzivatel.setPrezdivka(usernameRegister.getText().toString());
                newUzivatel.setHeslo(passwordConfirmRegister.getText().toString());
                newUzivatel.setEmail(emailRegister.getText().toString());
                databaseAccess.insertUzivatel(newUzivatel);
                Toast.makeText(RegisterActivity.this, "Byl jsi úspěšně zaregistrován!", Toast.LENGTH_LONG).show();
                databaseAccess.close();*/
                Intent register = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(register);
               /* this.finish();
            }else{
                Toast.makeText(RegisterActivity.this, "Hesla se neshodují", Toast.LENGTH_LONG).show();
            }*/
        }

        public void selectImage(View v){


             //
               // Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(login);

                Intent imagePick = new Intent(Intent.ACTION_PICK);
                imagePick.setType("image/*");
                startActivityForResult(imagePick, SELECT_PHOTO);
                


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
             uri = data.getData();
             try {
                 Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
            } catch (IOException e) {
                 e.printStackTrace();
            }
        }
    }
}