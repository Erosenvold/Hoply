package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.test.tables.Comments;
import com.example.test.tables.Users;

import java.util.List;

@Dao
public interface CommentsDao {
    @Query("SELECT * FROM hoply_comment")
    public List<Comments> getAllComments();

    @Query("SELECT comment_content FROM hoply_comment WHERE post_id=:postID ORDER BY time_created ASC")
    public String[] getCommentsFromPostID(int postID);

    @Query("SELECT user_id FROM hoply_comment WHERE post_id =:postID ORDER BY time_created ASC")
    public String[] getCommentUserIDFromPostID(int postID);

    @Query("SELECT time_created FROM hoply_comment WHERE post_id=:postID ORDER BY time_created ASC")
    public long getTimeStampFromID(int postID);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewComment(Comments comments);
}
