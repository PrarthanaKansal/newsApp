package com.example.prarthana.newsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NewsDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;

    public NewsDBHelper(Context context) {
        super(context, NewsContract.DATABASE_NAME, null, NewsContract.DATABASE_VERSION);
    }

    public void openConnection() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + NewsContract.aricleEntry.TABLE_NAME + " (" +
                NewsContract.aricleEntry.COLUMN_TITLE + " text not null ," +
                NewsContract.aricleEntry.COLUMN_DESCRIPTION + " text not null, " +
                NewsContract.aricleEntry.COLUMN_URL + " text not null ," +
                NewsContract.aricleEntry.COLUMN_URL_TO_IMAGE + " text not null, " +
                NewsContract.aricleEntry.COLUMN_CATEGORY + " text not null " + ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + NewsContract.aricleEntry.TABLE_NAME;
        db.execSQL(query);
        onCreate(db);

    }

    public void insertNews(ArrayList<ContentValues> listofContentValues) {
        for (int i = 0; i < listofContentValues.size(); i++) {
            database.insert(NewsContract.aricleEntry.TABLE_NAME, null, listofContentValues.get(i));
        }

    }

    public void clearNews() {
        String querydel = "delete from" + " " + NewsContract.aricleEntry.TABLE_NAME;
        database.execSQL(querydel);

        //database.delete(NewsContract.TABLE_NAME,null,null);
    }

    public int countNews() {
        String query = " Select Count(*) from " + NewsContract.aricleEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.getInt(0);
        return  cursor.getInt(0);
    }

    public Cursor getNews(){
//        String query = "select * from " + NewsContract.TABLE_NAME;
//        Cursor cursor = database.rawQuery(query, null);
//        return cursor;
        Cursor cursor= database.query(NewsContract.aricleEntry.TABLE_NAME,null,null,null,null,null,null);
        return cursor;
    }


}
