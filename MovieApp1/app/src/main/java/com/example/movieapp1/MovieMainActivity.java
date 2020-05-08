package com.example.movieapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.movieapp1.adapter.MoviesAdapter;
import com.example.movieapp1.api.Client;
import com.example.movieapp1.api.Service;
import com.example.movieapp1.model.Movie;
import com.example.movieapp1.model.MoviesResponse;
//import com.google.gson.internal.GsonBuildConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movieList;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String LOG_TAG=MoviesAdapter.class.getName();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);


        initViews();

        swipeRefreshLayout= findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_red_dark);
        swipeRefreshLayout.animate();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MovieMainActivity.this,"Movie refereshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();

       //get recyclerview by id
        recyclerView=findViewById(R.id.recycler_view);
        movieList=new ArrayList<>();
        moviesAdapter=new MoviesAdapter(this,movieList);

        //define recyclerview from activity_main
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);;
        moviesAdapter.notifyDataSetChanged();

        loadJSON();

    }

    private  void loadJSON(){
        try {


            //checks if the api key is on
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please obtain API key firstly from themoviedb.org",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }


                Client client=new Client();
                Service api_service=client.getClient().create(Service.class);
                Call<MoviesResponse> call= api_service.getTopRatedMovies((BuildConfig.THE_MOVIE_DB_API_TOKEN));
                //send request and notifcations about callback about his answer
                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies =response.body().getResults();
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                       // recyclerView.smoothScrollToPosition(0);
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                        Toast.makeText(MovieMainActivity.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                    }
                });

        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(MovieMainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.menu_settings:
               return true;
           default: return super.onOptionsItemSelected(item);
       }
    }
*/
}
