package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextInputLayout usernameLogin, passwordLogin;
    private Button loginButton, registerLink;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogin = findViewById(R.id.usernameLoginEditText);
        passwordLogin = findViewById(R.id.passwordLoginEditText);
        loginButton = findViewById(R.id.loginBtn);
//        registerLink = findViewById(R.id.registerInLoginBtn);

    }


    private Boolean validateUsername() {
        String username = usernameLogin.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            usernameLogin.setError("Řádek nesmí být prázdný");
            return false;
        } else {
            usernameLogin.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = passwordLogin.getEditText().getText().toString().trim();

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
        String username = usernameLogin.getEditText().getText().toString().trim();
        String password = passwordLogin.getEditText().getText().toString().trim();
        databaseHelper.openDataBase();
        Boolean exist = databaseHelper.checkUser(username, password);
        databaseHelper.close();

        if (exist) {
            passwordLogin.clearFocus();
            usernameLogin.clearFocus();
            Intent app = new Intent(LoginActivity.this, DrawerActivity.class);
            app.putExtra("USERNAME_FROM_LOGIN", username);
            startActivity(app);
            usernameLogin.getEditText().getText().clear();
            passwordLogin.getEditText().getText().clear();
            passwordLogin.setError(null);
        } else {
            passwordLogin.setError("Nesprávné uživatelské jméno nebo heslo");

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
