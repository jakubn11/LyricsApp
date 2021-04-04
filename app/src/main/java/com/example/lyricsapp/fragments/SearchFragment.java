package com.example.lyricsapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyricsapp.R;
import com.example.lyricsapp.ShowMoreArtistsActivity;
import com.example.lyricsapp.ShowMoreSongsActivity;
import com.example.lyricsapp.classes.Artist;
import com.example.lyricsapp.adapters.CustomListAdapter;
import com.example.lyricsapp.adapters.CustomListAdapterArtist;
import com.example.lyricsapp.classes.Track;
import com.example.lyricsapp.details.ArtistDetailActivity;
import com.example.lyricsapp.details.SongDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class SearchFragment extends Fragment {

    private ListView searchListViewTrack, searchListViewArtist;
    private ArrayList<Track> searchListTrack;
    private ArrayList<Artist> searchListArtist;
    private String track_id, track_name, track_author, data, artist_id, artist_name, textChangedQuery;
    private SearchView searchView;
    private TextView textSongsListView, textArtistListView, showMoreSongs, showMoreArtists;
    private Timer timer;
    private int userID;
    private LinearLayout searchLayout;
    private CustomListAdapter adapter;
    private CustomListAdapterArtist adapterArtist;
    private SearchView scrollViewSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        searchListViewTrack = view.findViewById(R.id.search_list_view_track);
        showMoreSongs = view.findViewById(R.id.showMoreSongs);
        showMoreArtists = view.findViewById(R.id.showMoreArtist);
        searchListViewArtist = view.findViewById(R.id.search_list_view_artist);
        searchLayout = view.findViewById(R.id.searchLinearLayout);
        textSongsListView = view.findViewById(R.id.textSongsListView);
        textArtistListView = view.findViewById(R.id.textArtistListView);
        scrollViewSearch = view.findViewById(R.id.scrollViewSearch);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        userID = bundle.getInt("USER_ID");

        searchLayout.setVisibility(View.INVISIBLE);

        showMoreSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowMoreSongsActivity.class);
                intent.putExtra("DATA_FROM_SEARCH", searchView.getQuery().toString());
                startActivity(intent);
            }
        });

        showMoreArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowMoreArtistsActivity.class);
                intent.putExtra("DATA_FROM_SEARCH", searchView.getQuery().toString());
                startActivity(intent);
            }
        });

        searchListViewTrack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = adapter.getItem(position);
                String idTrack = track.getId();
                Intent detail = new Intent(getContext(), SongDetailActivity.class);
                detail.putExtra("SONG_ID", idTrack);
                detail.putExtra("USER_ID", userID);
                Log.i("SONG_ID_SEARCH", idTrack);
                startActivity(detail);
                searchView.clearFocus();
            }
        });

        searchListViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = adapterArtist.getItem(position);
                String idArtist = artist.getIdArtist();
                Intent detail = new Intent(getContext(), ArtistDetailActivity.class);
                detail.putExtra("ARTIST_ID", idArtist);
                detail.putExtra("USER_ID", userID);
                startActivity(detail);
                searchView.clearFocus();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchLayout.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.contains(" ")) {
                    query = query.replace(" ", "%");
                }

                searchSong(query);
                searchArtist(query);
                searchLayout.setVisibility(View.VISIBLE);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        searchView.setQueryHint("Hledat");
        searchView.setIconifiedByDefault(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void searchSong(String song) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.musixmatch.com/ws/1.1/track.search?format=json&q_track=" + song + "&f_has_lyrics=1&s_track_rating=desc&quorum_factor=1 &page_size=4&page=1&apikey=24a77db4314e8422a65a8d369612e7f1";
        searchListTrack = new ArrayList<>();
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
                        searchListTrack.add(new Track(track_id, track_name, track_author, R.mipmap.ic_launcher));
                    }
                    adapter = new CustomListAdapter(getContext(), R.layout.my_list_item_song, searchListTrack);
                    searchListViewTrack.setAdapter(adapter);
                    ListUtils.setDynamicHeight(searchListViewTrack);
                    dialog.dismiss();

                    if (searchListTrack.isEmpty()) {
                        searchLayout.setVisibility(View.INVISIBLE);
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

    public void searchArtist(String artist) {
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        String url2 = "https://api.musixmatch.com/ws/1.1/artist.search?format=json&q_artist=" + artist + "&page=1&page_size=4&apikey=24a77db4314e8422a65a8d369612e7f1";
        searchListArtist = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(getContext(), null, "Prosím počkejte");
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
                    adapterArtist = new CustomListAdapterArtist(getContext(), R.layout.my_list_item_artist, searchListArtist);
                    searchListViewArtist.setAdapter(adapterArtist);
                    ListUtils.setDynamicHeight(searchListViewArtist);
                    dialog.dismiss();

                    if (searchListArtist.isEmpty()) {
                        searchLayout.setVisibility(View.INVISIBLE);
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
        queue2.add(request2);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
