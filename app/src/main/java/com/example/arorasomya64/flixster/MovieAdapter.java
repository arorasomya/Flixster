package com.example.arorasomya64.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arorasomya64.flixster.models.Config;
import com.example.arorasomya64.flixster.models.GlideApp;
import com.example.arorasomya64.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // list if movies
    ArrayList<Movie> movies;
    // config needed for image URLs
    Config config;
    // context for rendering
    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    // initialization with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    // creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds and inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        // build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        // if in portrait mode, load the poster image
        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            // load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());

        }

        // get the correct placeholder and imageview for the current orientation
        int placeHolderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;


        // load image using Glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(placeHolderId)
                .into(imageView);


    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }


    // create the viewholder as a static inner class
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;
        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.tvPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();
            // ensure that the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using the parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
