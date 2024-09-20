package com.sticker.nicekeyboard.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sticker.nicekeyboard.R;


import java.util.List;


public class ListImageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> listImage;

    @Override 
    public long getItemId(int i) {
        return (long) i;
    }

    
    class ViewHolder {

        
        ImageView f57a;
        private ListImageAdapter this$0;

        ViewHolder(ListImageAdapter listImageAdapter) {
        }
    }

    public ListImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.listImage = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override 
    public int getCount() {
        return this.listImage.size();
    }

    @Override 
    public Object getItem(int i) {
        return this.listImage.get(i);
    }

    @Override 
    @SuppressLint({"WrongConstant"})
    public View getView(int i, View view, ViewGroup viewGroup) {
        new DisplayMetrics();
        this.layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        ViewHolder viewHolder = new ViewHolder(this);
        if (view == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_list_image, viewGroup, false);
            viewHolder.f57a = (ImageView) view.findViewById(R.id.imgEmoiji);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(this.context).load(this.listImage.get(i)).into(viewHolder.f57a);
        return view;
    }

    public void setDataChanges(List<String> list) {
        this.listImage = list;
        notifyDataSetChanged();
    }
}
