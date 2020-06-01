package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    Context context;
    String comments[];
    String usernames[];

    public FeedAdapter(Context context, String usernames[], String comments[]){
        this.context = context;
        this.comments = comments;
        this.usernames = usernames;

    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed, parent, false);
        return new FeedAdapter.FeedViewHolder((view));

    }

    @Override
    public void onBindViewHolder (@NonNull FeedViewHolder holder, int position){


        


    }

}
