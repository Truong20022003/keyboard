package com.sticker.nicekeyboard.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sticker.nicekeyboard.R;


public class CustomAKeyBoard extends RelativeLayout implements View.OnLongClickListener {
    private CallBack mCallBack;
    private String strNum;
    private String strText;
    private TextView tvChar;
    private TextView tvNumber;

    
    public interface CallBack {
        void doSomeThing(View view);
    }

    public CallBack getmCallBack() {
        return this.mCallBack;
    }

    public void setmCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public CustomAKeyBoard(Context context) {
        super(context);
    }

    public CustomAKeyBoard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public CustomAKeyBoard(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    @SuppressLint({"ResourceType"})
    private void init(Context context, AttributeSet attributeSet, int i) {
        setGravity(17);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AKeyBoard, i, 0);
        this.strText = obtainStyledAttributes.getString(R.styleable.AKeyBoard_strText);
        this.strNum = obtainStyledAttributes.getString(R.styleable.AKeyBoard_strNum);
        LayoutInflater.from(context).inflate(R.layout.layout_row_key_board, (ViewGroup) this, true);
        this.tvChar = (TextView) findViewById(R.id.tvChar);
        this.tvChar.setText(this.strText.toLowerCase());
        this.tvNumber = (TextView) findViewById(R.id.tvNumber);
        this.tvNumber.setText(this.strNum.toLowerCase());
        setOnLongClickListener(this);
        obtainStyledAttributes.recycle();
    }

    public String getText() {
        return this.tvChar.getText().toString();
    }

    public void setText(String str) {
        this.tvChar.setText(str);
        this.tvNumber.setVisibility(View.INVISIBLE);
    }

    public String getNumber() {
        if (this.tvNumber.getText().toString().equalsIgnoreCase("")) {
            return this.tvChar.getText().toString();
        }
        return this.tvNumber.getText().toString();
    }

    public void setUpperCase() {
        this.tvChar.setText(this.strText.toUpperCase());
        this.tvNumber.setText(this.strNum.toUpperCase());
        invalidate();
    }

    public void setLowerCase() {
        this.tvChar.setText(this.strText.toLowerCase());
        this.tvNumber.setText(this.strNum.toLowerCase());
    }

    public void setBackgroundHJHJ(int i, int i2) {
        StateListDrawable selectorBackgroundDrawable = selectorBackgroundDrawable(getResources().getDrawable(i), getResources().getDrawable(i2));
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(selectorBackgroundDrawable);
        } else {
            setBackgroundDrawable(selectorBackgroundDrawable);
        }
    }

    public void setBackgroundNull() {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public static StateListDrawable selectorBackgroundDrawable(Drawable drawable, Drawable drawable2) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, drawable2);
        stateListDrawable.addState(new int[0], drawable);
        return stateListDrawable;
    }

    public void setColorText(int i) {
        this.tvNumber.setTextColor(i);
        this.tvChar.setTextColor(i);
    }

    public void setTypeFaceText(Typeface typeface) {
        this.tvNumber.setTypeface(typeface);
        this.tvChar.setTypeface(typeface);
    }

    @Override 
    public boolean onLongClick(View view) {
        if (this.mCallBack == null) {
            return true;
        }
        this.mCallBack.doSomeThing(view);
        return true;
    }
}
