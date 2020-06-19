package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    String comments[];
    String usernames[];


    public CommentAdapter(Context context, String usernames[], String comments[]){
        this.context = context;
        this.comments = comments;
        this.usernames = usernames;

        for(String s : usernames){
            System.out.println("Names: "+ s);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.commentfeed, parent, false);
        return new CommentViewHolder((view));

    }

    @Override
    public void onBindViewHolder (@NonNull CommentViewHolder holder, int position){
        holder.commentContent.setText(comments[position]);
        holder.commentUsernameText.setText(usernames[position]);
    }
    @Override
    public int getItemCount(){return comments.length;}

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView commentContent,commentUsernameText;
        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentUsernameText = itemView.findViewById(R.id.commentUsernameText);
        }

    }

}
