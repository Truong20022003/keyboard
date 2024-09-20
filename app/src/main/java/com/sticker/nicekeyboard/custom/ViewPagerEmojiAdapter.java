package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.sticker.nicekeyboard.R;


import java.util.List;


public class ViewPagerEmojiAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<EmojiObject> listEmoji;
    private Context mContext;

    @Override 
    public long getItemId(int i) {
        return (long) i;
    }

    
    class ViewHolder {

        
        ImageView f61a;
        private ViewPagerEmojiAdapter this$0;

        ViewHolder(ViewPagerEmojiAdapter viewPagerEmojiAdapter) {
        }
    }

    public ViewPagerEmojiAdapter(List<EmojiObject> list, Context context) {
        this.listEmoji = list;
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override 
    public int getCount() {
        return this.listEmoji.size();
    }

    @Override 
    public Object getItem(int i) {
        return this.listEmoji.get(i);
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder(this);
        if (view == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_emoji, viewGroup, false);
            viewHolder.f61a = (ImageView) view.findViewById(R.id.imgGridEmoji);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RequestManager with = Glide.with(this.mContext);
        with.load("file:///android_asset/" + this.listEmoji.get(i).getKeyEmoji()).into(viewHolder.f61a);
        return view;
    }

    public void dataChanges(List<EmojiObject> list) {
        this.listEmoji = list;
        notifyDataSetChanged();
    }
}
