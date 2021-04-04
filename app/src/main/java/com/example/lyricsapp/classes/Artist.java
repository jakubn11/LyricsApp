package com.example.lyricsapp.classes;

public class Artist {
    private String idArtist;
    private String artistName;
    private String timeStamp;

    public Artist(String idArtist, String artistName) {
        this.idArtist = idArtist;
        this.artistName = artistName;
    }

    public Artist(String idArtist, String artistName, String timeStamp) {
        this.idArtist = idArtist;
        this.artistName = artistName;
        this.timeStamp = timeStamp;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setIdArtist(String idArtist) {
        this.idArtist = idArtist;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
