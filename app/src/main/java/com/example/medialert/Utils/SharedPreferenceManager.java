package com.example.medialert.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    SharedPreferences nameSharedPreference;
    Context context;

    public SharedPreferenceManager(Context context) {
        this.context=context;
        nameSharedPreference=context.getSharedPreferences("NameSharedPreference",Context.MODE_PRIVATE);
    }

    public String getName(){
        return nameSharedPreference.getString("userName","");
    }
    public void setName(String name){
        SharedPreferences.Editor editor= nameSharedPreference.edit();
        editor.putString("userName",name);
    }



}
