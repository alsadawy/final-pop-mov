package com.example.user.popmov;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.popmov.data.MoviesContract;
import com.example.user.popmov.utiles.JsonUtils;
import com.example.user.popmov.utiles.NetworkUtiles;
import com.example.user.popmov.utiles.ReviewsAdapter;
import com.example.user.popmov.utiles.TrailerAdapter;
import com.example.user.popmov.utiles.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>
        , TrailerAdapter.TrailerClickHandler {

    private Movie movie;
    private ArrayList<String> trailers;
    private String apiKey;

    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.rating_tv)
    TextView rating;
    @BindView(R.id.description_tv)
    TextView description;
    @BindView(R.id.relase_tv)
    TextView release_date;
    @BindView(R.id.trailer_rv)
    RecyclerView trailer_rv;
    @BindView(R.id.reviews_rv)
    RecyclerView reviews_rv;
    @BindView(R.id.review_title)
    TextView reviwe_title_layout;
    @BindView(R.id.favorite)
    ImageView star;
    @BindView(R.id.poster_im)
    ImageView poster;

    private int moveID;
    private Loader<String[]> loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        apiKey = BuildConfig.ApiKey;

        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailer_rv.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            movie = intent.getParcelableExtra(MainActivity.MOVIE_INTENT_KEY);
            moveID = movie.getId();
            Picasso.get().load(JsonUtils.BASE_URL_IMAGE + movie.getImage_path()).into(poster);
            populateUI();
        } else {
            Toast.makeText(this, "Failed To load", Toast.LENGTH_SHORT).show();
        }


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isFavour(movie.getId())) {
                    star.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorAccent));
                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.MovieEntry.COLOUMN_MOVIE_ID, movie.getId());
                    values.put(MoviesContract.MovieEntry.COLOUMN_TITLE, movie.getTitle());
                    values.put(MoviesContract.MovieEntry.COLOUMN_IMAGE_PATH, movie.getImage_path());
                    values.put(MoviesContract.MovieEntry.COLOUMN_RATING, movie.getRating());
                    values.put(MoviesContract.MovieEntry.COLOUMN_DESCRIPTION, movie.getDescription());
                    values.put(MoviesContract.MovieEntry.COLOUMN_RELASE_DATE, movie.getRelease_date());

                    getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);


                } else {
                    star.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this, R.color.white));
                    String selection = MoviesContract.MovieEntry.COLOUMN_MOVIE_ID + " =?";
                    String[] args = {String.valueOf(movie.getId())};
                    Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId()))
                            .build();
                    getContentResolver().delete(uri, selection, args);

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager manager = getSupportLoaderManager();
        int LOADER_ID = 22;
        loader = manager.initLoader(LOADER_ID, null, this);
    }


    private void populateUI() {
        if (isFavour(movie.getId())) {
            star.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        title.setText(movie.getTitle());
        rating.append(movie.getRating());
        description.append(movie.getDescription());
        release_date.append(movie.getRelease_date());
    }

    boolean isFavour(int movieId) {
        String selection = MoviesContract.MovieEntry.COLOUMN_MOVIE_ID + " =?";
        String[] args = {String.valueOf(movieId)};
        Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null, selection, args, null);
        int count = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return count > 0;
    }


    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<String[]>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                String[] s = new String[2];
                try {
                    s[0] = NetworkUtiles.getResponse(NetworkUtiles.makeTrailerURL(apiKey, moveID));
                    s[1] = NetworkUtiles.getResponse(NetworkUtiles.makeReviwesURL(apiKey, moveID));
                    return s;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] s) {
        if (s != null && s.length > 0) {
            trailers = JsonUtils.getTrailers(s[0]);
            TrailerAdapter trailerAdapter = new TrailerAdapter(trailers, DetailsActivity.this);
            trailer_rv.setAdapter(trailerAdapter);
        }

        try {
            if(s != null) {
                JSONArray array = JsonUtils.makeJSONArray(s[1]);
                if (array.length() == 0) {
                    reviwe_title_layout.setVisibility(View.INVISIBLE);
                }
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(array);
                reviews_rv.setAdapter(reviewsAdapter);
                LinearLayoutManager linearManger = new LinearLayoutManager(DetailsActivity.this);
                reviews_rv.setLayoutManager(linearManger);
                reviews_rv.addItemDecoration(new DividerItemDecoration(reviews_rv.getContext(), linearManger.getOrientation()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NetworkUtiles.YOUTUBE_BASE_URL + trailers.get(position)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
