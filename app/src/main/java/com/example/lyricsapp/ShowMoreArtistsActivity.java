package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.classes.Artist;
import com.example.lyricsapp.adapters.CustomListAdapterArtist;
import com.example.lyricsapp.details.ArtistDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ShowMoreArtistsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Artist> searchListArtist;
    private String artist_id;
    private String artist_name;
    private CustomListAdapterArtist adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_more);

        listView = findViewById(R.id.ShowMoreSongsListView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String data = extras.getString("DATA_FROM_SEARCH");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vyhledávání: " + data);

        showMoreArtists(data);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = adapter.getItem(position);
                String idTrack = artist.getIdArtist();
                Intent detail = new Intent(getApplicationContext(), ArtistDetailActivity.class);
                detail.putExtra("ARTIST_ID", idTrack);
                startActivity(detail);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showMoreArtists(String artist) {
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        String url2 = "https://api.musixmatch.com/ws/1.1/artist.search?format=json&q_artist=" + artist + "&page=1&page_size=10&apikey=24a77db4314e8422a65a8d369612e7f1";
        searchListArtist = new ArrayList<>();
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONArray artist_list = body.getJSONArray("artist_list");
                    for (int i = 0; i < artist_list.length(); i++) {
                        JSONObject track_data = artist_list.getJSONObject(i);
                        JSONObject artist = track_data.getJSONObject("artist");
                        artist_id = artist.getString("artist_id");
                        artist_name = artist.getString("artist_name");
                        searchListArtist.add(new Artist(artist_id, artist_name));
                    }
                    adapter = new CustomListAdapterArtist(getApplicationContext(), R.layout.my_list_item_artist, searchListArtist);
                    listView.setAdapter(adapter);

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
        queue2.add(request2);
    }
}