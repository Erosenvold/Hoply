package com.example.test;

import android.content.Context;

import androidx.room.Room;

public final class LogSession {

    AppDatabase database = MainActivity.getDB();
    static int sessionID;

    public static void setSession(int userID){
        sessionID = userID;
    }
    public static boolean isLoggedIn(){
        if(sessionID != 0){
            return true;
        }else {
            return false;
        }
    }
    public static int getSessionID(){
        return sessionID;
    }


}
