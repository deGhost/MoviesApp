package com.sarali.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sarali.moviesapp.models.Movie;

import org.w3c.dom.Text;

public class MovieOverview extends AppCompatActivity {
    private static final String TAG = "MovieOverview";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);
        ImageView ivPoster;
        TextView tvTitle, tvReleaseDate;
        TextView tvOverview;

        Movie selectedMovie = getIntent().getParcelableExtra("overview");
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvReleaseDate= findViewById(R.id.tvReleaseDate);
        tvOverview = findViewById(R.id.tvOverview);
        tvTitle.setText(selectedMovie.getTitle());
        tvReleaseDate.setText(selectedMovie.getReleaseDate());
        tvOverview.setText(selectedMovie.getOverview());
        Log.d(TAG, "onCreate: selectedMovie.getPosterPath() "+selectedMovie.getPosterPath());
        Glide.with(this)
                .asBitmap()
                .load(selectedMovie.getPosterPath())
                //.apply(new RequestOptions().override(800, 900))
                .into(ivPoster);
    }
}