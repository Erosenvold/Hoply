package com.example.test;

import android.content.Context;

import androidx.room.Room;

public final class LogSession {

    AppDatabase database = MainActivity.getDB();
    static String sessionID;
    static String sessionIMG;
    static String sessionUsername;
    static String sessionStamp;

    public static void setSession(String userID, String username, String userIMG, String userStamp){
        sessionID = userID;
        sessionIMG = userIMG;
        sessionUsername = username;
        sessionStamp = userStamp;
    }
    public static boolean isLoggedIn(){
        if(sessionID != null){
            return true;
        }else {
            return false;
        }
    }
    public static String getSessionID(){
        return sessionID;
    }

    public static String getSessionIMG(){
        return  sessionIMG;
    }

    public static String getSessionUsername() {
        return sessionUsername;
    }

    public static String getSessionStamp() {
        return sessionStamp;
    }
}
