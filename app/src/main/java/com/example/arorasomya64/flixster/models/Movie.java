package com.example.arorasomya64.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // annotation indicates that class is parcelable
public class Movie {
    // values from the API
    String title;
    String overview;
    String posterPath; // only the path
    String backdropPath;
    Double voteAverage;
    String releaseDate;


    public Double getVoteAverage() {
        return voteAverage;
    }

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        releaseDate = object.getString("release_date");
    }

    public Movie() {} // no argument, empty constructor required for parceler

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() { return releaseDate; }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
