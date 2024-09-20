package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;


public class MyKeyboard extends Keyboard {
    private Key mEnterKey;
    private Key mLanguageSwitchKey;
    private Key mModeChangeKey;
    private Key mSavedLanguageSwitchKey;
    private Key mSavedModeChangeKey;
    private Key mSpaceKey;

    
    class LatinKey extends Key {
        public LatinKey(Resources resources, Row row, int i, int i2, XmlResourceParser xmlResourceParser) {
            super(resources, row, i, i2, xmlResourceParser);
        }

        @Override 
        public boolean isInside(int i, int i2) {
            if (this.codes[0] == -3) {
                i2 -= 10;
            }
            return super.isInside(i, i2);
        }
    }

    public MyKeyboard(Context context, int i) {
        super(context, i);
    }

    public MyKeyboard(Context context, int i, CharSequence charSequence, int i2, int i3) {
        super(context, i, charSequence, i2, i3);
    }

    @Override 
    protected Key createKeyFromXml(Resources resources, Row row, int i, int i2, XmlResourceParser xmlResourceParser) {
        LatinKey latinKey = new LatinKey(resources, row, i, i2, xmlResourceParser);
        if (((Key) latinKey).codes[0] == 10) {
            this.mEnterKey = latinKey;
        } else if (((Key) latinKey).codes[0] == 32) {
            this.mSpaceKey = latinKey;
        } else if (((Key) latinKey).codes[0] == -2) {
            this.mModeChangeKey = latinKey;
            this.mSavedModeChangeKey = new LatinKey(resources, row, i, i2, xmlResourceParser);
        } else if (((Key) latinKey).codes[0] == -101) {
            this.mLanguageSwitchKey = latinKey;
            this.mSavedLanguageSwitchKey = new LatinKey(resources, row, i, i2, xmlResourceParser);
        }
        return latinKey;
    }
}
