package com.example.casher.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseSettings.DB_NAME, null, DatabaseSettings.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String monthTable = "CREATE TABLE " + DatabaseSettings.MonthEntry.TABLE_NAME + " (" +
                DatabaseSettings.MonthEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSettings.MonthEntry.COL_MONTH_NAME + " TEXT NOT NULL, " +
                DatabaseSettings.MonthEntry.COL_MONTH_INCOME + " INTEGER NOT NULL, " +
                DatabaseSettings.MonthEntry.COL_MONTH_EXPENSES + " INTEGER NOT NULL, " +
                DatabaseSettings.MonthEntry.COL_MONTH_BALANCE + " INTEGER NOT NULL);";

        String dateTable = "CREATE TABLE " + DatabaseSettings.DateEntry.TABLE_NAME + " (" +
                DatabaseSettings.DateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSettings.DateEntry.COL_DATE_NAME + " TEXT NOT NULL, " +
                DatabaseSettings.DateEntry.COL_DATE_INCOME + " INTEGER NOT NULL, " +
                DatabaseSettings.DateEntry.COL_DATE_EXPENSES + " INTEGER NOT NULL);";

        String detailsInTable = "CREATE TABLE " + DatabaseSettings.DetailsInEntry.TABLE_NAME + " (" +
                DatabaseSettings.DetailsInEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSettings.DetailsInEntry.COL_DATE_NAME + " TEXT NOT NULL, " +
                DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES + " TEXT NOT NULL, " +
                DatabaseSettings.DetailsInEntry.COL_IN_TOTAL + " INTEGER NOT NULL, " +
                DatabaseSettings.DetailsInEntry.COL_IN_NOTES + " TEXT NOT NULL);";

        String detailsExTable = "CREATE TABLE " + DatabaseSettings.DetailsExEntry.TABLE_NAME + " (" +
                DatabaseSettings.DetailsExEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSettings.DetailsExEntry.COL_DATE_NAME + " TEXT NOT NULL, " +
                DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES + " TEXT NOT NULL, " +
                DatabaseSettings.DetailsExEntry.COL_EX_TOTAL + " INTEGER NOT NULL, " +
                DatabaseSettings.DetailsExEntry.COL_EX_NOTES + " TEXT NOT NULL);";

        db.execSQL(monthTable);
        db.execSQL(dateTable);
        db.execSQL(detailsInTable);
        db.execSQL(detailsExTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSettings.MonthEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSettings.DateEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSettings.DetailsInEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSettings.DetailsExEntry.TABLE_NAME);
        onCreate(db);
    }
}
