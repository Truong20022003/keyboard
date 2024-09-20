package com.sticker.nicekeyboard.custom;


import androidx.viewpager.widget.ViewPager;


public interface PageIndicator extends ViewPager.OnPageChangeListener {
    void notifyDataSetChanged();

    void setCurrentItem(int i);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setViewPager(ViewPager viewPager, int i, boolean z, int i2);

    void setViewPager(ViewPager viewPager, boolean z, int i);
}
