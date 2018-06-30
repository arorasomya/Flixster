package com.example.arorasomya64.flixster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.arorasomya64.flixster.models.Config;
import com.example.arorasomya64.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {
    // the movie to display
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView releaseDate;
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the paramter key for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieTrailerActivity";
    AsyncHttpClient client;
    // the recycler view
    RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        releaseDate = (TextView) findViewById(R.id.releaseDate);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s", movie.getTitle()));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        String release = ("Release Date: " + movie.getReleaseDate());
        releaseDate.setText(release);

        // initialize the client
        client = new AsyncHttpClient();
        final Button btnTrailer = findViewById(R.id.btnTrailer);
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View btnTrailer) {
                String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";// set the request parameters
                RequestParams params = new RequestParams();
                params.put(API_KEY_PARAM, getString(R.string.api_key)); // api key, always required
                // execute a GET request expecting a JSON object response
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // load the results into movies list
                        try {
                            JSONArray result = response.getJSONArray("results");
                            Log.i(TAG, String.format("Loaded %s movies", result.length()));
                            String key_id = result.getJSONObject(0).getString("key");
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("key_id", key_id);
                            startActivity(intent);
                            Log.i(TAG, String.format("Loaded trailer"));
                        } catch (JSONException e) {

                        }
                    }


                });

            }


        });
    }
}
