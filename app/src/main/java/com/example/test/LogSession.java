package com.example.test;



public final class LogSession {


    private static String sessionID;
    private static String sessionIMG;
    private static String sessionUsername;
    private static String sessionStamp;
    private static String sessionPassword;

    public static void setSession(String userID, String username, String userIMG, String userStamp, String password){
        sessionID = userID;
        sessionIMG = userIMG;
        sessionUsername = username;
        sessionStamp = userStamp;
        sessionPassword = password;
    }
    public static boolean isLoggedIn(){
        return sessionID != null;
    }

    public static String getSessionPassword() {
        return sessionPassword;
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

    public static void setSessionIMG(String sessionIMG) {
        LogSession.sessionIMG = sessionIMG;
    }
}
