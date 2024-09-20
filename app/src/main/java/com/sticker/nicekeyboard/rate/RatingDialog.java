package com.sticker.nicekeyboard.rate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.sticker.nicekeyboard.R;

import java.util.Objects;


public class RatingDialog extends Dialog {

    private Context context;
    private TextView tvCancel, tvSubmit;
    private ImageView ivClose;
    private OnPress onPress;
    private RatingBar rtb;


    public RatingDialog(Context context2) {
        super(context2, R.style.full_screen_dialog);
        this.context = context2;
        setContentView(R.layout.dialog_rate_app);
        WindowManager.LayoutParams attributes = Objects.requireNonNull(getWindow()).getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attributes);
        getWindow().setSoftInputMode(16);
        rtb = (RatingBar) findViewById(R.id.rtb);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        ivClose = (ImageView) findViewById(R.id.close);
        rtb.setRating(5.0f);

        onclick();
    }

    public interface OnPress {
        void send(float rate);

        void rating();

        void cancel();

        void later();
    }

    public void init(Context context, OnPress onPress) {
        this.onPress = onPress;
    }

    public void setOnPress(OnPress onPress) {
        this.onPress = onPress;
    }

    public void onclick() {
        tvSubmit.setOnClickListener(view -> {

            int starNumber = (int) rtb.getRating();

            if (starNumber == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.Please_feedback), Toast.LENGTH_SHORT).show();
                return;
            }
            if (starNumber <= 3.0) {
                onPress.send(rtb.getRating());
            } else {
                //Edit
                onPress.rating();
            }

            Bundle bundle = new Bundle();
            bundle.putString("rate_star", String.valueOf(starNumber));
        });

        tvCancel.setOnClickListener(view -> onPress.later());
        ivClose.setOnClickListener(v -> onPress.later());
    }
}
