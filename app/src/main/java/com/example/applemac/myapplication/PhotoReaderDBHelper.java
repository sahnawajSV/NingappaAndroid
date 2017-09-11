package com.example.applemac.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.applemac.myapplication.PhotoEntry;

/**
 * Created by applemac on 05/09/17.
 */


public class PhotoReaderDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "TestPhoto.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PhotoEntry.TABLE_NAME + " (" +
                    PhotoEntry._ID + " INTEGER PRIMARY KEY," +
                    PhotoEntry.PHOTO_ID + " TEXT," +
                    PhotoEntry.PHOTO_TITLE + " TEXT," +
                    PhotoEntry.URL + " TEXT," +
                    PhotoEntry.THUMBNAIL_URL + " TEXT," +
                    PhotoEntry.STATUS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME;

    public PhotoReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
