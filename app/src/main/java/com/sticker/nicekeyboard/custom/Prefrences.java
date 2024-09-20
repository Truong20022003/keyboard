package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.SharedPreferences;


public class Prefrences {

    
    public static SharedPreferences.Editor f66ed;
    public static SharedPreferences sharedPreferences;

    public static void setPref(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        f66ed = edit;
        edit.putInt(str, i);
        f66ed.apply();
    }

    public static int getPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(str, 0);
    }

    public static void setStringPref(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        f66ed = edit;
        edit.putString(str, str2);
        f66ed.apply();
    }

    public static String getStringPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getString(str, "Roboto-Regular.ttf");
    }

    public static void setBoolPref(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        f66ed = edit;
        edit.putBoolean(str, z);
        f66ed.apply();
    }

    public static boolean getBoolPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, true);
    }
}
