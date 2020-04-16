package com.example.test;

import android.content.Context;

import androidx.room.Room;

public final class LogSession {

    AppDatabase database = MainActivity.getDB();
    static String sessionID;

    public static void setSession(String userID){
        sessionID = userID;
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


}
