package com.example.lyricsapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.LoginActivity;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        checkConnection();

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

    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        try {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                loadSongs();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                loadSongs();
            }
        } catch (Exception e) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Žádné připojení")
                    .setMessage("Jste offline zkontrolujte prosím své internetové připojení")
                    .setPositiveButton("Zkusit znovu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();
                        }
                    }).setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //  Action for 'NO' Button
                    dialog.cancel();
                }
            }).show();
        }
    }

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
