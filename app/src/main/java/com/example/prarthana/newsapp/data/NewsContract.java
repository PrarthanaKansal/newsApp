package com.example.prarthana.newsapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {
    public static final String CONTENT_AUTHORITY="com.example.prarthana.newsapp";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLE="article";

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="news.db";

    public static class aricleEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();

        public static final String TABLE_NAME="article";

        public static final String  COLUMN_TITLE="title";
        public static final String  COLUMN_DESCRIPTION="description";
        public static final String  COLUMN_URL="url";
        public static final String  COLUMN_CATEGORY="category";
        public static final String  COLUMN_URL_TO_IMAGE="urlTOImage";
    }






}
