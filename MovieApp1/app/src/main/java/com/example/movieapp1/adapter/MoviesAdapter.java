package com.example.movieapp1.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp1.DetailActivity;
import com.example.movieapp1.R;
import com.example.movieapp1.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>{
    private Context context;
    private List<Movie> movieList;

    public MoviesAdapter(Context context,List<Movie> movieList)
    {
        this.context=context;
        this.movieList=movieList;

    }

    //kreira se nosilac prikaza
    @NonNull
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MyViewHolder holder, int position) {
      //getOriginalTitle
       holder.title.setText(movieList.get(position).getOriginalTitle());

       //getVote
       String vote =Double.toString(movieList.get(position).getVoteAverage());
       holder.userrating.setText(vote);

       //preuzimanje slike pomoću glide biblioteke
        Glide.with(context)
                .load(movieList.get(position).getPosterPath())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title,userrating;
        public ImageView thumbnail;

        public MyViewHolder (View view){
            super(view);
            title=view.findViewById(R.id.title);
            userrating=view.findViewById(R.id.userrating);
            thumbnail=view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION)
                    {
                        Movie clickedDataItem=movieList.get(pos);
                        Intent intent=new Intent(context, DetailActivity.class);
                        intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(v.getContext(),"You clicked "+clickedDataItem.getOriginalTitle(),Toast.LENGTH_LONG).show();



                    }
                }
            });
        }

    }
}
