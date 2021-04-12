package com.example.lyricsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lyricsapp.R;
import com.example.lyricsapp.classes.Artist;
import com.example.lyricsapp.adapters.CustomListAdapterArtist;
import com.example.lyricsapp.database.DatabaseHelper;
import com.example.lyricsapp.details.ArtistDetailActivity;
import com.example.lyricsapp.details.FavArtistDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavArtistsFragment extends Fragment {

    private int userID;
    private DatabaseHelper databaseHelper;
    private ArrayList<Artist> artistList;
    private ListView favouriteArtistList;
    private TextView nothigHere;
    private final boolean click = true;
    private CustomListAdapterArtist adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite_artists, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        artistList = new ArrayList<>();
        favouriteArtistList = view.findViewById(R.id.favouriteArtistsListView);
        nothigHere = view.findViewById(R.id.noArtistsHere);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        userID = bundle.getInt("USER_ID");

        loadFavArtists();
        displayArtistsInReverseOrder();
        reverseArtists();

        favouriteArtistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = adapter.getItem(position);
                String idArtist = artist.getIdArtist();
                Intent detail = new Intent(getContext(), FavArtistDetailActivity.class);
                detail.putExtra("ARTIST_ID", idArtist);
                detail.putExtra("USER_ID", userID);
                startActivity(detail);
            }
        });

        return view;
    }

    private void sortArtists() {
        Collections.sort(artistList, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getArtistName().compareTo(o2.getArtistName());
            }
        });
//        adapter.notifyDataSetChanged();
    }

    private void reverseArtists() {
        Collections.reverse(artistList);
//        adapter.notifyDataSetChanged();
    }

    private void displayArtistsInReverseOrder() {
        Collections.sort(artistList, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getTimeStamp().compareTo(o2.getTimeStamp());
            }
        });
    }

    private void loadFavArtists() {
        artistList = new ArrayList<>();
        databaseHelper.openDataBase();
        Cursor songs = databaseHelper.getFavouriteArtists(userID);
        if (songs.getCount() == 0) {
            nothigHere.setVisibility(View.VISIBLE);
        } else {
            while (songs.moveToNext()) {
                String id_autora = songs.getString(songs.getColumnIndex("id_autora"));
                String jmenoAutora = songs.getString(songs.getColumnIndex("jmeno_autora"));
                String timeStamp = songs.getString(songs.getColumnIndex("timeStamp"));
                artistList.add(new Artist(id_autora, jmenoAutora, timeStamp));
                adapter = new CustomListAdapterArtist(getContext(), R.layout.my_list_item_artist, artistList);
                favouriteArtistList.setAdapter(adapter);
                nothigHere.setVisibility(View.INVISIBLE);
            }
            databaseHelper.close();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        MenuItem sortSongsABC = menu.findItem(R.id.sortSongABC);
        MenuItem sortSongsDESC = menu.findItem(R.id.sortSongDESC);

        sortSongsABC.setVisible(false);
        sortSongsDESC.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortArtistDESC:
                loadFavArtists();
                sortArtists();
                reverseArtists();
                return true;
            case R.id.sortArtistABC:
                loadFavArtists();
                sortArtists();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
