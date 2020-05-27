package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    String headlines[], usernames[];
    Bitmap images[];

    int postIds[];
    Context context;

    //constructor for FeedAdapter, images are an int array because it can be tracked by enumeration.
    public FeedAdapter(Context context, String headlines[], String usernames[], Bitmap images[],int postIds[]){
        this.context = context;
        this.headlines = headlines;
        this.usernames = usernames;
        this.images = images;
        this.postIds = postIds;
    }

    //Creates a new FeedViewHolder and instantiates feeds.
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed, parent, false);
        return new FeedViewHolder((view));
    }
    //Creates a positioning in the FeedViewHolder, populates the feeds with headlines, usernames and images.
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.postHeadlineText.setText(headlines[position]);
        holder.postUsernameText.setText(usernames[position]);
        holder.postIdText.setText(String.valueOf(postIds[position]));
        holder.postImageView.setImageBitmap(images[position]);
    }

    //returns the number of feeds.
    @Override
    public int getItemCount() {
        return headlines.length;
    }


    //nested class
    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView postHeadlineText, postUsernameText, postIdText;

        ImageView postImageView;

        // constructor
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postHeadlineText = itemView.findViewById(R.id.postHeadlineText);
            postUsernameText = itemView.findViewById(R.id.postUsernameText);
            postImageView = itemView.findViewById(R.id.postImageView);
            postIdText = itemView.findViewById(R.id.postIdText);

        }

    }
}
