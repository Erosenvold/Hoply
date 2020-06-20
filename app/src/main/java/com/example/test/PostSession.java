package com.example.test;

import android.graphics.Bitmap;

/**
 * This class is a container for information about what post the user is currently accessing.
 */
public class PostSession {

    /**
     * Class Attributes
     */
    static int sessionPostID;
    static String sessionName;
    static String sessionUserID;
    static String sessionContent;
    static String sessionStamp;
    static String sessionGPS;
    static Bitmap sessionIMG;

    /**
     * Setter for the session.
     * @param postID the postID to log
     * @param name the username to log
     * @param userID the userID to log
     * @param content the posted text if added to log
     * @param stamp the time of creation to log
     * @param gps the location if added to log
     * @param img the posted image if added to log.
     */
    public static void setSession(int postID, String name, String userID, String content, String stamp, String gps, Bitmap img){

        sessionStamp = stamp;
        sessionContent = content;
        sessionName = name;
        sessionPostID = postID;
        sessionUserID = userID;
        sessionGPS = gps;
        sessionIMG = img;
    }

    /**
     * Getters
     */

    /**
     * @return the currently logged timestamp.
     */
    public static String getSessionStamp() {
        return sessionStamp;
    }

    /**
     * @return the currently logged posted content.
     */
    public static String getSessionContent() {
        return sessionContent;
    }

    /**
     * @return the currently logged userID.
     */
    public static String getSessionUserID() {
        return sessionUserID;
    }

    /**
     * @return the currently logged username.
     */
    public static String getSessionName() {
        return sessionName;
    }

    /**
     * @return the currently logged posted image.
     */
    public static Bitmap getSessionIMG() {
        return sessionIMG;
    }

    /**
     * @return the currently logged location.
     */
    public static String getSessionGPS() {
        return sessionGPS;
    }

    /**
     * @return the currently logged postID.
     */
    public static String getSessionPostID(){
        return sessionPostID+"";
    }
}
