package com.example.lyricsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.lyricsapp.classes.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "Lyrics.db";
    private static String DB_PATH;
    public final Context context;
    private SQLiteDatabase myDataBase = null;
    File dbFile;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        dbFile = new File(DB_PATH + DATABASE_NAME);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (!dbFile.exists()) {
            SQLiteDatabase db = super.getWritableDatabase();
            copyDataBase(db.getPath());
        }
        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (!dbFile.exists()) {
            SQLiteDatabase db = super.getReadableDatabase();
            copyDataBase(db.getPath());
        }
        return super.getReadableDatabase();
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void copyDataBase(String DB_PATH) {
        try {
            InputStream assestDB = context.getAssets().open("databases/" + DATABASE_NAME);
            OutputStream appDB = new FileOutputStream(DB_PATH, false);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = assestDB.read(buffer)) > 0) {
                appDB.write(buffer, 0, length);
            }
            appDB.flush();
            appDB.close();
            assestDB.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean openDataBase() {
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public Cursor getFavouriteSongs(int idUser) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibene_pisnicky WHERE TRIM(id_uzivatel) = '"+idUser+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getFavouriteArtists(int idUser) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibeni_autori WHERE TRIM(id_uzivatel) = '"+idUser+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getUserHeader(String username) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM uzivatel WHERE TRIM(prezdivka) = '"+username.trim()+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getUsersEmails() {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT email FROM uzivatel";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getUsersNames() {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT prezdivka FROM uzivatel";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getArtist(String idArtist, int idUser) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibeni_autori WHERE TRIM(id_autora) = '"+idArtist.trim()+"' AND TRIM(id_uzivatel) = '"+idUser+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getSong (String idSong, int idUser) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibene_pisnicky WHERE TRIM(id_pisnicky) = '"+idSong.trim()+"' AND TRIM(id_uzivatel) = '"+idUser+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getFavSongDetail (String idSong) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibene_pisnicky WHERE TRIM(id_pisnicky) = '"+idSong.trim()+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getFavArtistDetail (String idArtist) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibeni_autori WHERE TRIM(id_autora) = '"+idArtist.trim()+"'";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Boolean checkUser(String username, String password) {
        String[] columns = {"prezdivka"};
        String selection = "prezdivka=? and heslo=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = myDataBase.query("uzivatel", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();

        if (count > 0) {
            return true;
        } else {
            Log.d("TAG", "V databázi nejsou žádní uživatelé");
            return false;
        }
    }

    public void insertUserWithProfileImage(User user) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("prezdivka", user.getPrezdivka());
        values.put("email", user.getEmail());
        values.put("profilovka", user.getProfilovka());
        values.put("heslo", user.getHeslo());
        values.put("timeStamp", formatter.format(date));
        myDataBase.insert("uzivatel", null, values);
    }

    public void insertUserWithoutProfileImage(User user) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("prezdivka", user.getPrezdivka());
        values.put("email", user.getEmail());
        values.put("heslo", user.getHeslo());
        values.put("timeStamp", formatter.format(date));
        myDataBase.insert("uzivatel", null, values);
    }

    public void insertSong(String id_track, String name_song, String artist_name, String artist_id, int id_user, String text) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("id_pisnicky", id_track);
        values.put("nazev_pisnicky", name_song);
        values.put("jmeno_interpreta", artist_name);
        values.put("id_interpreta", artist_id);
        values.put("id_uzivatel", id_user);
        values.put("text", text);
        values.put("timeStamp", formatter.format(date));
        myDataBase.insert("oblibene_pisnicky", null, values);
    }

    public void deleteFavSong(String id_track, int id_user) {
        myDataBase.delete("oblibene_pisnicky", "id_pisnicky = ? AND id_uzivatel = ?", new String[]{id_track, String.valueOf(id_user)});
    }

    public void deleteFavArtist(String id_artist, int id_user) {
        myDataBase.delete("oblibeni_autori", "id_autora = ? AND id_uzivatel = ?", new String[]{id_artist, String.valueOf(id_user)});
    }

    public void insertArtist(String id_artist, String artist_name, int id_user) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("id_autora", id_artist);
        values.put("jmeno_autora", artist_name);
        values.put("id_uzivatel", id_user);
        values.put("timeStamp", formatter.format(date));
        myDataBase.insert("oblibeni_autori", null, values);
    }

    public void updateUserWithProfileImage(String prezdivka, User newUser) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("prezdivka", newUser.getPrezdivka());
        values.put("email", newUser.getEmail());
        values.put("heslo", newUser.getHeslo());
        values.put("profilovka", newUser.getProfilovka());
        values.put("timeStamp", formatter.format(date));
        myDataBase.update("uzivatel", values, "prezdivka = ?", new String[]{prezdivka});
    }

    public void updateUserWithoutProfileImage(String prezdivka, User newUser) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put("prezdivka", newUser.getPrezdivka());
        values.put("email", newUser.getEmail());
        values.put("heslo", newUser.getHeslo());
        values.put("timeStamp", formatter.format(date));
        myDataBase.update("uzivatel", values, "prezdivka = ?", new String[]{prezdivka});
    }
}