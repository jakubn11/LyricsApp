package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.classes.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;

import java.util.ArrayList;

public class FavSongDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, author, text;
    private String data, nazevPisnicky, jmenoAutora, textSong;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        name = findViewById(R.id.name_song);
        author = findViewById(R.id.author_song);
        text = findViewById(R.id.text_song);
        toolbar = findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("DATA_FROM_FAV_SONGS");

        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getSong(data);

        while (songs.moveToNext()) {
            nazevPisnicky = songs.getString(songs.getColumnIndex("nazev_pisnicky"));
            jmenoAutora = songs.getString(songs.getColumnIndex("jmeno_interpreta"));
            textSong = songs.getString(songs.getColumnIndex("text"));
        }
        name.setText(nazevPisnicky);
        author.setText(jmenoAutora);
        text.setText(textSong);

        databaseHelper.close();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
