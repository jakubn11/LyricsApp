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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.classes.CustomListAdapter;
import com.example.lyricsapp.classes.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowMoreSongsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Track> searchListTrack;
    ;
    private String track_id, track_name, track_author, data;
    private Toolbar toolbar;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_more_songs);

        listView = findViewById(R.id.ShowMoreSongsListView);
        toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("DATA_FROM_SEARCH");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vyhledávání: " + data);

        showMoreSongs(data);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String idTrack = track.getId();
                Intent detail = new Intent(getApplicationContext(), SongDetailActivity.class);
                detail.putExtra("ID_FROM_SONGS", idTrack);
                startActivity(detail);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showMoreSongs(String text) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.musixmatch.com/ws/1.1/track.search?format=json&q_artist=" + text + "&f_has_lyrics=1&s_track_rating=desc&quorum_factor=1&apikey=24a77db4314e8422a65a8d369612e7f1";
        searchListTrack = new ArrayList<>();

//        ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), null, "Prosím počkejte");
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
                        track_author = track.getString("artist_name");
                        searchListTrack.add(new Track(track_id, track_name, track_author, R.mipmap.ic_launcher));

                    }
                    adapter = new CustomListAdapter(getApplicationContext(), R.layout.my_list_item, searchListTrack);
                    listView.setAdapter(adapter);
//                    dialog.dismiss();

                    if (searchListTrack.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
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
}