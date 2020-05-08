package com.example.movieapp1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView nameofMovie,plotSynopsis,userRating,releaseDate;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //1.preuzimamo vrijednosti polja po id-u
        imageView= findViewById(R.id.image_header);
        nameofMovie=findViewById(R.id.title);
        plotSynopsis=findViewById(R.id.plotsynopsis);
        userRating=findViewById(R.id.userrating);
        releaseDate=findViewById(R.id.release_date);

        //2.preuzimamo vrijednosti iz apija
        Intent intentThatStartedThisActivity= getIntent();
        if(intentThatStartedThisActivity.hasExtra("original_title")){
            String thumbnail =getIntent().getExtras().getString("poster_path");
            String movieName =getIntent().getExtras().getString("original_title");
            String synopsis =getIntent().getExtras().getString("overview");
            String rating= getIntent().getExtras().getString("vote_average");
            String dateOfRelease = getIntent().getExtras().getString("release_date");

            //3.setujemo vrijednosti preuzete po id-u, sa vrijednostima koje smo preuzeli
            // iz apija u prethodnom koraku
            Glide.with(this)
                    .load(thumbnail)
                    .into(imageView);
            nameofMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        }
        else{
            Toast.makeText(this,"No API data",Toast.LENGTH_SHORT).show();
        }


    }
}
