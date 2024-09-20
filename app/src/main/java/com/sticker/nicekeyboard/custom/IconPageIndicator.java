package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.sticker.nicekeyboard.R;


public class IconPageIndicator extends HorizontalScrollView implements PageIndicator {
    private Context context;
    private boolean isText;
    private Runnable mIconSelector;
    private final IcsLinearLayout mIconsLayout;
    private ViewPager.OnPageChangeListener mListener;
    private int mSelectedIndex;
    private ViewPager mViewPager;
    private MethodUltilts methodUltilts;

    public IconPageIndicator(Context context) {
        this(context, null);
        this.context = context;
        this.methodUltilts = new MethodUltilts(context);
    }

    public IconPageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.methodUltilts = new MethodUltilts(context);
        setHorizontalScrollBarEnabled(false);
        this.mIconsLayout = new IcsLinearLayout(context, R.attr.vpiIconPageIndicatorStyle);
        addView(this.mIconsLayout, new LayoutParams(-1, this.methodUltilts.dip2px(context, 30.0f), 17));
        this.mIconsLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void animateToIcon(int i) {
        final View childAt = this.mIconsLayout.getChildAt(i);
        if (this.mIconSelector != null) {
            removeCallbacks(this.mIconSelector);
        }
        this.mIconSelector = new Runnable() { 
            @Override 
            public void run() {
                IconPageIndicator.this.smoothScrollTo(childAt.getLeft() - ((IconPageIndicator.this.getWidth() - childAt.getWidth()) / 2), 0);
                IconPageIndicator.this.mIconSelector = null;
            }
        };
        post(this.mIconSelector);
    }

    @Override 
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIconSelector != null) {
            post(this.mIconSelector);
        }
    }

    @Override 
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mIconSelector != null) {
            removeCallbacks(this.mIconSelector);
        }
    }

    @Override 
    public void onPageScrollStateChanged(int i) {
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(i);
        }
    }


    @Override 
    public void onPageScrolled(int i, float f, int i2) {
        if (this.mListener != null) {
            this.mListener.onPageScrolled(i, f, i2);
        }
    }

    @Override 
    public void onPageSelected(int i) {
        setCurrentItem(i);
        if (this.mListener != null) {
            this.mListener.onPageSelected(i);
        }
    }

    @Override 
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mListener = onPageChangeListener;
    }

    @Override 
    public void setViewPager(ViewPager viewPager, boolean z, int i) {
        this.isText = z;
        if (this.mViewPager != null) {
            this.mViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        this.mViewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        notifyDataSetChanged(i);
    }

    @Override 
    public void setViewPager(ViewPager viewPager, int i, boolean z, int i2) {
        setViewPager(viewPager, z, i2);
        setCurrentItem(i);
    }

    public void notifyDataSetChanged(int i) {
        this.mIconsLayout.removeAllViews();
        IconPagerAdapter iconPagerAdapter = (IconPagerAdapter) this.mViewPager.getAdapter();
        int count = iconPagerAdapter.getCount();
        int i2 = this.methodUltilts.getDisplayMetrics(this.context).widthPixels / i;
        for (int i3 = 0; i3 < count; i3++) {
            if (this.isText) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i2, this.methodUltilts.dip2px(this.context, 30.0f));
                TextView textView = new TextView(getContext(), null, R.attr.vpiIconPageIndicatorStyle);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setGravity(17);
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(2, 14.0f);
                textView.setText(iconPagerAdapter.getIconResId(i3).toString());
                this.mIconsLayout.addView(textView);
            } else {
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(i2, this.methodUltilts.dip2px(this.context, 30.0f));
                ImageView imageView = new ImageView(getContext(), null, R.attr.vpiIconPageIndicatorStyle);
                imageView.setLayoutParams(layoutParams2);
                imageView.setPadding(this.methodUltilts.dip2px(this.context, 5.0f), this.methodUltilts.dip2px(this.context, 5.0f), this.methodUltilts.dip2px(this.context, 5.0f), this.methodUltilts.dip2px(this.context, 5.0f));
                imageView.setImageResource(Integer.parseInt(iconPagerAdapter.getIconResId(i3).toString()));
                this.mIconsLayout.addView(imageView);
            }
        }
        if (this.mSelectedIndex > count) {
            this.mSelectedIndex = count - 1;
        }
        setCurrentItem(this.mSelectedIndex);
        requestLayout();
    }


    @Override 
    public void setCurrentItem(int i) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mSelectedIndex = i;
        this.mViewPager.setCurrentItem(i);
        int childCount = this.mIconsLayout.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            if (i2 == this.mSelectedIndex) {
                this.mIconsLayout.getChildAt(this.mSelectedIndex).setBackgroundColor(Color.parseColor("#2a2a2a"));
            } else {
                this.mIconsLayout.getChildAt(i2).setBackgroundColor(Color.parseColor("#121212"));
            }
        }
    }


    @Override 
    public void notifyDataSetChanged() {
        this.mIconsLayout.removeAllViews();
        IconPagerAdapter iconPagerAdapter = (IconPagerAdapter) this.mViewPager.getAdapter();
        int count = iconPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext(), null, R.attr.vpiIconPageIndicatorStyle);
            imageView.setImageResource(((Integer) iconPagerAdapter.getIconResId(i)).intValue());
            imageView.setTag(Integer.valueOf(i));
            imageView.setOnClickListener(new OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    IconPageIndicator.this.mViewPager.setCurrentItem(Integer.parseInt(view.getTag().toString()));
                }
            });
            this.mIconsLayout.addView(imageView);
        }
        if (this.mSelectedIndex > count) {
            this.mSelectedIndex = count - 1;
        }
        setCurrentItem(this.mSelectedIndex);
        requestLayout();
    }


}
