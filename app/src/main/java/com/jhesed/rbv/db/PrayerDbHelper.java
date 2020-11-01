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

import com.jhesed.rbv.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by jhesed on 7/29/2017.
 */


public class PrayerDbHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    final String TAG = "PrayerDBHelper";
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public PrayerDbHelper(Context context) {
        super(context, PrayerContract.DB_NAME, null, PrayerContract.DB_VERSION);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(PrayerContract.DB_NAME)
                .toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + PrayerContract.PrayerEntry.TABLE + " ( " +
                PrayerContract.PrayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PrayerContract.PrayerEntry.COL_TITLE + " TEXT NOT NULL, " +
                PrayerContract.PrayerEntry.COL_CONTENT + " TEXT NOT NULL, " +
                PrayerContract.PrayerEntry.COL_CATEGORY + " TEXT NOT NULL, " +
                PrayerContract.PrayerEntry.COL_DAY + " INTEGER DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_REMINDER_ENABLED + " INTEGER DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_REMINDER_TIME + " DATETIME DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_DONE + " INTEGER DEFAULT 0, " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + " INTEGER DEFAULT 0, " +
                PrayerContract.PrayerEntry.COL_DATE_CREATED +
                " DATETIME DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_DATE_MODIFIED +
                " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                PrayerContract.PrayerEntry.COL_DATE_LAST_SYNC + " DATETIME DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_SYNCED + " INTEGER DEFAULT NULL); ";

        Log.d(TAG, createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PrayerContract.PrayerEntry.TABLE);
        onCreate(db);
    }

    public Cursor selectAll(int isDone, int day, int isAnswered) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PrayerContract.PrayerEntry._ID + ", " +
                PrayerContract.PrayerEntry.COL_TITLE + ", " +
                PrayerContract.PrayerEntry.COL_CATEGORY +
                " FROM " + PrayerContract.PrayerEntry.TABLE + " WHERE " +
                PrayerContract.PrayerEntry.COL_IS_DONE + " = " + isDone + " AND " +
//                PrayerContract.PrayerEntry.COL_DAY + " = " + day + " AND " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + " = " + isAnswered + ";";

        return db.rawQuery(query, null);
    }

    public Cursor select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + PrayerContract.PrayerEntry.COL_TITLE +
                ", " + PrayerContract.PrayerEntry.COL_CONTENT +
                ", " + PrayerContract.PrayerEntry.COL_IS_ANSWERED +
                " FROM " + PrayerContract.PrayerEntry.TABLE +
                " WHERE " + PrayerContract.PrayerEntry._ID + " = " + id;

        return db.rawQuery(query, null);
    }

    public void update(int id, CharSequence title, CharSequence content, int isAnswered) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + PrayerContract.PrayerEntry.TABLE +
                " SET " + PrayerContract.PrayerEntry.COL_TITLE + "=\"" + title + "\", " +
                PrayerContract.PrayerEntry.COL_CONTENT + "=\"" + content + "\", " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + "=\"" + isAnswered + "\" " +
                " WHERE " + PrayerContract.PrayerEntry._ID + "=" + id;
        db.execSQL(query);

    }

    public void prayerDone(int id, int isDone) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + PrayerContract.PrayerEntry.TABLE +
                " SET " + PrayerContract.PrayerEntry.COL_IS_DONE + "=\"" + isDone + "\" " +
                " WHERE " + PrayerContract.PrayerEntry._ID + "=" + id;
        db.execSQL(query);
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PrayerContract.PrayerEntry.TABLE +
                " WHERE " + PrayerContract.PrayerEntry._ID + " = " + id;
        db.execSQL(query);
    }

    public void insert(String title, String content, String category) {
        // TODO: Check row uniqueness
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PrayerContract.PrayerEntry.COL_TITLE, title);
        values.put(PrayerContract.PrayerEntry.COL_CONTENT, content);
        values.put(PrayerContract.PrayerEntry.COL_DAY, getDayInInteger());
        values.put(PrayerContract.PrayerEntry.COL_CATEGORY, category);
        db.insertWithOnConflict(PrayerContract.PrayerEntry.TABLE,
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public int getDayInInteger() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getIsAnsweredInteger(int selectedId) {

        // get selected radio button for answered
        int isAnswered = 0;
        switch (selectedId) {
            case R.id.radio_answered_yes:
                isAnswered = 1;
                break;
        }
        return isAnswered;
    }

    public void createDataBase()
            throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            // TODO: DELETE ALL OF THESE HERE!!!
//            SQLiteDatabase db = this.getReadableDatabase();
//            db.execSQL("DROP TABLE IF EXISTS " + PrayerContract.PrayerEntry.TABLE);
//            onCreate(db);
//            this.importCSVData();
        } else {
            this.getWritableDatabase();
            this.importCSVData();
        }
    }

    public void prepopulateData() {
        try {
            this.createDataBase();
            this.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void importCSVData() throws IOException {

        Log.i("message", "(PrayerDbHelper.importCSVData): Importing CSV data");

        String csvFile = "prayer_requests.csv";
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
        String tableName = PrayerContract.PrayerEntry.TABLE;

        String columns = "_id, " +
                PrayerContract.PrayerEntry.COL_TITLE + ", " +
                PrayerContract.PrayerEntry.COL_CONTENT + ", " +
                PrayerContract.PrayerEntry.COL_CATEGORY;
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
            sb.append("'" + str[3].replace("\"", "") + "'");

            sb.append(endString);
            db.execSQL(sb.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public synchronized void close() {
        // close the database.
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }
}
