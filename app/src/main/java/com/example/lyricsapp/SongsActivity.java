package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lyricsapp.classes.Uzivatel;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.List;

public class SongsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_all_songs);
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
//                Intent intent = getIntent();
//                Bundle extras = intent.getExtras();
//                String username = extras.getString("USERNAME");
//                profile.putExtra("USERNAME2", username);
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