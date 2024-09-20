package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sticker.nicekeyboard.util.Constants;


public class ShareTheme {
    public static ShareTheme mIntance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private ShareTheme(Context context) {
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences("keyboard_share_name", 0);
    }

    public static ShareTheme getmIntance(Context context) {
        if (mIntance == null) {
            mIntance = new ShareTheme(context);
        }
        return mIntance;
    }

    public void saveData(ObjectTheme objectTheme) {
        String jsonObject= this.mSharedPreferences.getString(Constants.KEY_THEME, "");
        if (!jsonObject.equals(""))
            ShareThemeOld.getmIntance(mContext).saveData(new Gson().fromJson(jsonObject, new TypeToken<ObjectTheme>() {
            }.getType()));
        this.mSharedPreferences.edit().putString(Constants.KEY_THEME, new Gson().toJson(objectTheme)).apply();
    }

    public ObjectTheme getObjectTheme() {
        String string = this.mSharedPreferences.getString(Constants.KEY_THEME, "");
        if (!string.equals("")) {
            return (ObjectTheme) new Gson().fromJson(string, new TypeToken<ObjectTheme>() {
            }.getType());
        }
        saveData(new ObjectTheme());
        return new ObjectTheme();
    }

    public void putThemeNameConstance(String nameConstance) {
        ShareThemeOld.getmIntance(mContext).putThemeNameConstance(getThemeNameConstance());
        this.mSharedPreferences.edit().putString("ThemeNameConstance", nameConstance).apply();
    }

    public String getThemeNameConstance() {
        return this.mSharedPreferences.getString("ThemeNameConstance", Constants.THEME_15);
    }
}
