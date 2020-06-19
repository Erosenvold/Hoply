package com.example.test;

import android.graphics.Bitmap;
//Nicolai
public class PostSession {

    static int sessionPostID;
    static String sessionName;
    static String sessionUserID;
    static String sessionContent;
    static String sessionStamp;
    static String sessionGPS;
    static Bitmap sessionIMG;


    public static void setSession(int postId, String name, String userID, String content, String stamp, String gps, Bitmap img){

        sessionStamp = stamp;
        sessionContent = content;
        sessionName = name;
        sessionPostID = postId;
        sessionUserID = userID;
        sessionGPS = gps;
        sessionIMG = img;

    }

    public static String getSessionStamp() {
        return sessionStamp;
    }

    public static String getSessionContent() {
        return sessionContent;
    }

    public static String getSessionUserID() {
        return sessionUserID;
    }

    public static String getSessionName() {
        return sessionName;
    }

    public static Bitmap getSessionIMG() {
        return sessionIMG;
    }

    public static String getSessionGPS() {
        return sessionGPS;
    }

    public static String getSessionPostID(){

        return sessionPostID+"";
    }


}
