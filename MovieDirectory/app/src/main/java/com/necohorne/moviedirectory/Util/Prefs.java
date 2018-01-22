package com.necohorne.moviedirectory.Util;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by necoh on 2018/01/10.
 */

public class Prefs {

    SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setSearch(String search){
        sharedPreferences.edit().putString("search", search).commit();
    }

    public String getSearch(){
        return sharedPreferences.getString("search", "Batman");
    }

}
