package com.jhesed.rbv.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RandomBibleVerseDbHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    final String TAG = "RandomBibleVerseDBHelper";
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public RandomBibleVerseDbHelper(Context context) {
        super(context, BibleVerseContract.DB_NAME, null,
                BibleVerseContract.DB_VERSION);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(BibleVerseContract.DB_NAME)
                .toString();
    }

    public void createDataBase()
            throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            // TODO: DELETE ALL OF THESE HERE!!!
//            SQLiteDatabase db = this.getReadableDatabase();
//            db.execSQL("DROP TABLE IF EXISTS " + BibleVerseContract.BibleVerseEntry.TABLE);
//            onCreate(db);
//            this.importCSVData();
        } else {
            this.getWritableDatabase();
            this.importCSVData();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH;
            checkDB
                    = SQLiteDatabase
                    .openDatabase(
                            myPath, null,
                            SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

            // database doesn't exist yet.
            Log.e("message", "" + e);
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void openDataBase()
            throws SQLException {
        // Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase
                .openDatabase(
                        myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        // close the database.
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + BibleVerseContract.BibleVerseEntry.TABLE +
                        " ( " +
                        BibleVerseContract.BibleVerseEntry._ID +
                        " INTEGER PRIMARY " +
                        "KEY AUTOINCREMENT, " +
                        BibleVerseContract.BibleVerseEntry.COL_TITLE +
                        " TEXT NOT " +
                        "NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_MBB +
                        " TEXT NOT " +
                        "NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_NIV +
                        " TEXT NOT " +
                        "NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_NASB +
                        " TEXT NOT " +
                        "NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_DATE_CREATED +
                        " " +
                        "DATETIME DEFAULT NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_DATE_MODIFIED +
                        " " +
                        "DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        BibleVerseContract.BibleVerseEntry.COL_DATE_LAST_SYNC +
                        " " +
                        "DATETIME DEFAULT NULL, " +
                        BibleVerseContract.BibleVerseEntry.COL_IS_SYNCED +
                        " INTEGER" +
                        " DEFAULT NULL); ";

        Log.d(TAG, createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Comment out on prod
        db.execSQL("DROP TABLE IF EXISTS " + BibleVerseContract.BibleVerseEntry.TABLE);
        onCreate(db);
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + BibleVerseContract.BibleVerseEntry._ID +
                ", " +
                BibleVerseContract.BibleVerseEntry.COL_TITLE +
                " FROM " + BibleVerseContract.BibleVerseEntry.TABLE + ";";

        return db.rawQuery(query, null);
    }

    public Cursor select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT " + BibleVerseContract.BibleVerseEntry.COL_TITLE +
                        ", " + BibleVerseContract.BibleVerseEntry.COL_MBB +
                        ", " + BibleVerseContract.BibleVerseEntry.COL_NIV +
                        ", " + BibleVerseContract.BibleVerseEntry.COL_NASB +
                        " FROM " + BibleVerseContract.BibleVerseEntry.TABLE +
                        " WHERE " + BibleVerseContract.BibleVerseEntry._ID +
                        " = " + id;

        return db.rawQuery(query, null);
    }

    public void insert(String title, String mbb, String niv, String nasb) {
        // TODO: Check row uniqueness
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BibleVerseContract.BibleVerseEntry.COL_TITLE, title);
        values.put(BibleVerseContract.BibleVerseEntry.COL_MBB, mbb);
        values.put(BibleVerseContract.BibleVerseEntry.COL_NIV, niv);
        values.put(BibleVerseContract.BibleVerseEntry.COL_NASB, nasb);
        db.insertWithOnConflict(BibleVerseContract.BibleVerseEntry.TABLE,
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void prepopulateData() {
        try {
            this.createDataBase();
            this.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importCSVData() throws IOException {

        String csvFile = "bible_verses.csv";
        AssetManager manager = this.myContext.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer =
                new BufferedReader(new InputStreamReader(inStream));

        String line = "";
        String tableName = BibleVerseContract.BibleVerseEntry.TABLE;

        String columns = "_id, " +
                BibleVerseContract.BibleVerseEntry.COL_TITLE + ", " +
                BibleVerseContract.BibleVerseEntry.COL_NIV + ", " +
                BibleVerseContract.BibleVerseEntry.COL_MBB + ", " +
                BibleVerseContract.BibleVerseEntry.COL_NASB;
        String startString = "INSERT INTO " + tableName + " (" + columns +
                ") values(";
        String endString = ");";

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        while ((line = buffer.readLine()) != null) {
            StringBuilder sb = new StringBuilder(startString);
            String[] str = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            sb.append("'" + str[0] + "',");
            sb.append("'" + str[1].replace("\"", "") + "',");
            sb.append("'" + str[2].replace("\"", "") + "',");
            sb.append("'" + str[3].replace("\"", "") + "',");
            sb.append("'" + str[4].replace("\"", "") + "'");

            sb.append(endString);
            db.execSQL(sb.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
