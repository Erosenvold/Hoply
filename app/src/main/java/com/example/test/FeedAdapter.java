package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    String headlines[], usernames[];
    int images[];
    Context context;

    //constructor for FeedAdapter, images are an int array because it can be tracked by enumeration.
    public FeedAdapter(Context context, String headlines[], String usernames[], int images[]){
        this.context = context;
        this.headlines = headlines;
        this.usernames = usernames;
        this.images = images;

    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed, parent, false);
        return new FeedViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.postHeadlineText.setText(headlines[position]);
        holder.postHeadlineText.setText(usernames[position]);
        holder.postImageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return headlines.length;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView postHeadlineText, postUsernameText;
        ImageView postImageView;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postHeadlineText = itemView.findViewById(R.id.postHeadlineText);
            postUsernameText = itemView.findViewById(R.id.postUsernameText);
            postImageView = itemView.findViewById(R.id.postImageView);
        }

    }
}
