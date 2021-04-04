package com.example.lyricsapp.classes;

import android.graphics.Bitmap;

public class Track {
    private String nazevPisnicky;
    private String jmenoUmelce;
    private int cover;
    private String track_id;
    private String text;
    private String albumName;
    private String timeStamp;

    public Track(String nazevPisnicky, String jmenoUmelce, int cover) {
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.cover = cover;
    }

    public Track(String track_id, String nazevPisnicky, String jmenoUmelce, int cover, String timeStamp) {
        this.track_id = track_id;
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.cover = cover;
        this.timeStamp = timeStamp;
    }

    public Track(String track_id, String nazevPisnicky, String jmenoUmelce, int cover) {
        this.track_id = track_id;
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.cover = cover;
    }

    public Track(String track_id, String nazevPisnicky, String jmenoUmelce, String text) {
        this.track_id = track_id;
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.text = text;
    }

    public Track(String track_id, String nazevPisnicky, int cover, String albumName) {
        this.track_id = track_id;
        this.nazevPisnicky = nazevPisnicky;
        this.cover = cover;
        this.albumName = albumName;
    }

    public Track() {

    }

    public int getCover() {
        return cover;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNazevPisnicky() {
        return nazevPisnicky;
    }

    public String getJmenoUmelce() {
        return jmenoUmelce;
    }

    public String getId() {
        return track_id;
    }

    public void setNazevPisnicky(String nazevPisnicky) {
        this.nazevPisnicky = nazevPisnicky;
    }

    public void setJmenoUmelce(String jmenoUmelce) {
        this.jmenoUmelce = jmenoUmelce;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }
}
