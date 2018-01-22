package com.necohorne.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.necohorne.moviedirectory.Activities.MovieDetailActivity;
import com.necohorne.moviedirectory.Model.Movie;
import com.necohorne.moviedirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by necoh on 2018/01/10.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.movie_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        String posterlink = movie.getPoster();

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());

        Picasso.with(context)
                .load(posterlink)
                .placeholder( android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);

        holder.year.setText( movie.getYear());


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView year;
        public TextView type;
        public ImageView poster;


        public ViewHolder(View itemView, final Context ctx) {
            super( itemView );
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.movieTitleID);
            year = (TextView) itemView.findViewById(R.id.movieReleaseID);
            type = (TextView) itemView.findViewById(R.id.movieCatID);
            poster = (ImageView) itemView.findViewById(R.id.movieImageID);

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Movie movie = movieList.get( getAdapterPosition());
                    Intent intent = new Intent( context, MovieDetailActivity.class);
                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);

                    //Toast.makeText(context, "Row Tapped", Toast.LENGTH_LONG).show(); test if working.
                }
            } );
        }

        @Override
        public void onClick(View v) {

        }
    }
}
