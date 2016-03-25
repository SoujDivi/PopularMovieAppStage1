package com.example.soujanya.moiveapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by soujanya on 11/2/15.
 */
public class AppPreferences {



    Context context;
    SharedPreferences.Editor editor;
    public static SharedPreferences prefs;

    public AppPreferences(Context context){
        this.context = context;
        if (context != null) {
            prefs = context.getSharedPreferences(Constants.APP_KEY, 0);
            editor = prefs.edit();
        }
    }

    public void addString(String key, String value) {
        editor.putString(key, value);

    }

    public void removeString(String key) {
        editor.remove(key);

    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }
}
