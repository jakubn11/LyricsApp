package com.example.lyricsapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lyricsapp.R;

import java.util.List;

public class CustomListAdapterArtist extends ArrayAdapter<Artist> {
    private Context myContext;
    private int resorce;
    private List<Artist> artistList;
    private TextView artistName, authorName;

    public CustomListAdapterArtist(Context myContext, int resource, List<Artist> artistList) {
        super(myContext, resource, artistList);
        this.myContext = myContext;
        this.resorce = resource;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(resorce, null);
        artistName = view.findViewById(R.id.name_artist_list_item);


        Artist artist = artistList.get(position);

        artistName.setText(artist.getArtist_name());

        return view;
    }
}
