package com.sticker.nicekeyboard.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sticker.nicekeyboard.R;

import java.util.List;


public class EmojiCharAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<EmojiObject> listEmoji;
    private Context mContext;

    @Override 
    public long getItemId(int i) {
        return (long) i;
    }

    
    class ViewHolder {

        
        TextView f53a;
        private EmojiCharAdapter this$0;

        ViewHolder(EmojiCharAdapter emojiCharAdapter) {
        }
    }

    public EmojiCharAdapter(Context context, List<EmojiObject> list) {
        this.mContext = context;
        this.listEmoji = list;
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
    @SuppressLint({"WrongConstant"})
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder(this);
        if (view == null) {
            this.layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            view = this.layoutInflater.inflate(R.layout.adapter_emoji_char, viewGroup, false);
            viewHolder.f53a = (TextView) view.findViewById(R.id.tvEmojiChar);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.f53a.setText(this.listEmoji.get(i).getKeyEmoji());
        return view;
    }

    public void setDataChanges(List<EmojiObject> list) {
        this.listEmoji = list;
        notifyDataSetChanged();
    }
}
