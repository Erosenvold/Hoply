package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.test.tables.Comments;
import java.util.List;
//Data Access Object For Local DB Comments
@Dao
public interface CommentsDao {
    //Used for DB connection
    @Query("SELECT * FROM hoply_comment")
    public List<Comments> getAllComments();

    //Returns array with comment content for a given Post
    @Query("SELECT content FROM hoply_comment WHERE post_id=:postID ORDER BY stamp ASC")
    public String[] getCommentsFromPostID(int postID);

    //Returns array with user ID's for a given Post
    @Query("SELECT user_id FROM hoply_comment WHERE post_id =:postID ORDER BY stamp ASC")
    public String[] getCommentUserIDFromPostID(int postID);

    //Used to insert comment
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewComment(Comments comments);

    //DELETES ALL COMMENTS IN LOCAL DB!
    @Query("DELETE FROM hoply_comment")
    public void deleteAllComments();
}
