package com.example.lyricsapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.lyricsapp.details.SongDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private ListView topSongsList;
    private ArrayList<Track> trackList;
    private String track_name, track_author, track_id;
    private int userID;
    private CustomListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_songs, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        assert getArguments() != null;
        userID = getArguments().getInt("USER_ID");

        trackList = new ArrayList<>();
        topSongsList = view.findViewById(R.id.SongsListView);

        setHasOptionsMenu(true);

        loadSongs();

        topSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String idTrack = track.getId();
                Intent detail = new Intent(getContext(), SongDetailActivity.class);
                detail.putExtra("SONG_ID", idTrack);
                detail.putExtra("USER_ID", userID);
                startActivity(detail);
            }
        });

        return view;
    }

//    private void showCustomDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage("Prosím připojte se k internetu pro načtení písniček")
//                .setCancelable(false)
//                .setPositiveButton("Připojit se", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
//        }).setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getContext(), LoginActivity.class));
//            }
//        });
//    }

//    private void checkNetworkConnection() {
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context);
//            NetworkInfo networkInfo = null;
//            if (connectivityManager != null) {
//                networkInfo = connectivityManager.getActiveNetworkInfo();
//            }
//            return networkInfo != null && networkInfo.isConnected();
//        } catch (NullPointerException e) {
//            return false;
//        }
//    }

    private void loadSongs() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.musixmatch.com/ws/1.1/chart.tracks.get?format=json&page=1&page_size=10&country=XW%20&f_has_lyrics=1&apikey=24a77db4314e8422a65a8d369612e7f1";
        ProgressDialog dialog = ProgressDialog.show(getContext(), null, "Prosím počkejte");
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
                        trackList.add(new Track(track_id, track_name, track_author, R.mipmap.ic_launcher));
                    }
                    adapter = new CustomListAdapter(getContext(), R.layout.my_list_item_song, trackList);
                    topSongsList.setAdapter(adapter);
                    dialog.dismiss();

                    if (trackList.isEmpty()) {
                        Toast.makeText(getContext(), "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
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
