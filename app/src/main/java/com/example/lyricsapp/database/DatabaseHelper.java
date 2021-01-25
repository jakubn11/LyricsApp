package com.example.lyricsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.example.lyricsapp.classes.Uzivatel;

import java.io.ByteArrayOutputStream;
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
        super(context, DATABASE_NAME, null , DB_VERSION);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        dbFile = new File(DB_PATH + DATABASE_NAME);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if(!dbFile.exists()){
            SQLiteDatabase db = super.getWritableDatabase();
            copyDataBase(db.getPath());
        }
        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if(!dbFile.exists()){
            SQLiteDatabase db = super.getReadableDatabase();
            copyDataBase(db.getPath());
        }
        return super.getReadableDatabase();
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void copyDataBase(String DB_PATH){
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
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /*public List<String> getData(){
        List<String> list = new ArrayList<>();
        String query = "SELECT * FROM uzivatel";
        Cursor cursor = myDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }*/

    public Boolean checkUser(String username, String password) {
        String[] columns = {"prezdivka"};

        String selection = "prezdivka=? and heslo=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = myDataBase.query("uzivatel", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public void vlozeniUzivatele(Uzivatel uzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", uzivatel.getPrezdivka());
        values.put("email", uzivatel.getEmail());
        values.put("profilovka", uzivatel.getProfilovka());
        values.put("heslo", uzivatel.getHeslo());
        myDataBase.insert("uzivatel", null, values);
    }

    public void updateUzivatel(Uzivatel uzivatel, Uzivatel newUzivatel) {
        ContentValues values = new ContentValues();
        values.put("prezdivka", newUzivatel.getPrezdivka());
        values.put("email", newUzivatel.getEmail());
        values.put("heslo", newUzivatel.getHeslo());
//        values.put("prezdivka", newUzivatel.getProfilovka());
        myDataBase.update("uzivatel", values, "prezdivka = ?", new String[]{uzivatel.getPrezdivka()});
    }
}