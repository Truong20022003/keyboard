package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.PopupWindow;

import com.sticker.nicekeyboard.R;


public class  MyKeyboardView extends KeyboardView {
    private static final float GESTURE_THRESHOLD_DIP = 10.0f;
    public static final int KEYCODE_EMOJI_8 = -1306;
    public static final int KEYCODE_LANGUAGE_SWITCH = -101;
    public static final int KEYCODE_OPTIONS = -100;
    private int GestureThreshold;
    private int bgABC;
    private int bgDelete;
    private int bgDot;
    private int bgEmoji;
    private int bgEnter;
    private int bgKeyNomal;
    private int bgKeyPress;
    private int bgMicro;
    private int bgShift;
    private int bgSpace;
    private int bgSpaceNomal;
    private int bgSpacePress;
    private int colorButton;
    private int colorKey;
    private int colorTextSpace;
    private Context context;
    private int imgDelete;
    private int imgEmoji;
    private int imgEnter;
    private int imgMicro;
    private int imgShift;
    private int imgDot;
    private int mBackground;
    private Context mContext;
    private Typeface mTypeface;
    private PopupWindow popup;
    private String selectedFont;
    private float scale = getContext().getResources().getDisplayMetrics().density;
    private int shift = R.mipmap.shift_white;

    @Override
    protected boolean onLongPress(Keyboard.Key key) {
        return true;
    }

