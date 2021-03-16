package com.example.lyricsapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lyricsapp.classes.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FavSongsFragment extends Fragment {
    private String nazevPisnicky, jmenoAutora;
    private DatabaseHelper databaseHelper;
    private ArrayList<Track> trackList;
    private ListView favouriteSongsList;
    private boolean click = true;
    private ImageView dots;
    private CustomListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite_songs, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        trackList = new ArrayList<>();
        favouriteSongsList = view.findViewById(R.id.FavouriteSongsListView);
        dots = view.findViewById(R.id.dots_item);

        loadFavSongs();

        favouriteSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String jmeno = track.getNazevPisnicky();
                Intent detail = new Intent(getContext(), FavSongDetailActivity.class);
                detail.putExtra("DATA_FROM_FAV_SONGS", jmeno);
                startActivity(detail);
            }
        });

        return view;
    }

    private void loadFavSongs() {
        trackList = new ArrayList<>();
        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getData();
        if (songs.getCount() == 0) {
            Toast.makeText(getContext(), "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
        } else {
            while (songs.moveToNext()) {
                nazevPisnicky = songs.getString(songs.getColumnIndex("nazev_pisnicky"));
                jmenoAutora = songs.getString(songs.getColumnIndex("jmeno_interpreta"));
                trackList.add(new Track(nazevPisnicky, jmenoAutora, R.mipmap.ic_launcher));
                adapter = new CustomListAdapter(getContext(), R.layout.my_list_item, trackList);
                favouriteSongsList.setAdapter(adapter);
            }
            databaseHelper.close();
        }
    }
}
