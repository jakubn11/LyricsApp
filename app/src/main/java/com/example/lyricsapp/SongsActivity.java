//package com.example.lyricsapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
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
//import com.example.lyricsapp.database.DatabaseHelper;
//import com.google.android.material.navigation.NavigationView;
//import com.google.gson.JsonArray;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayInputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class SongsActivity extends DrawerActivity {
//
//    private DatabaseHelper databaseHelper;
//    private ListView TopSongsList;
//    private ArrayList<Track> trackList;
//    private String user, track_name, track_author, track_id;
//    private DrawerLayout mDrawer;
//    private Toolbar toolbar;
//    private NavigationView nvDrawer;
//    private ActionBarDrawerToggle drawerToggle;
//    private Fragment fragment;
//    private CustomListAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        TopSongsList = findViewById(R.id.SongsListView);
//
//        databaseHelper = new DatabaseHelper(this);
//
//
//        RequestQueue queue = Volley.newRequestQueue(SongsActivity.this);
//        String url = "https://api.musixmatch.com/ws/1.1/track.search?format=json&s_track_rating=desc&quorum_factor=0%2C9&page=10&apikey=24a77db4314e8422a65a8d369612e7f1";
//        trackList = new ArrayList<>();
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
//                        track_id = track.getString("track_id");
//                        track_name = track.getString("track_name");
//                        track_author = track.getString("artist_name");
//                        trackList.add(new Track(track_id, track_name, track_author, R.mipmap.ic_launcher));
//                    }
//                    adapter = new CustomListAdapter(SongsActivity.this, R.layout.my_list_item, trackList);
//                    TopSongsList.setAdapter(adapter);
//                    dialog.dismiss();
//
//                    if (trackList.isEmpty()) {
//                        Toast.makeText(SongsActivity.this, "Nejsou zde žádné písničky", Toast.LENGTH_LONG).show();
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
//
//    }
//
//}