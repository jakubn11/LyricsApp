package com.example.lyricsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lyricsapp.R;
import com.example.lyricsapp.classes.Track;

import java.util.List;

public class CustomListAdapterArtistSongs extends ArrayAdapter<Track>{
    private Context myContext;
    private int resorce;
    private List<Track> trackList;
    private TextView nameSong, albumName;
    private ImageView cover;

    public CustomListAdapterArtistSongs(Context myContext, int resource, List<Track> trackList) {
        super(myContext, resource, trackList);
        this.myContext = myContext;
        this.resorce = resource;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(resorce, null);
        nameSong = view.findViewById(R.id.name_song_list_item);
        albumName = view.findViewById(R.id.author_list_item);
        cover = view.findViewById(R.id.cover_list_item);

        Track track = trackList.get(position);

        nameSong.setText(track.getNazevPisnicky());
        albumName.setText(track.getAlbumName());

        return view;
    }
}
