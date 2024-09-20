package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sticker.nicekeyboard.util.Constants;


public class ShareThemeOld {
    public static ShareThemeOld mIntance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private ShareThemeOld(Context context) {
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences("keyboard_share_name_old", 0);
    }

    public static ShareThemeOld getmIntance(Context context) {
        if (mIntance == null) {
            mIntance = new ShareThemeOld(context);
        }
        return mIntance;
    }

    public void saveData(ObjectTheme objectTheme) {
        this.mSharedPreferences.edit().putString(Constants.KEY_THEME, new Gson().toJson(objectTheme)).apply();
    }

    private ObjectTheme getObjectTheme() {
        String string = this.mSharedPreferences.getString(Constants.KEY_THEME, "");
        if (!string.equals("")) {
            return (ObjectTheme) new Gson().fromJson(string, new TypeToken<ObjectTheme>() {
                private ShareThemeOld this$0;
            }.getType());
        }
        saveData(new ObjectTheme());
        return new ObjectTheme();
    }

    public void putThemeNameConstance(String nameConstance) {
        this.mSharedPreferences.edit().putString("ThemeNameConstance", nameConstance).apply();
    }

    private String getThemeNameConstance() {
        return this.mSharedPreferences.getString("ThemeNameConstance", Constants.THEME_15);
    }

    public void setOldValue() {
        ShareTheme.getmIntance(mContext).saveData(getObjectTheme());
        ShareTheme.getmIntance(mContext).putThemeNameConstance(getThemeNameConstance());
    }
}
