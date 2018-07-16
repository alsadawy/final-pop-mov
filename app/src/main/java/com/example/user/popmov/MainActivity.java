package com.example.user.popmov;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.user.popmov.data.MoviesContract;
import com.example.user.popmov.utiles.JsonUtils;
import com.example.user.popmov.utiles.MovieAdapter;
import com.example.user.popmov.utiles.NetworkUtiles;
import com.example.user.popmov.utiles.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnImgClicked {

    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    @BindView(R.id.noFavourite)
    TextView nothing;

    private String json;
    private String apiKey;
    private LoaderManager manager1;
    private ArrayList<String> images;
    private final String POSITION = "position";
    public static final String MOVIE_INTENT_KEY = "movie";
    private final String URL_KEY = "urlKey";


    // Bundle key
    private final String ON_SAVED_STATE_KEY = "json";
    // loaders ids
    private final int IMAGE_LODER_ID = 35;
    private final int MOVIE_DATA_LOADER = 36;
    private final int GET_DATA_LOADER_ID = 37;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null ){
            json = savedInstanceState.getString(ON_SAVED_STATE_KEY);
            images = JsonUtils.imageList(json);
            ArrayList<String> imagePaths = new ArrayList<>();
            for (String path : images) {
                imagePaths.add(JsonUtils.BASE_URL_IMAGE + path);
            }
            MovieAdapter adapter = new MovieAdapter(imagePaths, MainActivity.this);
            recyclerView.setAdapter(adapter);
        }
        else {
            apiKey = BuildConfig.ApiKey;
            manager1 = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putInt(URL_KEY,1);
            manager1.initLoader(GET_DATA_LOADER_ID,bundle,getData);
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ON_SAVED_STATE_KEY,json);

    }

    private boolean isFavour(String imagePath) {
        String selection = MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH + " =?";
        String[] args = {imagePath};
        Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null, selection, args, null);
        int count = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return count > 0;
    }

    @Override
    public void onClicklistner(int position) {

        if(isFavour(images.get(position))){
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION,position);
            manager1.initLoader(MOVIE_DATA_LOADER,bundle,allMovieDataLoader);
        }

        Intent intent = new Intent(this, DetailsActivity.class);
        Movie movie = JsonUtils.parseJSON(json, position);
        intent.putExtra(MOVIE_INTENT_KEY,movie);
        startActivity(intent);
    }



    final LoaderManager.LoaderCallbacks<String> getData = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {

            return  new AsyncTaskLoader<String>(MainActivity.this) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }
                @Override
                public String loadInBackground() {
                    URL url;
                    if( args.getInt(URL_KEY) == 1) {
                         url = NetworkUtiles.makeUrl(apiKey, NetworkUtiles.POPULARITY_SORT);
                    }
                    else {
                        url = NetworkUtiles.makeUrl(apiKey, NetworkUtiles.HIGHEST_SORT);
                    }
                    try {
                        return NetworkUtiles.getResponse(url);

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String s) {
            if (s != null) {
                json = s;
                images = JsonUtils.imageList(s);
                ArrayList<String> imagePaths = new ArrayList<>();
                for (String path : images) {
                    imagePaths.add(JsonUtils.BASE_URL_IMAGE + path);
                }
                MovieAdapter adapter = new MovieAdapter(imagePaths, MainActivity.this);
                recyclerView.setAdapter(adapter);


            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> allImagesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(MainActivity.this) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    String[] arg = {MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH};
                    return getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                            arg, null, null, MoviesContract.MovieEntry._ID);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            images = new ArrayList<>();
            try {
                while (cursor.moveToNext()) {
                    images.add(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH)));
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            ArrayList<String> imagePaths = new ArrayList<>();
            for (String path : images) {
                imagePaths.add(JsonUtils.BASE_URL_IMAGE + path);
            }

            MovieAdapter adapter = new MovieAdapter(imagePaths, MainActivity.this);
            recyclerView.setAdapter(adapter);
            if (imagePaths.isEmpty()) {
                nothing.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<Cursor> allMovieDataLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<Cursor>(MainActivity.this) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    String whereClause = MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH + " =?";
                    String[] selectionArgs = {images.get(args.getInt(POSITION))};

                    return getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                            null,
                            whereClause,
                            selectionArgs,
                            null);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            Movie movie ;
            if (cursor.moveToNext()) {
                movie = new Movie(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_RELASE_DATE)),
                        cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLOUMN_MOVIE_ID)));

                cursor.close();
                intent.putExtra(MOVIE_INTENT_KEY,movie);

            }
            startActivity(intent);


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.most_popular:
                Bundle bundle = new Bundle();
                bundle.putInt(URL_KEY,1);
                manager1.initLoader(GET_DATA_LOADER_ID,bundle,getData);
                break;
            case R.id.highest_rated:
                Bundle bundle2 = new Bundle();
                bundle2.putInt(URL_KEY,2);
                manager1.restartLoader(GET_DATA_LOADER_ID,bundle2,getData);
                break;
            case R.id.menu_favourite:
                manager1.initLoader(IMAGE_LODER_ID,null,allImagesLoader);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
