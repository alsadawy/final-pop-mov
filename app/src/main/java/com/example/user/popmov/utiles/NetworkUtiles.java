package com.example.user.popmov.utiles;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

final public class NetworkUtiles {

    private NetworkUtiles() {
    }

    private final static String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private final static String API_PRAM = "api_key";
    private final static String SORT_PRAM = "sort_by";
    final static public String POPULARITY_SORT = "popularity.desc";
    final static public String HIGHEST_SORT = "vote_count.desc";
    private final static String ADULT_PRAM = "include_adult";
    private final static String ADULT = "false";
    public  final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";



    public static URL makeUrl(String apiKey, String sortQuery){

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_PRAM,apiKey)
                .appendQueryParameter(SORT_PRAM,sortQuery)
                .appendQueryParameter(ADULT_PRAM,ADULT).build();
        URL url = null;
        try {
             url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL makeTrailerURL(String apiKey, int movieID ){
        Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/"+movieID+"/videos")
                .buildUpon().appendQueryParameter(API_PRAM,apiKey).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL makeReviwesURL(String apiKey, int movieID){
        Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/" + movieID + "/reviews")
                .buildUpon().appendQueryParameter(API_PRAM,apiKey).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try{
        InputStream stream = connection.getInputStream();
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter("\\A");
        if(scanner.hasNext()){
           return scanner.next();
        }
        else{
            return null;
        }}
        finally {
            connection.disconnect();
        }
    }
}
