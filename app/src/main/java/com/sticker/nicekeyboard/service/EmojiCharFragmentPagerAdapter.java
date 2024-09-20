package com.sticker.nicekeyboard.service;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.sticker.nicekeyboard.R;
import com.sticker.nicekeyboard.custom.EmojiCharAdapter;
import com.sticker.nicekeyboard.custom.EmojiCharItemClicked;
import com.sticker.nicekeyboard.custom.EmojiObject;
import com.sticker.nicekeyboard.custom.IconPagerAdapter;

import java.util.List;


public class EmojiCharFragmentPagerAdapter extends PagerAdapter implements IconPagerAdapter {
    private EmojiCharAdapter adapter;
    private EmojiCharItemClicked emojiCharItemClickedCallback;
    private List<EmojiObject> listEmoji;
    private Context mContext;
    private Object[] tabTitles;

    @Override 
    public int getCount() {
        return 4;
    }

    @Override 
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public EmojiCharFragmentPagerAdapter(Context context, Object[] objArr, List<EmojiObject> list, EmojiCharItemClicked emojiCharItemClicked) {
        this.mContext = context;
        this.tabTitles = objArr;
        this.listEmoji = list;
        this.emojiCharItemClickedCallback = emojiCharItemClicked;
    }

    @Override 
    public Object getIconResId(int i) {
        return this.tabTitles[i % this.tabTitles.length];
    }

    @Override 
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        LayoutInflater.from(this.mContext);
        ViewGroup viewGroup2 = (ViewGroup) ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.layout_emoji_char, viewGroup, false);
        this.adapter = new EmojiCharAdapter(this.mContext, this.listEmoji);
        GridView gridView = (GridView) viewGroup2.findViewById(R.id.grvEmojiChar);
        gridView.setAdapter((ListAdapter) this.adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
            @Override 
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                EmojiCharFragmentPagerAdapter.this.emojiCharItemClickedCallback.emojiCharItemClickedListenner(((EmojiObject) adapterView.getItemAtPosition(i2)).getKeyEmoji());
            }
        });
        viewGroup.addView(viewGroup2);
        return viewGroup2;
    }

    @Override 
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override 
    public CharSequence getPageTitle(int i) {
        Drawable drawable = ContextCompat.getDrawable(this.mContext, Integer.parseInt(this.tabTitles[i].toString()));
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(new ImageSpan(drawable, 0), 0, 1, 33);
        return spannableString;
    }

    public void setDataChanges(List<EmojiObject> list) {
        this.adapter.setDataChanges(list);
    }
}
