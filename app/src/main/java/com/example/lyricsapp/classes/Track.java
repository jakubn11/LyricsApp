package com.example.lyricsapp.classes;

public class Track {
    private String nazevPisnicky;
    private String jmenoUmelce;
    private int cover;
    private String track_id;

    public Track(String nazevPisnicky, String jmenoUmelce, int cover) {
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.cover = cover;
    }

    public Track(String track_id, String nazevPisnicky, String jmenoUmelce, int cover) {
        this.track_id = track_id;
        this.nazevPisnicky = nazevPisnicky;
        this.jmenoUmelce = jmenoUmelce;
        this.cover = cover;
    }

    public String getNazevPisnicky() {
        return nazevPisnicky;
    }

    public String getJmenoUmelce() {
        return jmenoUmelce;
    }

    public int getCover() {
        return cover;
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

    public void setCover(int cover) {
        this.cover = cover;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }
}
