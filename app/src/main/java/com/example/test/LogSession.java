package com.example.test;


//Saves User info for convenience and "logged in" Checks.
public final class LogSession {

    private static String sessionID;
    private static String sessionIMG;
    private static String sessionUsername;
    private static String sessionStamp;
    private static String sessionPassword;

    //Sets user Info.
    public static void setSession(String userID, String username, String userIMG, String userStamp, String password){
        sessionID = userID;
        sessionIMG = userIMG;
        sessionUsername = username;
        sessionStamp = userStamp;
        sessionPassword = password;
    }

    //Checks if user is logged in.
    public static boolean isLoggedIn(){
        if(sessionID != null){
            return true;
        }else {
            return false;
        }
    }

    //Gets User info.
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

    //Sets user image separately during profile edit.
    public static void setSessionIMG(String sessionIMG) {
        LogSession.sessionIMG = sessionIMG;
    }
}
