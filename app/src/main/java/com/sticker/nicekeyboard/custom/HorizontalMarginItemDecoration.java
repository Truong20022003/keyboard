package com.sticker.nicekeyboard.custom;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {
    Context context;
    @DimenRes
    int horizontalMarginInDp;
    private int horizontalMarginInPx;

    public HorizontalMarginItemDecoration(Context context, int horizontalMarginInDp) {
        this.context = context;
        this.horizontalMarginInDp = horizontalMarginInDp;
        horizontalMarginInPx = (int) context.getResources().getDimension(horizontalMarginInDp);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = horizontalMarginInPx;
        outRect.left = horizontalMarginInPx;
    }
}
