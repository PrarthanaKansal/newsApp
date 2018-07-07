package com.example.prarthana.newsapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NewsProvider extends ContentProvider {
    NewsDBHelper newsDBHelper;
    public static final int article=100;
    public static final int ARTICLE_TITLE_ONLY=101;
    @Override
    public boolean onCreate() {
        newsDBHelper = new NewsDBHelper(getContext());
        return true;
    }
    public static UriMatcher uriMatcher=matchURI();


    public static UriMatcher matchURI(){
        UriMatcher matcher= new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,NewsContract.PATH_ARTICLE,article);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,NewsContract.PATH_ARTICLE+"/*",ARTICLE_TITLE_ONLY);
        return matcher;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db= newsDBHelper.getWritableDatabase();
        int match=uriMatcher.match(uri);
        Cursor cursor;
        switch (match){
            case article: {
                cursor = db.query(NewsContract.aricleEntry.TABLE_NAME, null, null, null, null, null, null, null);
                break;
            }
            case ARTICLE_TITLE_ONLY: {
                String column= uri.getPathSegments().get(1);
                projection= new String[]{column};
                cursor=db.query(NewsContract.aricleEntry.TABLE_NAME,projection,null,null,null,null,null);
                break;
            }


            default:{
                throw new UnsupportedOperationException("Unknown uri" + uri);
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db= newsDBHelper.getWritableDatabase();
        int match=uriMatcher.match(uri);
        int counter=0;
        switch (match){
            case article:{
                counter=db.delete(NewsContract.aricleEntry.TABLE_NAME,null,null);
                break;

            }
            default:{
                throw new UnsupportedOperationException("Unknown uri" + uri);
            }
        }
        return counter;



    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db= newsDBHelper.getWritableDatabase();
        int match=uriMatcher.match(uri);
        int counter=0;
        switch (match){
            case article:{
                db. beginTransaction();
                try{
                for (ContentValues value:values) {
                    long id=db.insert(NewsContract.aricleEntry.TABLE_NAME,null,value);
                    if(id!=-1){
                        counter++;
                    }
                }
                db.setTransactionSuccessful();
                }
                catch (Exception e){}
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return counter;
            }
            default:{
                return super.bulkInsert(uri, values);
            }

        }


    }
}
