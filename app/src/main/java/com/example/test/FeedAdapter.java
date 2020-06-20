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

/**
 * This class contains functionality for adapting the RecyclerView in FeedActivity
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    /**
     * Class Attributes
     */
    private OnPostListener mOnNoteListener;
    String postText[], usernames[],shownText[];
    Bitmap images[];
    int postIds[];
    Context context;

    /**
     * Constructor creates a new FeedAdapter
     * @param context the context in which the adapter should be used.
     * @param postText a chronological list of post-content to display.
     * @param usernames a chronological list usernames to display.
     * @param images a chronological list of post-images to display.
     * @param postIds a chronological list of postId's.
     * @param onPostListener an interface that makes it possible to click on a post and get the correct postId.
     */
    public FeedAdapter(Context context, String postText[], String usernames[], Bitmap images[],int postIds[], OnPostListener onPostListener){
        this.context = context;
        this.postText = postText;
        this.usernames = usernames;
        this.images = images;
        this.postIds = postIds;
        this.mOnNoteListener = onPostListener;
        //if post-content contains 100+ characters show only 100 and 3 dots. This is purely cosmetic.
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

    /**
     * This method is called when the RecyclerView needs a new ViewHolder
     * @param parent the ViewGroup to create new Views in.
     * @param viewType the type of View to create in the ViewGroup
     * @return A new FeedViewHolder that contains a ViewGroup for instantiating feed-elements
     */
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed, parent, false);
        return new FeedViewHolder(view, mOnNoteListener);
    }

    /**
     * This method is called by the RecyclerView and updates the elements to reflect items
     * linked to the current position in the ViewHolder.
     * @param holder the FeedViewHolder to look at.
     * @param position the position to track.
     */
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.postHeadlineText.setText(shownText[position]);
        holder.postUsernameText.setText(usernames[position]);
        holder.postIdText.setText(String.valueOf(postIds[position]));
        holder.postImageView.setImageBitmap(images[position]);
    }

    /**
     * This method returns precisely the amount of posts to show.
     * @return the number of posts to show.
     */
    @Override
    public int getItemCount() {
        return postText.length;
    }

    /**
     * This inner class contains the functionality for the ViewHolder type FeedViewHolder
     */
    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * Class attributes
         */
        TextView postHeadlineText, postUsernameText, postIdText;
        ImageView postImageView;
        OnPostListener onPostListener;

        /**
         * Constructor creates a new FeedViewHolder
         * @param itemView the layout preset feed.xml
         * @param onPostListener the interface used to track the Id of a post.
         */
        public FeedViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            postHeadlineText = itemView.findViewById(R.id.postHeadlineText);
            postUsernameText = itemView.findViewById(R.id.postUsernameText);
            postImageView = itemView.findViewById(R.id.postImageView);
            postIdText = itemView.findViewById(R.id.postIdText);
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
        }

        /**
         * This method tells the onPostListener which post was clicked.
         * @param v the View that was clicked.
         */
        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    /**
     * the OnPostListener interface.
     */
    public interface OnPostListener{
        void onPostClick(int position);
    }
}
