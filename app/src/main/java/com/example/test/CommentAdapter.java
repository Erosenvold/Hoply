package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class contains functionality for adapting the RecyclerView in ReadProfileActivity
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    /**
     * Class Attributes
     */
    Context context;
    String comments[];
    String usernames[];

    /**
     * Constructor creates a new CommentAdapter.
     * @param context the context in which the adapter should be used.
     * @param usernames a chronological list of users that have commented on a post.
     * @param comments a chronological list of texts that have been commented on a post.
     * precondition: make sure that the indices of users and comments match.
     */
    public CommentAdapter(Context context, String usernames[], String comments[]){
        this.context = context;
        this.comments = comments;
        this.usernames = usernames;
    }

    /**
     * This method is called when the RecyclerView needs a new ViewHolder
     * @param parent the ViewGroup to create new Views in.
     * @param viewType the type of View to create in the ViewGroup
     * @return A new CommentViewHolder that contains a ViewGroup for instantiating comment-elements
     */
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.commentfeed, parent, false);
        return new CommentViewHolder((view));
    }

    /**
     * This method is called by the RecyclerView and updates the elements to reflect the comments and users
     * linked to the current position in the ViewHolder.
     * @param holder the CommentViewHolder to look at.
     * @param position the position to track.
     */
    @Override
    public void onBindViewHolder (@NonNull CommentViewHolder holder, int position){
        holder.commentContent.setText(comments[position]);
        holder.commentUsernameText.setText(usernames[position]);
    }

    /**
     * This method returns precisely the amount of comments on a post.
     * @return the number of comments
     */
    @Override
    public int getItemCount(){
        return comments.length;
    }

    /**
     * This inner class contains the functionality for the ViewHolder type CommentViewHolder
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        /**
         * Class Attributes
         * 2 TextViews objects to be updated using the adapter.
         */
        TextView commentContent,commentUsernameText;

        /**
         * Constructor:
         * @param itemView the layout preset commentfeed.xml
         */
        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentUsernameText = itemView.findViewById(R.id.commentUsernameText);
        }
    }
}
