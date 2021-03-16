package com.example.lyricsapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lyricsapp.database.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView username, email;
    private DatabaseHelper databaseHelper;
    private String user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        username = view.findViewById(R.id.username_profile);
        email = view.findViewById(R.id.email_profile);
        profileImage = view.findViewById(R.id.image_profile);

        setHasOptionsMenu(true);

        user = getArguments().getString("DATA_FROM_DRAWER");

        setUserData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit :
                Intent editProfile = new Intent(getContext(), EditProfileActivity.class);
                editProfile.putExtra("USERNAME_FROM_PROFILE", user);
                startActivity(editProfile);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUserData() {
        databaseHelper.openDataBase();
        Cursor data = databaseHelper.getUserHeader(user);
        data.moveToFirst();
        username.setText(data.getString(data.getColumnIndex("prezdivka")));
        email.setText(data.getString(data.getColumnIndex("email")));
        byte[] blob2 = data.getBlob(data.getColumnIndex("profilovka"));
        databaseHelper.close();
        if (blob2 == null) {
            Log.i("PROFILE PHOTO","V databázi není pro tento účet profilová fotka");
        } else {
            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(blob2);
            Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
            profileImage.setImageBitmap(bitmap2);
        }
    }

}
