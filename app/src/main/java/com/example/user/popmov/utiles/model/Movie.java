package com.example.user.popmov.utiles.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String title;
    private String image_path;
    private String rating;
    private String description;
    private String release_date;
    private int id;


    public Movie(String title, String image_path, String rating, String description, String release_date, int id) {
        this.title = title;
        this.image_path = image_path;
        this.rating = rating;
        this.description = description;
        this.release_date = release_date;
        this.id = id;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        image_path = in.readString();
        rating = in.readString();
        description = in.readString();
        release_date = in.readString();
        id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image_path);
        dest.writeString(rating);
        dest.writeString(description);
        dest.writeString(release_date);
        dest.writeInt(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", image_path='" + image_path + '\'' +
                ", rating='" + rating + '\'' +
                ", description='" + description + '\'' +
                ", release_date='" + release_date + '\'' +
                ", id=" + id +
                '}';
    }
}
