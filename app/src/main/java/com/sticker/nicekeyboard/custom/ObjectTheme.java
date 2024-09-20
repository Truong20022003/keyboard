package com.sticker.nicekeyboard.custom;

import android.content.Context;

import com.sticker.nicekeyboard.util.Constants;


public class ObjectTheme {
    private BackgroundKeyboard backgroundKeyboard;
    private ItemKeyBroad itemKeyBroad;
    private Context mContext;

    public ObjectTheme() {
        this.itemKeyBroad = new ItemKeyBroad(Constants.COLOR_TEXT_SMALL_DEFAULT, Constants.COLOR_TEXT_MAIN_DEFAULT);
        this.backgroundKeyboard = new BackgroundKeyboard(false, false, Constants.COLOR_BG_DEFAULT, "", Constants.THEME_IOS_DEFAULT, false);
    }

    public ObjectTheme(ItemKeyBroad itemKeyBroad, BackgroundKeyboard backgroundKeyboard) {
        this.itemKeyBroad = itemKeyBroad;
        this.backgroundKeyboard = backgroundKeyboard;
    }

    public ItemKeyBroad getItemKeyBroad() {
        return this.itemKeyBroad;
    }

    public void setItemKeyBroad(ItemKeyBroad itemKeyBroad) {
        this.itemKeyBroad = itemKeyBroad;
    }

    public BackgroundKeyboard getBackgroundKeyboard() {
        return this.backgroundKeyboard;
    }

    public void setBackgroundKeyboard(BackgroundKeyboard backgroundKeyboard) {
        this.backgroundKeyboard = backgroundKeyboard;
    }
}
