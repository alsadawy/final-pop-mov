package com.example.user.popmov.utiles;



import com.example.user.popmov.utiles.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private final static String RESULTS = "results";
    final public static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500";
    public final static String TITLE = "title";
    public final static String POSTER = "poster_path";
    public final static String OVERVIEW = "overview";
    public final static String RATING = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String ID = "id";


    public static ArrayList<String> imageList(String json) {
            ArrayList<String> images = null;
            try {

                JSONArray jsonArray = makeJSONArray(json);
                images = new ArrayList<>();
                for (int i=0; i< jsonArray.length();i++){
                    JSONObject j = jsonArray.getJSONObject(i);
                    images.add(j.optString("poster_path"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  images;
        }


        public static Movie parseJSON(String json , int position){
            String title, poster,overview,rating,release_date;
            int id;
            Movie movie = null;
            try {

                JSONArray jsonArray = makeJSONArray(json);
                JSONObject movieJson = jsonArray.getJSONObject(position);

                title = movieJson.optString(TITLE);
                poster = movieJson.optString(POSTER);
                overview = movieJson.optString(OVERVIEW);
                rating = movieJson.optString(RATING);
                release_date = movieJson.optString(RELEASE_DATE);
                id = movieJson.optInt(ID);

                movie = new Movie(title,poster,rating,overview,release_date,id);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }

        public static ArrayList<String> getTrailers(String json){
            ArrayList<String> trailers = null;
            try {

                JSONArray jsonArray = makeJSONArray(json);

                trailers = new ArrayList<>();
                for(int i= 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getString("site").equals("YouTube")){
                        trailers.add(object.getString("key"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailers;
        }

        public static JSONArray makeJSONArray(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            return object.getJSONArray(RESULTS);
        }
        public static String getAuthor(JSONArray jsonArray , int position) throws JSONException {
            JSONObject jsonObject =jsonArray.getJSONObject(position);
            return jsonObject.getString("author");
        }
        public static String getReview(JSONArray jsonArray  , int position) throws JSONException {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            return jsonObject.getString("content");
        }
}
