package com.example.lyricsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.lyricsapp.R;
import com.example.lyricsapp.adapters.CustomListAdapter;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.database.DatabaseHelper;
import com.example.lyricsapp.details.FavSongDetailActivity;
import com.example.lyricsapp.details.SongDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavSongsFragment extends Fragment {

    private int userID;
    private DatabaseHelper databaseHelper;
    private ArrayList<Track> trackList;
    private ListView favouriteSongsList;
    private TextView nothingHere;
    private final boolean click = true;
    private CustomListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite_songs, container, false);

        favouriteSongsList = view.findViewById(R.id.favouriteSongsListView);
        nothingHere = view.findViewById(R.id.noSongsHere);

        databaseHelper = new DatabaseHelper(getContext());

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        userID = bundle.getInt("USER_ID");

        loadFavSongs();
        displaySongsInReverseOrder();
        reverseSongs();

        favouriteSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String idSong = track.getId();
                Intent detail = new Intent(getContext(), FavSongDetailActivity.class);
                detail.putExtra("SONG_ID", idSong);
                detail.putExtra("USER_ID", userID);
                detail.putExtra("FAV_SONGS_FRAGMENT", "fav_songs");
                startActivity(detail);
            }
        });

        return view;
    }

    private void sortSongs() {
        Collections.sort(trackList, new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                return o1.getNazevPisnicky().compareTo(o2.getNazevPisnicky());
            }
        });
    }

    private void reverseSongs() {
        Collections.reverse(trackList);
    }

    private void displaySongsInReverseOrder() {
        Collections.sort(trackList, new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                return o1.getTimeStamp().compareTo(o2.getTimeStamp());
            }
        });
    }

    private void loadFavSongs() {
        trackList = new ArrayList<>();
        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getFavouriteSongs(userID);
        if (songs.getCount() == 0) {
            nothingHere.setVisibility(View.VISIBLE);
        } else {
            while (songs.moveToNext()) {
                String track_id = songs.getString(songs.getColumnIndex("id_pisnicky"));
                String nazevPisnicky = songs.getString(songs.getColumnIndex("nazev_pisnicky"));
                String jmenoAutora = songs.getString(songs.getColumnIndex("jmeno_interpreta"));
                String timeStamp = songs.getString(songs.getColumnIndex("timeStamp"));
                trackList.add(new Track(track_id, nazevPisnicky, jmenoAutora, R.mipmap.ic_launcher, timeStamp));
                adapter = new CustomListAdapter(getContext(), R.layout.my_list_item_song, trackList);
                favouriteSongsList.setAdapter(adapter);
                nothingHere.setVisibility(View.INVISIBLE);
            }
            databaseHelper.close();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        MenuItem sortArtistABC = menu.findItem(R.id.sortArtistABC);
        MenuItem sortArtistDESC = menu.findItem(R.id.sortArtistDESC);

        sortArtistABC.setVisible(false);
        sortArtistDESC.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortSongDESC:
                loadFavSongs();
                sortSongs();
                reverseSongs();
                return true;
            case R.id.sortSongABC:
                loadFavSongs();
                sortSongs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
