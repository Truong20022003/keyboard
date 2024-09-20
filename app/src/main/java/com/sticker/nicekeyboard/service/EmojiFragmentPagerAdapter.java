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
import com.sticker.nicekeyboard.custom.EmojiClickCallback;
import com.sticker.nicekeyboard.custom.EmojiObject;
import com.sticker.nicekeyboard.custom.IconPagerAdapter;
import com.sticker.nicekeyboard.custom.ViewPagerEmojiAdapter;

import java.util.List;


public class EmojiFragmentPagerAdapter extends PagerAdapter implements IconPagerAdapter {
    private ViewPagerEmojiAdapter adapter;
    private EmojiClickCallback emojiClickCallback;
    private boolean isEmoticons;
    private List<EmojiObject> listEmoji;
    private Context mContext;
    private Object[] tabTitles;

    @Override 
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public EmojiFragmentPagerAdapter(Context context, Object[] objArr, List<EmojiObject> list, EmojiClickCallback emojiClickCallback, boolean z) {
        this.mContext = context;
        this.tabTitles = objArr;
        this.listEmoji = list;
        this.emojiClickCallback = emojiClickCallback;
        this.isEmoticons = z;
    }

    @Override 
    public Object getIconResId(int i) {
        return this.tabTitles[i % this.tabTitles.length];
    }

    @Override 
    public int getCount() {
        return this.tabTitles.length;
    }

    @Override 
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        LayoutInflater.from(this.mContext);
        ViewGroup viewGroup2 = (ViewGroup) ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.layout_viewpager_emoji, viewGroup, false);
        this.adapter = new ViewPagerEmojiAdapter(this.listEmoji, this.mContext);
        GridView gridView = (GridView) viewGroup2.findViewById(R.id.grvEmoji);
        gridView.setAdapter((ListAdapter) this.adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
            @Override 
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                if (EmojiFragmentPagerAdapter.this.isEmoticons) {
                    EmojiFragmentPagerAdapter.this.emojiClickCallback.emojiClickedListenner("", ((EmojiObject) adapterView.getItemAtPosition(i2)).getKeyEmoji());
                } else {
                    EmojiFragmentPagerAdapter.this.emojiClickCallback.emojiClickedListenner(((EmojiObject) adapterView.getItemAtPosition(i2)).getNameEmoji(), ((EmojiObject) adapterView.getItemAtPosition(i2)).getKeyEmoji());
                }

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

    public void setDataChanges(List<EmojiObject> list, boolean z) {
        this.isEmoticons = z;
        this.adapter.dataChanges(list);
    }
}
