//package com.example.lyricsapp;
//
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.lyricsapp.classes.CustomListAdapter;
//import com.example.lyricsapp.classes.Track;
//import com.google.android.material.navigation.NavigationView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchActivity extends AppCompatActivity {
//
//    private EditText searchBar;
//    private ListView searchListViewTrack, searchListViewArtist;
//    private ArrayList<Track> searchListTrack, searchListArtist;
//    private String track_name, track_author;
//    private DrawerLayout search_layout;
//    private NavigationView navigationView;
//    private androidx.appcompat.widget.Toolbar toolbar, closeButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        toolbar = findViewById(R.id.toolbar);
//        search_layout = findViewById(R.id.search_layout);
//        navigationView = findViewById(R.id.nav_view);
//        searchBar = findViewById(R.id.search_edit_text);
//        closeButton = findViewById(R.id.close_button);
//
//        toolbar.setTitle("");
//
//        setSupportActionBar(toolbar);
//
//
//        searchListViewTrack = findViewById(R.id.search_list_view_track);
//        searchListViewArtist = findViewById(R.id.search_list_view_artist);
//        searchListViewTrack = findViewById(R.id.search_list_view_track);
//        searchListViewArtist = findViewById(R.id.search_list_view_artist);
//
//
//        searchBar.requestFocus();
//        searchBar.setFocusableInTouchMode(true);
//        closeButton.setVisibility(View.INVISIBLE);
//
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (searchBar.getText().toString().trim().length() > 0) {
//                    closeButton.setVisibility(View.VISIBLE);
//                } else {
//                    closeButton.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    String text = searchBar.getText().toString();
//                    searchSong(text.replace(" ", "%"));
//                    searchArtist(text.replace(" ", "%"));
//                }
//                return false;
//            }
//        });
//    }
//
//    public void searchSong(String text) {
//        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
//        String url = "https://api.musixmatch.com/ws/1.1/track.search?format=json&q_artist=" + text + "&s_track_rating=desc&quorum_factor=1&page_size=4&page=1&apikey=24a77db4314e8422a65a8d369612e7f1";
//        searchListTrack = new ArrayList<>();
//
//        ProgressDialog dialog = ProgressDialog.show(this, null, "Prosím počkejte");
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject message = response.getJSONObject("message");
//                    JSONObject body = message.getJSONObject("body");
//                    JSONArray track_list = body.getJSONArray("track_list");
//                    for (int i = 0; i < track_list.length(); i++) {
//                        JSONObject track_data = track_list.getJSONObject(i);
//                        JSONObject track = track_data.getJSONObject("track");
//                        track_name = track.getString("track_name");
//                        track_author = track.getString("artist_name");
//                        searchListTrack.add(new Track(track_name, track_author, R.mipmap.ic_launcher));
//
//                    }
//                    CustomListAdapter adapter = new CustomListAdapter(SearchActivity.this, R.layout.my_list_item, searchListTrack);
//                    searchListViewTrack.setAdapter(adapter);
//                    dialog.dismiss();
//
//                    if (searchListTrack.isEmpty()) {
//                        Toast.makeText(SearchActivity.this, "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("LOG_TAG", e.toString());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("ERROR", error.toString());
//            }
//        });
//        queue.add(request);
//    }
//
//    public void searchArtist(String text) {
//        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
//        String url = "https://api.musixmatch.com/ws/1.1/artist.search?format=json&q_artist=" + text + "%20Dragons&page=1&page_size=4&apikey=24a77db4314e8422a65a8d369612e7f1\n";
//        searchListArtist = new ArrayList<>();
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject message = response.getJSONObject("message");
//                    JSONObject body = message.getJSONObject("body");
//                    JSONArray track_list = body.getJSONArray("track_list");
//                    for (int i = 0; i < track_list.length(); i++) {
//                        JSONObject track_data = track_list.getJSONObject(i);
//                        JSONObject track = track_data.getJSONObject("track");
//                        track_name = track.getString("track_name");
//                        track_author = track.getString("artist_name");
//                        searchListArtist.add(new Track(track_name, track_author, R.mipmap.ic_launcher));
//
//                    }
//                    CustomListAdapter adapter = new CustomListAdapter(SearchActivity.this, R.layout.my_list_item, searchListArtist);
//                    searchListViewArtist.setAdapter(adapter);
//
//                    if (searchListArtist.isEmpty()) {
//                        Toast.makeText(SearchActivity.this, "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("LOG_TAG", e.toString());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("ERROR", error.toString());
//            }
//        });
//        queue.add(request);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    public void cancelSearch(View view) {
//        searchBar.getText().clear();
//    }
//}