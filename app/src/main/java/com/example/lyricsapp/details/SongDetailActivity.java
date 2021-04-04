package com.example.lyricsapp.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.R;
import com.example.lyricsapp.database.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SongDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, author, text;
    private String idSong, track_id, track_name, artist_name, artist_id, lyrics_body, fav_songs;
    private DatabaseHelper databaseHelper;
    private MenuItem likeEmpty, likeFull, showArtist;
    private Menu menu;
    private int userID;

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
        fav_songs = extras.getString("FAV_SONGS_FRAGMENT");
        Log.i("USER ID", String.valueOf(userID));
        Log.i("SONG_ID", String.valueOf(idSong));

        loadSongInfo();
        loadLyrics();
    }

    private void loadSongInfo() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.musixmatch.com/ws/1.1/track.get?format=json&track_id=" + idSong + "&apikey=24a77db4314e8422a65a8d369612e7f1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject track = body.getJSONObject("track");
                    track_id = track.getString("track_id");
                    track_name = track.getString("track_name");
                    artist_name = track.getString("artist_name");
                    artist_id = track.getString("artist_id");

                    checkSong(track_id, userID);

                    name.setText(track_name);
                    author.setText(artist_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LOG_TAG", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR", error.toString());
            }
        });
        queue.add(request);
    }

    private void loadLyrics() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url2 = "https://api.musixmatch.com/ws/1.1/track.lyrics.get?format=json&track_id=" + idSong + "&apikey=24a77db4314e8422a65a8d369612e7f1";
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject lyrics = body.getJSONObject("lyrics");
                    lyrics_body = lyrics.getString("lyrics_body");


                    text.setText(lyrics_body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LOG_TAG", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR", error.toString());
            }
        });
        queue.add(request2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_like_song, menu);

        likeEmpty = menu.findItem(R.id.like_song);
        likeFull = menu.findItem(R.id.like_full_song);
        showArtist = menu.findItem(R.id.show_artist);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.like_song) {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            databaseHelper.openDataBase();
                databaseHelper.insertSong(idSong, track_name, artist_name, userID, lyrics_body);
            databaseHelper.close();
            return true;
        } else if (id == R.id.like_full_song) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            databaseHelper.openDataBase();
            databaseHelper.deleteFavSong(idSong);
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

    private void checkSong(String songId, int idUser) {
        databaseHelper.openDataBase();
        Cursor result = databaseHelper.getSong(songId, idUser);

        if (result.getCount() == 0) {
            likeEmpty.setVisible(true);
            likeFull.setVisible(false);
        } else {
            likeEmpty.setVisible(false);
            likeFull.setVisible(true);
        }
        databaseHelper.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        String text = "fav_songs";
        if(fav_songs==text) {
            Log.i("JMENO", "nameFramgnt");
        }
        return true;
    }
}