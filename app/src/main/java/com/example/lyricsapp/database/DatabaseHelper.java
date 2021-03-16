package com.example.lyricsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.lyricsapp.classes.Uzivatel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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

    public Cursor getData() {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibene_pisnicky";
        Cursor data = myDataBase.rawQuery(query, null);
        return data;
    }

    public Cursor getUserHeader(String username) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM uzivatel WHERE TRIM(prezdivka) = '"+username.trim()+"'";
        Cursor email = myDataBase.rawQuery(query, null);
        return email;
    }

    public Cursor getSong (String nameSong) {
        myDataBase = this.getReadableDatabase();
        String query = "SELECT * FROM oblibene_pisnicky WHERE TRIM(nazev_pisnicky) = '"+nameSong.trim()+"'";
        Cursor email = myDataBase.rawQuery(query, null);
        return email;
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

    public void insertUserWithProfileImage(Uzivatel uzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", uzivatel.getPrezdivka());
        values.put("email", uzivatel.getEmail());
        values.put("profilovka", uzivatel.getProfilovka());
        values.put("heslo", uzivatel.getHeslo());
        myDataBase.insert("uzivatel", null, values);
    }

    public void insertUserWithoutProfileImage(Uzivatel uzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", uzivatel.getPrezdivka());
        values.put("email", uzivatel.getEmail());
        values.put("heslo", uzivatel.getHeslo());
        myDataBase.insert("uzivatel", null, values);
    }

    public void updateUserWithProfileImage(String prezdivka, Uzivatel newUzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", newUzivatel.getPrezdivka());
        values.put("email", newUzivatel.getEmail());
        values.put("heslo", newUzivatel.getHeslo());
        values.put("profilovka", newUzivatel.getProfilovka());
        myDataBase.update("uzivatel", values, "prezdivka = ?", new String[]{prezdivka});
    }

    public void updateUserWithoutProfileImage(String prezdivka, Uzivatel newUzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", newUzivatel.getPrezdivka());
        values.put("email", newUzivatel.getEmail());
        values.put("heslo", newUzivatel.getHeslo());
        myDataBase.update("uzivatel", values, "prezdivka = ?", new String[]{prezdivka});
    }
}