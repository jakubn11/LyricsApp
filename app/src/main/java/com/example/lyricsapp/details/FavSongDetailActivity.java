package com.example.lyricsapp.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.R;
import com.example.lyricsapp.adapters.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;
import com.example.lyricsapp.fragments.FavSongsFragment;
import com.example.lyricsapp.fragments.SearchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavSongDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, author, text;
    private String idSong, track_id, track_name, artist_name, artist_id, lyrics_body, fav_songs;
    private DatabaseHelper databaseHelper;
    private MenuItem likeEmpty, likeFull, showArtist;
    private Menu menu;
    private int userID;
    private Cursor result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        name = findViewById(R.id.name_song);
        author = findViewById(R.id.author_song);
        text = findViewById(R.id.text_song);
        toolbar = findViewById(R.id.toolbar);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        idSong = extras.getString("SONG_ID");
        userID = extras.getInt("USER_ID");

        loadSongInfo();
    }

    private void loadSongInfo() {
        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getFavSongDetail(idSong);
        while (songs.moveToNext()) {
            track_id = songs.getString(songs.getColumnIndex("id_pisnicky"));
            track_name = songs.getString(songs.getColumnIndex("nazev_pisnicky"));
            artist_name = songs.getString(songs.getColumnIndex("jmeno_interpreta"));
            artist_id = songs.getString(songs.getColumnIndex("id_interpreta"));
            lyrics_body = songs.getString(songs.getColumnIndex("text"));

            name.setText(track_name);
            author.setText(artist_name);
            text.setText(lyrics_body);
        }
        databaseHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_like_song, menu);

        likeEmpty = menu.findItem(R.id.like_song);
        likeFull = menu.findItem(R.id.like_full_song);
        showArtist = menu.findItem(R.id.show_artist);
        this.menu = menu;

        checkSong();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.like_song) {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            databaseHelper.openDataBase();
            databaseHelper.insertSong(track_id, track_name, artist_name, artist_id, userID, lyrics_body);
            databaseHelper.close();
            return true;
        } else if (id == R.id.like_full_song) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            databaseHelper.openDataBase();
            databaseHelper.deleteFavSong(track_id, userID);
            databaseHelper.close();
            return true;
        } else if (id == R.id.show_artist) {
            Intent app = new Intent(getApplicationContext(), ArtistDetailActivity.class);
            app.putExtra("ARTIST_ID", artist_id);
            app.putExtra("USER_ID", userID);
            startActivity(app);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkSong() {
        databaseHelper.openDataBase();
        result = databaseHelper.getSong(idSong, userID);

        if (result.getCount() == 0) {
            likeEmpty.setVisible(true);
            likeFull.setVisible(false);
        } else {
            likeEmpty.setVisible(false);
            likeFull.setVisible(true);
        }
        databaseHelper.close();
    }

//    @Override
//    public void onBackPressed() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.flContent, fragment);
//        fragmentTransaction.addToBackStack(fragment.toString());
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.commit();
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}