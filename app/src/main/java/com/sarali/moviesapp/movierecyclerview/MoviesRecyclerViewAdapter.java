package com.sarali.moviesapp.movierecyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sarali.moviesapp.MovieOverview;
import com.sarali.moviesapp.R;
import com.sarali.moviesapp.models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder> {
    private static final String TAG = "MoviesRecyclerViewAdapt";
    
    // List of Movies, holds the data
    ArrayList <Movie> moviesList = new ArrayList<Movie>();
    Context context;

    // Recyclerview constructor
    public MoviesRecyclerViewAdapter(Context context, ArrayList<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;

    }

    // Inflating the view with a single movie item layout
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movieitem, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    // Loading at start the posters of movies using Glide
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: has been called ");

        // loading the poster images
        Glide.with(context)
                .asBitmap()
                .load(moviesList.get(position).getPosterPath())
                .apply(new RequestOptions().override(800, 600))
                .into(holder.ivMovie);

        // setting movie titles, dates and overview
        holder.tvTitle.setText(moviesList.get(position).getTitle());
        holder.tvDate.setText(moviesList.get(position).getReleaseDate());
        holder.tvBrief.setText(moviesList.get(position).getOverview());

        // setting an onClickListener for every item of moviesList
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item at position: "+position + "has been clicked!");
                Toast.makeText(context, "Loading overview...", Toast.LENGTH_SHORT).show();

                Intent overview = new Intent(context, MovieOverview.class);
                overview.putExtra("overview", moviesList.get(position));
                context.startActivity(overview);

            }
        });
    }

    // Returning this allows to display n items inside the recyclerview list
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    // Holds all the widgets composing a single item of the movies list
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        // Widgets forming the movie item
        RelativeLayout itemLayout;
        ImageView ivMovie;
        TextView tvTitle, tvDate, tvBrief;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);


            ivMovie = itemView.findViewById(R.id.ivMovie);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBrief= itemView.findViewById(R.id.tvBrief);
            itemLayout = itemView.findViewById(R.id.itemLayout);





        }
    }

}
