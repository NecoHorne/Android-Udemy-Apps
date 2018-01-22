package com.necohorne.blog.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.necohorne.blog.Model.Blog;
import com.necohorne.blog.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by necoh on 2018/01/15.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Blog blog = blogList.get( position);
        String imageUrl = null;

        holder.title.setText(blog.getTitle());
        holder.description.setText(blog.getDescription());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimeStamp())).getTime());
        holder.timeStamp.setText(formattedDate);

        imageUrl = blog.getImage();

        //Picasso library to load image
        Picasso.with( context).load(imageUrl).into( holder.image);

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView timeStamp;
        public ImageView image;
        public String userID;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            title = view.findViewById(R.id.postTitleList );
            description = view.findViewById(R.id.postTextList);
            timeStamp = view.findViewById(R.id.timeStampList);
            image = view.findViewById(R.id.postImageListId);

            userID = null;

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next activity

                }
            } );

        }
    }
}
