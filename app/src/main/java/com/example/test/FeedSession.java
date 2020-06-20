package com.example.test;


//Stores info for feed.
public class FeedSession {

    //Declares and initializes Offset
    private static int sessionOffset = 0;

    //Resets Offset
    public static void resetSessionOffset() {
        FeedSession.sessionOffset = 0;
    }

    //Gets Offset
    public static int getSessionOffset() {
        return sessionOffset;
    }

    //Increase Offset by +10
    public static void incSessionOffset() {
        sessionOffset = sessionOffset +10;
    }

    //Decrease Offset by -10
    public static void decSessionOffset() {
        sessionOffset = sessionOffset -10;
    }



}
