package com.example.lyricsapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Lyrics.db";
    public final static String DB_PATH = "/data/data/com.example.lyricsapp/databases/";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            deleteDatabase();
        }
    }

    @Override
    public synchronized void close() throws SQLException {
        if (myDatabase != null)
            myDatabase.close();
        super.close();

    }

    private boolean checkDatabase() {
        boolean checkDB = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {

        }
        return checkDB;
    }

    private void copyDatabase() throws IOException {
        InputStream myInputStream = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutputStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[2024];
        int length;
        while ((length = myInputStream.read(buffer)) > 0) {
            myOutputStream.write(buffer, 0, length);
        }
        myOutputStream.flush();
        myOutputStream.close();
        myInputStream.close();
    }

    public void deleteDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    public void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void createDatabase() throws IOException {
        boolean myDatabaseExist = checkDatabase();
        if (myDatabaseExist) {
            Log.v("DB Exists", "db exists");
        } else {
            this.getReadableDatabase();
            try {
                this.close();
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying DB");
            }
        }
    }

}