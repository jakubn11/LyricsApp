package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLogin, passwordLogin;
    private Button loginBtn, registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogin = (EditText) findViewById(R.id.usernameLoginEditText);
        passwordLogin = (EditText) findViewById(R.id.passwordLoginEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerLink = (Button) findViewById(R.id.registerInLoginBtn);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(startRegister);
            }
        });

    }

    public void login(View v) {
/*        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        try {
            String username = usernameLogin.getText().toString();
            String password = passwordLogin.getText().toString();
            List<String> list = databaseAccess.getLogin(username, password);*/
            Intent login = new Intent(getApplicationContext(), SongsActivity.class);
            startActivity(login);
/*            Toast.makeText(LoginActivity.this, username + password, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Špatné heslo nebo uživatelské jméno", Toast.LENGTH_LONG).show();
            databaseAccess.close();
        }*/
    }
}
