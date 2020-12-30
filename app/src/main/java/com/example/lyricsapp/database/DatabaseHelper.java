package com.example.lyricsapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = DatabaseHelper.class.getName();
    private static String DB_PATH;
    private static final String DATABASE_NAME = "Lyrics.sqlite";
    private static final int DB_VERSION = 1;

    public final Context context;
    private SQLiteDatabase myDataBase = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null , DB_VERSION);

        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.v("log_tag", "DBPath: " + DB_PATH + DATABASE_NAME);
    }


    public boolean checkDataBase(){
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }


    private void copyDataBase() throws IOException{
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public void createDataBase() {
        boolean databaseExist = checkDataBase();

        if (databaseExist) {
            Log.v("log_tag", "database does exist");
        } else {
            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    public boolean openDataBase() throws SQLException{
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return myDataBase != null;
    }


    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }


    public String getUserNameFromDB(){
        String query = "SELECT locale FROM android_metadata";
        Cursor cursor = myDataBase.rawQuery(query, null);
        String userName = null;
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    userName = cursor.getString(0);
                }while (cursor.moveToNext());
            }
        }
        return userName;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "Upgrading database, this will drop database and recreate.");
    }

}