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

import com.example.lyricsapp.database.DatabaseHelper;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLogin, passwordLogin;
    private Button loginBtn, registerLink;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usernameLogin = findViewById(R.id.usernameLoginEditText);
        this.passwordLogin = findViewById(R.id.passwordLoginEditText);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.registerLink = findViewById(R.id.registerInLoginBtn);
    }

    private Boolean validateUsername() {
        String username = usernameLogin.getText().toString();

        if (username.isEmpty()) {
            usernameLogin.setError("Řádek nesmí být prázdný");
            return false;
        } else {
            usernameLogin.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = passwordLogin.getText().toString();

        if (password.isEmpty()) {
            passwordLogin.setError("Řádek nesmí být prázdný");
            return false;
        } else {
            passwordLogin.setError(null);
            return true;
        }
    }

    public void openRegister(View v) {
        Intent startRegister = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(startRegister);
    }

    public void login(View v) {
        if (!validateUsername() | !validatePassword()) {
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getReadableDatabase();
        String username = usernameLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        databaseHelper.openDataBase();
        Boolean exist = databaseHelper.checkUser(username, password);
        databaseHelper.close();
        if (exist) {
            Toast.makeText(LoginActivity.this, username + " " + password, Toast.LENGTH_LONG).show();
            Intent login = new Intent(getApplicationContext(), SongsActivity.class);
            startActivity(login);
            usernameLogin.getText().clear();
            passwordLogin.getText().clear();
        } else {
            Toast.makeText(LoginActivity.this, "Nesprávné uživatelské jméno nebo heslo", Toast.LENGTH_LONG).show();
        }
    }
}
