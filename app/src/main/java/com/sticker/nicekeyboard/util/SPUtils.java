package com.sticker.nicekeyboard.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class SPUtils {
    public static final String SHARED_PREFS_NAME = "Wifi Manager";
    public static final String STORAGE = "STORAGE";


    public static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(str, str2);
    }

    public static void setLong(Context context, String str, long i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putLong(str, i);
        edit.apply();
    }

    public static long getLong(Context context, String str, long i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getLong(str, i);
    }

    public static void setInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt(str, i);
    }

    public static void setBoolean(Context context, String str, boolean b) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putBoolean(str, b);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean b) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getBoolean(str, b);
    }
    public static boolean checkStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        }
    }
}
