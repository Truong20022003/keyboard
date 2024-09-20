package com.sticker.nicekeyboard.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.sticker.nicekeyboard.R;
import com.sticker.nicekeyboard.service.ServiceIKeyboard;


import java.util.ArrayList;
import java.util.List;


public class CandidateView extends View {
    private static final List<String> EMPTY_LIST = new ArrayList();
    private static final int MAX_SUGGESTIONS = 32;
    private static final int OUT_OF_BOUNDS = -1;
    private static final int SCROLL_PIXELS = 20;
    private static final int X_GAP = 60;
    private Rect mBgPadding;
    private int mColorNormal;
    private int mColorRecommended;
    private boolean mScrolled;
    private int mSelectedIndex;
    private Drawable mSelectionHighlight;
    private ServiceIKeyboard mService;
    private List<String> mSuggestions;
    private int mTargetScrollX;
    private int mTotalWidth;
    private boolean mTypedWordValid;
    private int mVerticalPadding;
    private int mTouchX = -1;
    private int[] mWordWidth = new int[32];
    private int[] mWordX = new int[32];
    private int mColorOther = -1;
    private Paint mPaint = new Paint();
    private GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CandidateView.this.mScrolled = true;
            int scrollX = (int) (((float) CandidateView.this.getScrollX()) + f);
            if (scrollX < 0) {
                scrollX = 0;
            }
            if (CandidateView.this.getWidth() + scrollX > CandidateView.this.mTotalWidth) {
                scrollX = (int) (((float) scrollX) - f);
            }
            CandidateView.this.mTargetScrollX = scrollX;
            CandidateView.this.scrollTo(scrollX, CandidateView.this.getScrollY());
            CandidateView.this.invalidate();
            return true;
        }
    });

    public CandidateView(Context context) {
        super(context);
        this.mSelectionHighlight = context.getResources().getDrawable(R.mipmap.btn_nomal_theme2);
        this.mSelectionHighlight.setState(new int[]{16842910, 16842908, 16842909, 16842919});
        Resources resources = context.getResources();
        setBackgroundColor(resources.getColor(R.color.bg_clay_midnight));
        this.mColorNormal = resources.getColor(R.color.candidate_normal);
        this.mColorRecommended = resources.getColor(R.color.candidate_other);
        this.mVerticalPadding = resources.getDimensionPixelSize(R.dimen.value_16);
        this.mPaint.setColor(this.mColorNormal);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize((float) resources.getDimensionPixelSize(R.dimen.text_20));
        this.mPaint.setStrokeWidth(0.0f);
        setHorizontalFadingEdgeEnabled(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
    }

    public void setService(ServiceIKeyboard serviceIKeyboard) {
        this.mService = serviceIKeyboard;
    }

    @Override
    public int computeHorizontalScrollRange() {
        return this.mTotalWidth;
    }

    @Override
    protected void onMeasure(int i, int i2) {
        int resolveSize = resolveSize(50, i);
        Rect rect = new Rect();
        this.mSelectionHighlight.getPadding(rect);
        setMeasuredDimension(resolveSize, resolveSize(rect.bottom + ((int) this.mPaint.getTextSize()) + this.mVerticalPadding + rect.top, i2));
    }


    @Override

    protected void onDraw(android.graphics.Canvas canvas) {

    }

    private void scrollToTarget() {
        int i;
        int scrollX = getScrollX();
        if (this.mTargetScrollX > scrollX) {
            i = scrollX + 20;
            if (i >= this.mTargetScrollX) {
                i = this.mTargetScrollX;
                requestLayout();
            }
        } else {
            i = scrollX - 20;
            if (i <= this.mTargetScrollX) {
                i = this.mTargetScrollX;
                requestLayout();
            }
        }
        scrollTo(i, getScrollY());
        invalidate();
    }

    @SuppressLint({"WrongCall"})
    public void setSuggestions(List<String> list, boolean z, boolean z2) {
        clear();
        if (list != null) {
            this.mSuggestions = new ArrayList(list);
        }
        this.mTypedWordValid = z2;
        scrollTo(0, 0);
        this.mTargetScrollX = 0;
        onDraw(null);
        invalidate();
        requestLayout();
    }

    public void clear() {
        this.mSuggestions = EMPTY_LIST;
        this.mTouchX = -1;
        this.mSelectedIndex = -1;
        invalidate();
    }

    @Override
    @RequiresApi(api = 16)
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        int action = motionEvent.getAction();
        int y = (int) motionEvent.getY();
        this.mTouchX = (int) motionEvent.getX();
        switch (action) {
            case 0:
                this.mScrolled = false;
                break;
            case 1:
                if (!this.mScrolled && this.mSelectedIndex >= 0) {
                    this.mService.pickSuggestionManually(this.mSelectedIndex);
                }
                this.mSelectedIndex = -1;
                removeHighlight();
                requestLayout();
                break;
            case 2:
                if (y <= 0 && this.mSelectedIndex >= 0) {
                    this.mService.pickSuggestionManually(this.mSelectedIndex);
                    this.mSelectedIndex = -1;
                    break;
                }
                break;
        }
        invalidate();
        return true;
    }

    @RequiresApi(api = 16)
    @SuppressLint({"WrongCall"})
    public void takeSuggestionAt(float f) {
        this.mTouchX = (int) f;
        onDraw(null);
        if (this.mSelectedIndex >= 0) {
            this.mService.pickSuggestionManually(this.mSelectedIndex);
        }
        invalidate();
    }

    private void removeHighlight() {
        this.mTouchX = -1;
        invalidate();
    }
}
