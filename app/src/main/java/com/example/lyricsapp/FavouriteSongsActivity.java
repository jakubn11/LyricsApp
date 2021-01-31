package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class FavouriteSongsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout fav_songs_layout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_songs);

        fav_songs_layout = findViewById(R.id.fav_songs_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle3 = new ActionBarDrawerToggle(this, fav_songs_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        fav_songs_layout.addDrawerListener(toggle3);
        toggle3.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_fav_songs);
    }

    public void onBackPressed() {
        if(fav_songs_layout.isDrawerOpen(GravityCompat.START)) {
            fav_songs_layout.closeDrawer(GravityCompat.START);
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
                startActivity(profile);
                break;
            case R.id.nav_logout:
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                break;
        }
        fav_songs_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}