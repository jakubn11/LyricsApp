package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.classes.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SongDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, author, text;
    private String data, track_name, track_author, lyrics_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        name = findViewById(R.id.name_song);
        author = findViewById(R.id.author_song);
        text = findViewById(R.id.text_song);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(track_name);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("ID_FROM_SONGS");

        RequestQueue queue = Volley.newRequestQueue(SongDetailActivity.this);
        String url = "https://api.musixmatch.com/ws/1.1/track.get?format=json&track_id=" + data + "&apikey=24a77db4314e8422a65a8d369612e7f1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject track = body.getJSONObject("track");
                    track_name = track.getString("track_name");
                    track_author = track.getString("artist_name");

                    name.setText(track_name);
                    author.setText(track_author);
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

        String url2 = "https://api.musixmatch.com/ws/1.1/track.lyrics.get?format=json&track_id=" + data + "&apikey=24a77db4314e8422a65a8d369612e7f1";
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}