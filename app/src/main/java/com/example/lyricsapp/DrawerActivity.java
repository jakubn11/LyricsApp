package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyricsapp.database.DatabaseHelper;
import com.example.lyricsapp.fragments.FavArtistsFragment;
import com.example.lyricsapp.fragments.FavSongsFragment;
import com.example.lyricsapp.fragments.ProfileFragment;
import com.example.lyricsapp.fragments.SearchFragment;
import com.example.lyricsapp.fragments.SettingsFragment;
import com.example.lyricsapp.fragments.SongsFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Objects;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment fragment;
    private String usernameLogin;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usernameLogin = extras.getString("USERNAME_FROM_LOGIN");

        // This will display an Up icon (<-), we will replace it with hamburger later
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        View headerLayout = navigationView.inflateHeaderView(R.layout.header);
        ImageView userImageHeader = headerLayout.findViewById(R.id.image_header);
        TextView userUsernameHeader = headerLayout.findViewById(R.id.username_header);
        TextView userEmailHeader = headerLayout.findViewById(R.id.email_header);

        databaseHelper.openDataBase();
        Cursor data = databaseHelper.getUserHeader(usernameLogin);
        data.moveToFirst();
        String username = data.getString(data.getColumnIndex("prezdivka"));
        String email = data.getString(data.getColumnIndex("email"));
        userID = data.getInt(data.getColumnIndex("id_uzivatel"));
        userUsernameHeader.setText(username);
        userEmailHeader.setText(email);
        databaseHelper.close();

        byte[] blob = data.getBlob(data.getColumnIndex("profilovka"));
        if (blob == null) {
            Log.i("PROFILE PHOTO", "V databázi není pro tento účet profilová fotka");
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            userImageHeader.setImageBitmap(bitmap);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);


        FavSongsFragment favSongsFragment = new FavSongsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", usernameLogin);
        bundle.putInt("USER_ID", userID);
        favSongsFragment.setArguments(bundle);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, favSongsFragment);
        tx.commit();

        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setTitle(navigationView.getMenu().getItem(0).getTitle());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", usernameLogin);
        bundle.putInt("USER_ID", userID);
        switch (menuItem.getItemId()) {
            case R.id.nav_fav_songs:
                fragmentClass = FavSongsFragment.class;
                break;
//                FavSongsFragment favSongsFragment = new FavSongsFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("USERNAME", usernameLogin);
//                bundle.putInt("USER_ID", userID);
//                favSongsFragment.setArguments(bundle);
//                fragmentTransaction.add(R.id.flContent, fragment);
//                fragmentTransaction.commit();
//                break;
            case R.id.nav_fav_artists:
                fragmentClass = FavArtistsFragment.class;
                break;
//                FavArtistsFragment favArtistsFragment = new FavArtistsFragment();
//                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//                Bundle bundle2 = new Bundle();
//                bundle2.putString("USERNAME", usernameLogin);
//                bundle2.putInt("USER_ID", userID);
//                favArtistsFragment.setArguments(bundle2);
//                fragmentTransaction2.add(R.id.flContent, fragment);
//                fragmentTransaction2.commit();
//                break;
            case R.id.nav_all_songs:
                fragmentClass = SongsFragment.class;
                break;
//                SongsFragment songsFragment = new SongsFragment();
//                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                Bundle bundle3 = new Bundle();
//                bundle3.putString("USERNAME", usernameLogin);
//                bundle3.putInt("USER_ID", userID);
//                songsFragment.setArguments(bundle3);
//                fragmentTransaction3.add(R.id.flContent, fragment);
//                fragmentTransaction3.commit();
//                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
//                ProfileFragment profileFragment = new ProfileFragment();
//                FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
//                Bundle bundle4 = new Bundle();
//                bundle4.putString("USERNAME", usernameLogin);
//                bundle4.putInt("USER_ID", userID);
//                profileFragment.setArguments(bundle4);
//                fragmentTransaction4.add(R.id.flContent, fragment);
//                fragmentTransaction4.commit();
//                break;
            case R.id.nav_search:
                fragmentClass = SearchFragment.class;
                break;
//                SearchFragment searchFragment = new SearchFragment();
//                FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
//                Bundle bundle5 = new Bundle();
//                bundle5.putString("USERNAME", usernameLogin);
//                bundle5.putInt("USER_ID", userID);
//                searchFragment.setArguments(bundle5);
//                fragmentTransaction5.add(R.id.flContent, fragment);
//                fragmentTransaction5.commit();
//                break;
            case R.id.nav_logout:
//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setMessage("Prosím připojte se k internetu pro načtení písniček")
//                        .setCancelable(false)
//                        .setPositiveButton("Připojit se", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                            }
//                        }).setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    }
//                });
                break;
            default:
                fragmentClass = FavSongsFragment.class;
        }
        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
//         Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        toolbar.setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private void logout() {
        Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logout);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}