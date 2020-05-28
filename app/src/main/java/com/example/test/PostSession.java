package com.example.test;

public final class PostSession {

    static int sessionID;

    public static void setSession(int postId){
        sessionID = postId;
    }

    public static int getSessionID(){
        return sessionID;
    }


}
