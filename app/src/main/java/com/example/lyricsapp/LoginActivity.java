package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.lyricsapp.database.DatabaseHelper;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLogin, passwordLogin;
    private Button loginButton, registerLink;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogin = findViewById(R.id.usernameLoginEditText);
        passwordLogin = findViewById(R.id.passwordLoginEditText);
        loginButton = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerInLoginBtn);
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
//            Toast.makeText(LoginActivity.this, username + " " + password, Toast.LENGTH_LONG).show();
            Intent login = new Intent(LoginActivity.this, SongsActivity.class);
            startActivity(login);
            usernameLogin.getText().clear();
            passwordLogin.getText().clear();
        } else {
            Toast.makeText(LoginActivity.this, "Nesprávné uživatelské jméno nebo heslo", Toast.LENGTH_LONG).show();
        }
    }
}