    public MyKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.selectedFont = Prefrences.getStringPref(this.mContext, "selectedFont");
        AssetManager assets = context.getAssets();
        this.mTypeface = Typeface.createFromAsset(assets, "fonts/" + this.selectedFont);
    }

    public MyKeyboardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        this.selectedFont = Prefrences.getStringPref(this.mContext, "selectedFont");
        AssetManager assets = context.getAssets();
        this.mTypeface = Typeface.createFromAsset(assets, "fonts/" + this.selectedFont);
    }

    public void setBackgroundButton(int shift, int delete, int emoji, int micro, int ABC, int space, int dot, int enter) {
        this.bgShift = shift;
        this.bgDelete = delete;
        this.bgEmoji = emoji;
        this.bgMicro = micro;
        this.bgABC = ABC;
        this.bgSpace = space;
        this.bgDot = dot;
        this.bgEnter = enter;
        invalidate();
    }

    public void setBackgroundButton(int shift, int delete, int emoji, int micro, int ABC, int space, int dot, int enter, int swith) {
        this.bgShift = shift;
        this.bgDelete = delete;
        this.bgEmoji = emoji;
        this.bgMicro = micro;
        this.bgABC = ABC;
        this.bgSpace = space;
        this.bgDot = dot;
        this.bgEnter = enter;
        invalidate();
    }

    public void setBackgroundKey(int i, int i2) {
        this.bgKeyNomal = i;
        this.bgKeyPress = i2;
        invalidate();
    }

    public void setImageButton(int imgShift, int imgDot, int i, int i2, int i3, int i4) {
        this.imgShift = imgShift;
        this.imgDot = imgDot;
        this.imgDelete = i;
        this.imgEmoji = i2;
        this.imgMicro = i3;
        this.imgEnter = i4;
        invalidate();
    }

    public void colorText(int i, int i2) {
        this.colorKey = i;
        this.colorButton = i2;
        invalidate();
    }

    public void setBackgroundSpace(int i, int i2) {
        this.bgSpaceNomal = i;
        this.bgSpacePress = i2;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        invalidate();
    }

    public void setColorTextSpace(int i) {
        this.colorTextSpace = i;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        this.GestureThreshold = (int) ((this.scale * GESTURE_THRESHOLD_DIP) + 0.5f);
        paint.setTextSize((float) this.GestureThreshold);
        paint.setTypeface(this.mTypeface);
        paint.setColor(this.colorKey);
        Paint paint2 = new Paint();
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setTextSize((float) ((int) ((this.scale * 19.0f) + 0.5f)));
        paint2.setTypeface(this.mTypeface);
        paint2.setColor(this.colorKey);
        Paint paint3 = new Paint();
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setTextSize((float) ((int) ((this.scale * 19.0f) + 0.5f)));
        paint3.setTypeface(this.mTypeface);
        paint3.setColor(this.colorButton);
        Paint paint4 = new Paint();
        paint4.setTextAlign(Paint.Align.CENTER);
        paint4.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint4.setTextSize((float) ((int) ((this.scale * 19.0f) + 0.5f)));
        paint4.setTypeface(this.mTypeface);
        paint4.setColor(this.colorTextSpace);
        for (Keyboard.Key key : getKeyboard().getKeys()) {
            if (key.label != null) {
                if (key.codes[0] != 32 && key.codes[0] != -2 && key.codes[0] != 46) {
                    if (key.pressed) {
                        Drawable drawable = getResources().getDrawable(this.bgKeyPress);
                        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                        drawable.draw(canvas);
                    } else {
                        Drawable drawable2 = getResources().getDrawable(this.bgKeyNomal);
                        drawable2.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                        drawable2.draw(canvas);
                    }
                    if (!key.label.equals("q") && !key.label.equals("w") && !key.label.equals("e") && !key.label.equals("r") && !key.label.equals("t") && !key.label.equals("y") && !key.label.equals("u") && !key.label.equals("i") && !key.label.equals("o")) {
                        key.label.equals("p");
                    }
                    canvas.drawText(key.label.toString(), (float) (key.x + (key.width / 2)), ((float) key.y) + (((float) key.height) / 1.5f), paint2);
                } else if (key.codes[0] == -2) {
                    boolean z = key.pressed;
                    Drawable drawable3 = getResources().getDrawable(this.bgABC);
                    drawable3.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable3.draw(canvas);
                    canvas.drawText(key.label.toString(), (float) (key.x + (key.width / 2)), ((float) key.y) + (((float) key.height) / 1.5f), paint3);
                } else {
                    if (key.pressed) {
                        Drawable drawable4 = getResources().getDrawable(this.bgSpacePress);
                        drawable4.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                        drawable4.draw(canvas);
                    } else {
                        Drawable drawable5 = getResources().getDrawable(this.bgSpaceNomal);
                        drawable5.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                        drawable5.draw(canvas);
                    }
                    canvas.drawText(key.label.toString(), (float) (key.x + (key.width / 2)), ((float) key.y) + (((float) key.height) / 1.5f), paint4);
                }
            }
            if (key.icon != null) {
                int i = key.codes[0];
                if (key.codes[0] == 46) {
                    boolean z = key.pressed;
                    Drawable drawable3 = getResources().getDrawable(this.bgDot);
                    drawable3.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable3.draw(canvas);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgDot), (float) (key.x + (key.width / 3.6f)), (float) ((key.height / 1.75f) + key.y), paint);
                }
                if (key.codes[0] == -1) {
                    boolean z = key.pressed;
                    Drawable drawable3 = getResources().getDrawable(this.bgShift);
                    drawable3.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable3.draw(canvas);
                    //canvas.drawText(key.label.toString(), (float) (key.x + (key.width / 2)), ((float) key.y) + (((float) key.height) / 1.7f), paint3);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgShift), (float) (key.x + (key.width / 3.5f)), (float) ((key.height / 3.2) + key.y), paint);
                }
                if (i == -111) {
                    boolean z2 = key.pressed;
                    Drawable drawable6 = getResources().getDrawable(this.bgMicro);
                    drawable6.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable6.draw(canvas);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgMicro), ((float) key.x) + (((float) key.width) / 6f), (float) ((key.height / 3) + key.y), paint);
                } else if (i == -10) {
                    boolean z3 = key.pressed;
                    Drawable drawable7 = getResources().getDrawable(this.bgEmoji);
                    drawable7.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable7.draw(canvas);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgEmoji), ((float) key.x) + (((float) key.width) / 7.2f), (float) ((key.height / 3) + key.y), paint);
                } else if (i == -5) {
                    boolean z4 = key.pressed;
                    Drawable drawable8 = getResources().getDrawable(this.bgDelete);
                    drawable8.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable8.draw(canvas);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgDelete), (float) (key.x + (key.width / 3.8f)), (float) ((key.height / 3.2) + key.y), paint);
                } else if (i == 10) {
                    boolean z5 = key.pressed;
                    Drawable drawable9 = getResources().getDrawable(this.bgEnter);
                    drawable9.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable9.draw(canvas);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.imgEnter), ((float) key.x) + (((float) key.width) / 3.4f), (float) ((key.height / 3.4) + key.y), paint);
                }
            }
        }
    }

    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return super.onKeyLongPress(i, keyEvent);
    }
}
