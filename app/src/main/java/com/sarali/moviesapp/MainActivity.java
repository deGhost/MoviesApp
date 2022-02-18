package com.sarali.moviesapp;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.sarali.moviesapp.models.Movie;
import com.sarali.moviesapp.movierecyclerview.MoviesRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String GET_url = "https://api.themoviedb.org/3/movie/popular?api_key=11d1f5f13f5bd1df8b3150388623acc6";
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private HashMap<Integer,Movie>fetchedMovies = new HashMap<>();
    private ArrayList<String>moviesTest = new ArrayList<>();
    private Movie movieTest;
    private SwipeRefreshLayout refreshLayout;
    MoviesRecyclerViewAdapter moviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        refreshLayout = findViewById(R.id.refreshLayout);

        if(!isConnected()) {
            Log.i(TAG, "onCreate: user not connected");
            promptForConnectivity(this);
        }
            Log.i(TAG, "onCreate: user connected");
            loadData(GET_url);

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    moviesRecyclerView.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            });



    }


    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvMovies);
        moviesRecyclerView = new MoviesRecyclerViewAdapter(this,moviesList);
        recyclerView.setAdapter(moviesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // Fetching data from the Movie DB
    private RequestQueue loadData(String GET_url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                GET_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        HashMap<String, String> results= new HashMap<>();
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jArray.length(); i++) {
                                Gson gson = new Gson();
                                JSONObject currentJsonObj = jArray.getJSONObject(i);
                                Log.d(TAG, "onResponse: "+ currentJsonObj);

                                // Create an object i from jsonArray response "results"
                                movieTest = new Movie();
                                movieTest.setTitle(currentJsonObj.getString("title"));
                                movieTest.setReleaseDate(currentJsonObj.getString("release_date")); //TODO extract only year
                                movieTest.setOverview(currentJsonObj.getString("overview"));
                                movieTest.setPosterPath("https://image.tmdb.org/t/p/w500"+currentJsonObj.getString("poster_path"));

                                // Adding the object as String to a String arrayList for debugging purposes
                                moviesTest.add(String.valueOf(movieTest.getTitle()));

                                // Adding the object fetchedMovies
                                moviesList.add(movieTest);
                                Log.d("hashmapMovie", "Movie "+ i+ " :"+ moviesList.get(i).getOverview() );
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

}