package com.example.applemac.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.net.HttpURLConnection;

/**
 * Created by applemac on 05/09/17.
 */

public class PhotoProcess {

    Context context = null;
    Activity activity = null;


    public PhotoProcess(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }

    public void listPhoto() {

        Log.i("TESTAPP", " ###### listPhoto: ");

        int i=0,j=0;
        PhotoReaderDBHelper mDbHelper = new PhotoReaderDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor resultSet = db.rawQuery("Select * from " + PhotoEntry.TABLE_NAME + " where " + PhotoEntry.STATUS + " is NULL",null);
        resultSet.moveToFirst();

        Log.i("TESTAPP", " ###### listPhoto count : " + resultSet.getCount());

        if(resultSet.getCount() == 0) {
            return;
        }
        TableLayout l1 = (TableLayout) activity.findViewById(R.id.table1);
        l1.setStretchAllColumns(true);
        l1.bringToFront();

        do
        {   TableRow tr =  new TableRow(context);
                tr.setId(Integer.valueOf(resultSet.getString(0)));
            ImageView i1 = new ImageView(context);
            TextView t1 = new TextView(context);
            ImageView i2 = new ImageView(context);


            final String urlID =  resultSet.getString(3);
            t1.setText(resultSet.getString(2));
            t1.setPadding(8, 8, 8, 8);
           // t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            t1.setMaxWidth(200);

            try {
                FileInputStream io = context.openFileInput(resultSet.getString(4));
                Bitmap img =   BitmapFactory.decodeStream(io);
                i1.setImageBitmap(img);
            }
            catch (Exception e) {
                e.printStackTrace();
            }


            i2.setImageDrawable( activity.getResources().getDrawable(R.drawable.ic_delete_black_24dp));
            tr.addView(i1);
            tr.addView(t1);
            tr.addView(i2);


            l1.addView(tr);
            i++;

            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, PhotoDetailActivity.class);


                    intent.putExtra("urlID",urlID);
                    activity.startActivity(intent);
                }
            });
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, PhotoDetailActivity.class);


                    intent.putExtra("urlID",urlID);
                    activity.startActivity(intent);
                }
            });

            i2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    PhotoReaderDBHelper mDbHelper = new PhotoReaderDBHelper(context);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(PhotoEntry.STATUS, "deleted");
                   // db.update(PhotoEntry.TABLE_NAME, values, PhotoEntry._ID + "", new String[] { String.valueOf(view.getId())});
                  //  listPhoto();
                }
            });


            if(i == 5) {
                break;
            }
        }while(resultSet.moveToNext());
    }
}
