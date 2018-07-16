package com.example.user.popmov.data;

import android.net.Uri;
import android.provider.BaseColumns;

final public class MoviesContract {

    private MoviesContract(){}

    public final static String AUTHORITY = "com.example.user.popmov";
    private final static Uri CONTENT = Uri.parse("content://" + AUTHORITY);

    public final static String PATH_MOVIES = "movies";

    public static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLe_NAME + "( " +
            MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            MovieEntry.COLOUMN_MOVIE_ID + " INTEGER NOT NULL , " +
            MovieEntry.COLOUMN_TITLE + " TEXT NOT NULL , " +
            MovieEntry.COLOUMN_IMAGE_PATH + " TEXT NOT NULL , " +
            MovieEntry.COLOUMN_RATING + " TEXT NOT NULL , " +
            MovieEntry.COLOUMN_DESCRIPTION + " TEXT NOT NULL , " +
            MovieEntry.COLOUMN_RELASE_DATE + " TEXT NOT NULL )";


    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = CONTENT.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLe_NAME = "movies";
        public static final String COLOUMN_MOVIE_ID ="movieID";
        public static final String COLOUMN_TITLE ="title";
        public static final String COLOUMN_IMAGE_PATH ="imagePath";
        public static final String COLOUMN_RATING ="rating";
        public static final String COLOUMN_DESCRIPTION = "description";
        public static final String COLOUMN_RELASE_DATE = "relaseDate";

    }

}
