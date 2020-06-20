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

//Nicolai
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private OnPostListener mOnNoteListener;


    String postText[], usernames[],shownText[];
    Bitmap images[];

    int postIds[];
    Context context;

    //constructor for FeedAdapter, images are an int array because it can be tracked by enumeration.
    public FeedAdapter(Context context, String postText[], String usernames[], Bitmap images[],int postIds[], OnPostListener onPostListener){


        this.context = context;
        this.postText = postText;
        this.usernames = usernames;
        this.images = images;
        this.postIds = postIds;
        this.mOnNoteListener = onPostListener;
        int i = 0;
        shownText = new String[postText.length];
        for(String s: postText) {
            if (s.length() > 100) {
                this.shownText[i] = s.substring(0, 100) + "...";
            } else if (s.length() > 0) {
                this.shownText[i] = s;
            }
            i = i+1;
        }


    }

    //Creates a new FeedViewHolder and instantiates feeds.
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed, parent, false);
        return new FeedViewHolder(view, mOnNoteListener);
    }
    //Creates a positioning in the FeedViewHolder, populates the feeds with postText, usernames and images.
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.postHeadlineText.setText(shownText[position]);
        holder.postUsernameText.setText(usernames[position]);
        holder.postIdText.setText(String.valueOf(postIds[position]));
        holder.postImageView.setImageBitmap(images[position]);
    }

    //returns the number of feeds.
    @Override
    public int getItemCount() {
        return postText.length;
    }


    //nested class
    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView postHeadlineText, postUsernameText, postIdText;

        ImageView postImageView;

        OnPostListener onPostListener;

        // constructor
        public FeedViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            postHeadlineText = itemView.findViewById(R.id.postHeadlineText);
            postUsernameText = itemView.findViewById(R.id.postUsernameText);
            postImageView = itemView.findViewById(R.id.postImageView);
            postIdText = itemView.findViewById(R.id.postIdText);

            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onPostListener.onPostClick(getAdapterPosition());

        }
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }
}
