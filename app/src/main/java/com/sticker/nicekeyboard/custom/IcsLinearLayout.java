package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;


public class IcsLinearLayout extends LinearLayout {

    
    private static final int[] f67LL = {16843049, 16843561, 16843562};
    private static final int LL_DIVIDER = 0;
    private static final int LL_DIVIDER_PADDING = 2;
    private static final int LL_SHOW_DIVIDER = 1;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerWidth;
    private int mShowDividers;

    public IcsLinearLayout(Context context, int i) {
        super(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, f67LL, i, 0);
        setDividerDrawable(obtainStyledAttributes.getDrawable(0));
        this.mShowDividers = obtainStyledAttributes.getInteger(1, 0);
        obtainStyledAttributes.recycle();
    }

    @Override 
    protected void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        super.measureChildWithMargins(view, i, i2, i3, i4);
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
