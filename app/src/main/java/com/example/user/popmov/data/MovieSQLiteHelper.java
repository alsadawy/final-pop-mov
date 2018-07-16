package com.example.user.popmov.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    private static final String dataBaseName ="movie.db";
    private static final int version_nu = 1;

    public MovieSQLiteHelper(Context context) {
        super(context, dataBaseName, null, version_nu);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoviesContract.CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLe_NAME);
        onCreate(db);
    }
}
