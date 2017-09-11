package com.example.applemac.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        String urlID = getIntent().getStringExtra("urlID");

       ImageView imgView =  (ImageView)findViewById(R.id.show_photo);

        try {
            FileInputStream io = openFileInput(urlID);
            Bitmap img = BitmapFactory.decodeStream(io);
            imgView.setImageBitmap(img);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Log.i("TESTAPP", " ###### urlID: " + urlID);
    }
}
