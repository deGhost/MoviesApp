package com.sarali.moviesapp.movierecyclerview;

import android.content.Context;
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
import com.sarali.moviesapp.R;
import com.sarali.moviesapp.models.Movie;

import java.util.ArrayList;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder>{
    private static final String TAG = "MoviesRecyclerViewAdapt";
    ArrayList <Movie> moviesList = new ArrayList<Movie>();
    Context context;

    public MoviesRecyclerViewAdapter(Context context, ArrayList<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movieitem, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: has been called ");

        // loading the poster images
        Glide.with(context)
                .asBitmap()
                .load(moviesList.get(position).getPosterPath())
                .apply(new RequestOptions().override(800, 600))
                .into(holder.ivMovie);

        // setting movie titles & dates
        holder.tvTitle.setText(moviesList.get(position).getTitle());
        holder.tvDate.setText(moviesList.get(position).getReleaseDate());

        // setting an onClickListener for every item of moviesList
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item at position: "+position + "has been clicked!");
                Toast.makeText(context, "Loading overview...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        // Widgets forming the movie item
        RelativeLayout itemLayout;
        ImageView ivMovie;
        TextView tvTitle, tvDate;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);


            ivMovie = itemView.findViewById(R.id.ivMovie);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemLayout = itemView.findViewById(R.id.itemLayout);





        }
    }

}
