package com.zeyaly.extractor.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    Context cntx;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public final String KEY_Points_ID = "points";
    public final String KEY_User_Name = "userName";
    public final String KEY_Email_ID = "emilId";
    public final String KEY_Mobile_NO = "mobileNo";
    public final String In = "in";

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        this.cntx = cntx;
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        editor = prefs.edit();
    }


    public String getKEYPoints() {
        String userID = prefs.getString(KEY_Points_ID, "0");
        return userID;
    }

    public void setKEYPoints(String userID) {
        editor.putString(KEY_Points_ID, userID);
        editor.apply();
    }
    public String getKEY_User_Name() {
        String userID = prefs.getString(KEY_User_Name, "");
        return userID;
    }

    public void setKEY_User_Name(String userID) {
        editor.putString(KEY_User_Name, userID);
        editor.apply();
    }
    public String getKEY_Email_ID() {
        String userID = prefs.getString(KEY_Email_ID, "");
        return userID;
    }

    public void setKEY_Email_ID(String userID) {
        editor.putString(KEY_Email_ID, userID);
        editor.apply();
    }
 public String getKEY_Mobile_NO() {
        String userID = prefs.getString(KEY_Mobile_NO, "");
        return userID;
    }

    public void setKEY_Mobile_NO(String userID) {
        editor.putString(KEY_Mobile_NO, userID);
        editor.apply();
    }

    public void setLogin(String userID) {
        editor.putString(In, userID);
        editor.apply();
    }

    public String getLogin() {
        String userID = prefs.getString(In, "0");
        return userID;
    }

}
