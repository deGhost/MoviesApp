package com.sarali.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sarali.moviesapp.models.Movie;
import com.sarali.moviesapp.movierecyclerview.MoviesRecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateViews();
    }

    // Fetching data from the Movie DB
    private void populateViews(){
        Movie movie = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie1 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie2 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie3 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie4 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie5 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie6 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        Movie movie7 = new Movie("the conjuring boo!","onjuring : Les Dossiers Warren ou La Conjuration au Québec (The Conjuring) est un film d'horreur américain réalisé par James Wan et sorti en 2013.", "https://fr.web.img6.acsta.net/pictures/210/025/21002526_20130430172022533.jpg", "2021" );
        moviesList.add(movie);
        moviesList.add(movie1);
        moviesList.add(movie2);
        moviesList.add(movie3);
        moviesList.add(movie4);
        moviesList.add(movie5);
        moviesList.add(movie6);
        moviesList.add(movie7);

        initRecyclerView();
    }
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvMovies);
        MoviesRecyclerViewAdapter moviesRecyclerView = new MoviesRecyclerViewAdapter(this,moviesList);
        recyclerView.setAdapter(moviesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}