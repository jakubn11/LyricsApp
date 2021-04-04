package com.example.lyricsapp.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.R;
import com.example.lyricsapp.adapters.CustomListAdapterArtistSongs;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String data, artist_id, artist_name, track_id, track_name, track_author, album_name;
    private ArrayList<Track> ListTrack;
    private TextView artistName;
    private ListView popularSongs;
    private Menu menu;
    private int userID;
    private DatabaseHelper databaseHelper;
    private MenuItem likeEmpty, likeFull;
    private CustomListAdapterArtistSongs adapter;
    private LinearLayout showSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        toolbar = findViewById(R.id.toolbar);
        artistName = findViewById(R.id.artist_name_text_view);
        popularSongs = findViewById(R.id.popular_songs_list_view);
        showSongs = findViewById(R.id.showSongsArtistDetail);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("ARTIST_ID");
        userID = extras.getInt("USER_ID");
        Log.i("USER ID", String.valueOf(userID));

        loadArtistInfo();
        loadSongListView();

        popularSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String idTrack = track.getId();
                Intent detail = new Intent(getApplicationContext(), SongDetailActivity.class);
                detail.putExtra("SONG_ID", idTrack);
                detail.putExtra("USER_ID", userID);
                startActivity(detail);
            }
        });
    }

    private void loadArtistInfo() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.musixmatch.com/ws/1.1/artist.get?format=json&artist_id=" + data + "&apikey=24a77db4314e8422a65a8d369612e7f1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject artist = body.getJSONObject("artist");
                    artist_id = artist.getString("artist_id");
                    artist_name = artist.getString("artist_name");

                    checkArtist(artist_id, userID);

                    artistName.setText(artist_name);
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

    private void loadSongListView() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.musixmatch.com/ws/1.1/track.search?format=json&f_artist_id=" + data + "&f_has_lyrics=1&s_track_rating=desc&page_size=10&page=1&apikey=24a77db4314e8422a65a8d369612e7f1\n";
        ListTrack = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONArray track_list = body.getJSONArray("track_list");
                    for (int i = 0; i < track_list.length(); i++) {
                        JSONObject track_data = track_list.getJSONObject(i);
                        JSONObject track = track_data.getJSONObject("track");
                        track_id = track.getString("track_id");
                        track_name = track.getString("track_name");
                        album_name = track.getString("album_name");
                        ListTrack.add(new Track(track_id, track_name, R.mipmap.ic_launcher, album_name));
                    }
                    adapter = new CustomListAdapterArtistSongs(getApplicationContext(), R.layout.my_list_item_song, ListTrack);
                    popularSongs.setAdapter(adapter);

                    if (ListTrack.isEmpty()) {
                        showSongs.setVisibility(View.INVISIBLE);
                    }

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

    private void checkArtist(String id, int idUser) {
        databaseHelper.openDataBase();
        Cursor result = databaseHelper.getArtist(id, idUser);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_like_aritst, menu);

        likeEmpty = menu.findItem(R.id.like_artist);
        likeFull = menu.findItem(R.id.like_full_artist);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.like_artist) {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            databaseHelper.openDataBase();
            databaseHelper.insertArtist(data, artist_name, userID);
            databaseHelper.close();
            return true;
        } else if (id == R.id.like_full_artist) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            databaseHelper.openDataBase();
            databaseHelper.deleteFavArtist(data);
            databaseHelper.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}