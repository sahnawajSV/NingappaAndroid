package com.example.applemac.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.FileOutputStream;
/**
 * Created by applemac on 05/09/17.
 */

public class PhotoAsynchTask extends AsyncTask<String, String, String> {

    Context context = null;
    Activity activity = null;
    HttpURLConnection urlConnection;

    public PhotoAsynchTask(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("http://jsonplaceholder.typicode.com/photos");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.i("TESTAPP", " ###### json: " + result.toString());

            //JSONObject jsonObj = new JSONObject(result.toString());
            JSONArray jsonArray = new JSONArray(result.toString());


            Log.i("TESTAPP", " ###### jsonArray 1: " + jsonArray.get(0));

            Log.i("TESTAPP", " ###### jsonArray length: " + jsonArray.length());

            for (int i=0; i<jsonArray.length(); i++) {

                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
               // Log.i("TESTAPP", " ###### jjsonObject: " + jsonObject.get("albumId"));

                String thumbNail =  jsonObject.get("thumbnailUrl").toString();

                thumbNail =  "150" + thumbNail.substring( thumbNail.lastIndexOf("/")+1, thumbNail.length());

                String urlID =  jsonObject.get("url").toString();

                urlID =  "600" + urlID.substring( urlID.lastIndexOf("/")+1, urlID.length());

                if(i ==0) {
                    Log.i("TESTAPP", " ###### thumbNail: " + thumbNail);
                }

                InputStream is = (InputStream) new URL(jsonObject.get("thumbnailUrl").toString()).getContent();
                Bitmap thumbnail = BitmapFactory.decodeStream(is);

                FileOutputStream fos = context.openFileOutput(thumbNail, Context.MODE_PRIVATE);
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();


                 is = (InputStream) new URL(jsonObject.get("url").toString()).getContent();
                Bitmap img = BitmapFactory.decodeStream(is);

                 fos = context.openFileOutput(urlID, Context.MODE_PRIVATE);
                img.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();



                PhotoReaderDBHelper mDbHelper = new PhotoReaderDBHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(PhotoEntry.PHOTO_ID, jsonObject.get("id").toString());
                values.put(PhotoEntry.PHOTO_TITLE, jsonObject.get("title").toString());
                values.put(PhotoEntry.URL, urlID);
                values.put(PhotoEntry.THUMBNAIL_URL, thumbNail);

// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(PhotoEntry.TABLE_NAME, null, values);

               Log.i("TESTAPP", " ###### newRowId: " + newRowId);

                if (i == 5) {
                    break;
                }
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }

    @Override
    protected void onPostExecute(String result)
    {

        Log.i("TESTAPP", " ###### onPostExecute: ");

        PhotoProcess photoProcess = new PhotoProcess(context, activity);
        photoProcess.listPhoto();

    }
}
