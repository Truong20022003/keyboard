package com.sticker.nicekeyboard.custom;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.sticker.nicekeyboard.service.ServiceIKeyboard;


public class SearchBox extends Activity {
    public static final String DATA_VOID = "data_void";
    private static final int RECOGNIZER_REQ_CODE = 1234;

    @Override 
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            startActivityForResult(new Intent("android.speech.action.RECOGNIZE_SPEECH"), RECOGNIZER_REQ_CODE);
        } catch (ActivityNotFoundException unused) {
//            Toast.makeText(this, getString(R.string.not_support), 0).show();
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.googlequicksearchbox")));
            } catch (ActivityNotFoundException unused2) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox")));
            }
        }
    }

    @Override 
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == RECOGNIZER_REQ_CODE) {
            Intent intent2 = new Intent(this, ServiceIKeyboard.class);
            intent2.putExtra(DATA_VOID, intent.getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
            startService(intent2);
            finish();
        }
    }
}
