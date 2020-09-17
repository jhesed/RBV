package com.jhesed.rbv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RandomBibleVerseDbHelper extends SQLiteOpenHelper {
    final String TAG = "RandomBibleVerseDBHelper";

    public RandomBibleVerseDbHelper(Context context) {
        super(context, BibleVerseContract.DB_NAME, null, BibleVerseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + BibleVerseContract.BibleVerseEntry.TABLE + " ( " +
                BibleVerseContract.BibleVerseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BibleVerseContract.BibleVerseEntry.COL_TITLE + " TEXT NOT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_MBB + " TEXT NOT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_NIV + " TEXT NOT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_NASB + " TEXT NOT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_ESV + " TEXT NOT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                BibleVerseContract.BibleVerseEntry.COL_DATE_MODIFIED + " TIMESTAMP DEFAULT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_DATE_LAST_SYNC + " TIMESTAMP DEFAULT NULL, " +
                BibleVerseContract.BibleVerseEntry.COL_IS_SYNCED + " INTEGER DEFAULT NULL); ";

        Log.d(TAG, createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BibleVerseContract.BibleVerseEntry.TABLE);
        onCreate(db);
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + BibleVerseContract.BibleVerseEntry._ID + ", " +
                BibleVerseContract.BibleVerseEntry.COL_TITLE +
                " FROM " + BibleVerseContract.BibleVerseEntry.TABLE + ";";

        return db.rawQuery(query, null);
    }

    public Cursor select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + BibleVerseContract.BibleVerseEntry.COL_TITLE +
                ", " + BibleVerseContract.BibleVerseEntry.COL_MBB +
                ", " + BibleVerseContract.BibleVerseEntry.COL_NIV +
                ", " + BibleVerseContract.BibleVerseEntry.COL_NASB +
                ", " + BibleVerseContract.BibleVerseEntry.COL_ESV +
                " FROM " + BibleVerseContract.BibleVerseEntry.TABLE +
                " WHERE " + BibleVerseContract.BibleVerseEntry._ID + " = " + id;

        return db.rawQuery(query, null);
    }

    public void insert(String title, String mbb, String niv, String nasb, String esv) {
        // TODO: Check row uniqueness
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BibleVerseContract.BibleVerseEntry.COL_TITLE, title);
        values.put(BibleVerseContract.BibleVerseEntry.COL_MBB, mbb);
        values.put(BibleVerseContract.BibleVerseEntry.COL_NIV, niv);
        values.put(BibleVerseContract.BibleVerseEntry.COL_NASB, nasb);
        values.put(BibleVerseContract.BibleVerseEntry.COL_ESV, esv);
        db.insertWithOnConflict(BibleVerseContract.BibleVerseEntry.TABLE,
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
