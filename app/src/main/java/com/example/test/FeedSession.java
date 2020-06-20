package com.example.test;



public class FeedSession {



    private static int sessionOffset = 0;

    public static void resetSessionOffset() {
        FeedSession.sessionOffset = 0;
    }

    public static int getSessionOffset() {
        return sessionOffset;
    }

    public static void incSessionOffset() {
        sessionOffset = sessionOffset +10;
    }

    public static void decSessionOffset() {
        sessionOffset = sessionOffset -10;
    }



}
