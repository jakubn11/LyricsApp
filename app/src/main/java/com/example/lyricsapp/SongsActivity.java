package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lyricsapp.database.DatabaseHelper;

import java.util.List;

public class SongsActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        this.listView = findViewById(R.id.ListView1);

        databaseHelper = new DatabaseHelper(this);
        //databaseHelper.copyDataBase("/data/data/com.example.lyricsapp/databases/Lyrics.db");
    }

    /*private List<Uzivatel> getData() {
        databaseHelper.openDataBase();
        List<Uzivatel> list = databaseHelper.getData();
        databaseHelper.close();
        return list;
    }*/

    /*private void addList() {
        this.list = getData();
    }*/
}