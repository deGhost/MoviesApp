package com.sarali.moviesapp;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import com.sarali.moviesapp.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.sarali.moviesapp.models.Movie;
import com.sarali.moviesapp.movierecyclerview.MoviesRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // Endpoint to MovieDB API
    private String GET_url = BuildConfig.LINK+ BuildConfig.API_KEY+"&page=";
    
    // List of Movies fetched
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    
    // This object will serve as template for every movie we fetch from the endpoint
    private Movie movie;
    
    // For using pull to refresh
    private SwipeRefreshLayout refreshLayout;
    
    // For fetching the next page when bottom is reached
    private NestedScrollView nestedScroll;
    
    // The movies recyclerview adapter
    private MoviesRecyclerViewAdapter moviesRecyclerView;
    
    // Initial fetching page
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Retriving views from layout xml
        refreshLayout = findViewById(R.id.refreshLayout);
        nestedScroll = findViewById(R.id.nestedScroll);

        // Fetching data only if user is connected to internet
        if(!isConnected()) {
            Log.i(TAG, "onCreate: user not connected");
            promptForConnectivity(this);
        }
            Log.i(TAG, "onCreate: user connected");
            loadData(GET_url, page);

            // No option to fetch multiple pages at once, as the scroll reaches buttom, we fetch the next page
            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                        page = page +1;
                        loadData(GET_url, page);
                    }

                }
            });
            // Pull to refresh feature to update the list of trending movies
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    moviesRecyclerView.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            });



    }


    // Building the movies recyclerview
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvMovies);
        moviesRecyclerView = new MoviesRecyclerViewAdapter(this,moviesList);
        recyclerView.setAdapter(moviesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // Fetching data from the Movie DB
    private RequestQueue loadData(String GET_url, int page) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                GET_url+String.valueOf(page),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        HashMap<String, String> results= new HashMap<>();
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject currentJsonObj = jArray.getJSONObject(i);
                                Log.d(TAG, "onResponse: "+ currentJsonObj);

                                // Create an object i from jsonArray response "results"
                                movie = new Movie();
                                movie.setTitle(currentJsonObj.getString("title"));
                                String fullDate = currentJsonObj.getString("release_date");
                                movie.setReleaseDate(getYear(fullDate));
                                movie.setOverview(currentJsonObj.getString("overview"));
                                movie.setPosterPath("https://image.tmdb.org/t/p/w500"+currentJsonObj.getString("poster_path"));

                                // Adding objects to the movie list
                                moviesList.add(movie);
                                Log.d("ListOfMovies", "Movie "+ i+ " :"+ moviesList.get(i).getOverview() );
                                initRecyclerView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString() );
            }
        });
        requestQueue.add(stringRequest);

        return requestQueue;

    }

    // Checking internet status for both Wifi and mobile data
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo cellConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConnection != null && wifiConnection.isConnected()) || (cellConnection != null && cellConnection.isConnected())){
            Log.i(TAG, "isConnected: true");
            return true;
        }else{
            Log.i(TAG, "isConnected: false");
            return false;
        }
    }

    // Redirecting the user to turn on connectivity
    private void promptForConnectivity(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Internet is required for viewing trending movies!")
                .setCancelable(false)
                .setPositiveButton("Enable internet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                        System.exit(0);
                        finish();
                    }
                });
        builder.show();
    }

    // Formatting the release date of movies to return only Year
    private String getYear(String fullDate){
        String[] releaseDate = Iterables.toArray(
                Splitter
                        .fixedLength(4).
                        split(fullDate),
                String.class);
        return releaseDate[0];
    }
}
