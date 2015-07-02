package com.example.parminder.searchonsocial;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by PARMINDER on 6/30/2015.
 */
public class sharedPreferences {
    public static final String def = "Nothing is Found";
    Context context;

    public sharedPreferences(Context applicationContext) {
        this.context = applicationContext;
    }

    //sharedPreferences
    public String getText() {
        SharedPreferences sharedPre = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchText = sharedPre.getString("user", def);
        return searchText;
    }
}
