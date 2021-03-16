package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.classes.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class FavouriteSongsActivity extends DrawerActivity {


    private String user;
    private DatabaseHelper databaseHelper;
    private ArrayList<Track> trackList;
    private ListView favouriteSongsList;
    private androidx.appcompat.widget.SearchView searchView;
    private PopupWindow popupWindow;
    private boolean click = true;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_songs);

        favouriteSongsList = findViewById(R.id.FavouriteSongsListView);
        searchView = findViewById(R.id.search_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("DOmů");

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                SongsActivity.this.listAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                SongsActivity.this.adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (toolbar != null) {
//                    toolbar.setVisibility(View.INVISIBLE);
//                    searchView.setMaxWidth(Integer.MAX_VALUE);
//                }
//            }
//        });
//
//        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                if (toolbar != null) {
//                    toolbar.setVisibility(View.VISIBLE);
//                }
//                return false;
//            }
//        });


//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        user = extras.getString("USERNAME_FROM_LOGIN");
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username_header);
//        txtProfileName.setText(user);
//
//        databaseHelper = new DatabaseHelper(this);
//        databaseHelper.openDataBase();
//        Cursor data = databaseHelper.getUserHeader(user);
//        data.moveToFirst();
//        TextView txtProfileEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_header);
//        txtProfileEmail.setText(data.getString(data.getColumnIndex("email")));
//        byte[] blob = data.getBlob(data.getColumnIndex("profilovka"));
//        if (blob == null) {
//            Log.i("PROFILE PHOTO", "V databázi není pro tento účet profilová fotka");
//        } else {
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            ImageView imageProfile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_header);
//            imageProfile.setImageBitmap(bitmap);
//            databaseHelper.close();
//        }

        trackList = new ArrayList<>();
        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getData();
        if (songs.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
        } else {
            while (songs.moveToNext()) {
                String nazevPisnicky = songs.getString(songs.getColumnIndex("nazev_pisnicky"));
                String jmenoAutora = songs.getString(songs.getColumnIndex("jmeno_interpreta"));
                trackList.add(new Track(nazevPisnicky, jmenoAutora, R.mipmap.ic_launcher));
                CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.my_list_item, trackList);
                favouriteSongsList.setAdapter(adapter);
            }
        }
        databaseHelper.close();
    }
}